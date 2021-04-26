package com.dalma.coffee.service.util;

import com.dalma.coffee.erp.api.ErpWorkOrder;
import com.dalma.coffee.service.bridge.BrokerBridge;
import com.dalma.coffee.service.bridge.LatteBridge;
import com.dalma.coffee.service.enums.BrokerApiPath;
import com.dalma.coffee.service.enums.LatteApiPath;
import com.dalma.coffee.service.exception.NonExistentWarehouseException;
import com.dalma.coffee.service.exception.NonExistentWorkStationException;
import com.dalma.coffee.service.exception.UnableCreateMaterialException;
import com.dalma.coffee.service.exception.UnableIntegrateWorkOrderException;
import com.dalma.coffee.service.exception.UnableParseWarehouseException;
import com.dalma.coffee.service.exception.UnableParseWorkStationException;
import com.dalma.coffee.service.exception.UnableScheduleWorkOrdersException;
import com.dalma.common.util.Constant;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import com.dalma.contract.dto.warehouse.BrokerWarehouseSummaryOutputDto;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialInputDto;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrationOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderMaterialsSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderSummaryOutputDto;
import com.dalma.contract.dto.work.order.item.BrokerWorkOrderItemInputDto;
import com.dalma.contract.dto.work.order.schedule.WorkOrderSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WorkOrderUtil {
	private final BrokerBridge brokerBridge;
	private final LatteBridge latteBridge;
	private final ObjectMapper objectMapper;
	private final ModelMapper modelMapper;

	public WorkOrderUtil(BrokerBridge brokerBridge, LatteBridge latteBridge, ObjectMapper objectMapper,
			ModelMapper modelMapper) {
		this.brokerBridge = brokerBridge;
		this.latteBridge = latteBridge;
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
	}

	public BrokerWarehouseSummaryOutputDto retrieveWarehouse(String warehouseErpId) {
		try {
			String brokerWarehouse = brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
					.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(warehouseErpId).toString());
			if (Strings.isEmpty(brokerWarehouse)) {
				throw new NonExistentWarehouseException(warehouseErpId);
			}
			BrokerWarehouseSummaryOutputDto warehouse = objectMapper.readValue(brokerWarehouse,
					BrokerWarehouseSummaryOutputDto.class);
			if (warehouse == null) {
				throw new UnableParseWarehouseException(warehouseErpId, brokerWarehouse);
			}

			return warehouse;
		} catch (JsonProcessingException e) {
			log.error("Error retrieving warehouse", e);
		}

		throw new NonExistentWarehouseException(warehouseErpId);
	}

	public BrokerWorkStationSummaryOutputDto retrieveWorkstation(String workstationErpId) {
		try {
			String brokerWorkstation = brokerBridge.getFromBroker(
					new StringBuilder(BrokerApiPath.WORK_STATION.getPath()).append(BrokerApiPath.EXTERNAL.getPath())
							.append(Constant.SLASH).append(workstationErpId).toString());
			if (Strings.isEmpty(brokerWorkstation)) {
				throw new NonExistentWorkStationException(workstationErpId);
			}

			BrokerWorkStationSummaryOutputDto workstation = objectMapper.readValue(brokerWorkstation,
					BrokerWorkStationSummaryOutputDto.class);
			if (workstation == null) {
				throw new UnableParseWorkStationException(workstationErpId, brokerWorkstation);
			}

			return workstation;
		} catch (JsonProcessingException e) {
			log.error("Error retrieving workstation", e);
		}

		throw new NonExistentWorkStationException(workstationErpId);
	}

	private String createMaterial(BrokerWarehouseMaterialInputDto material, BrokerWarehouseSummaryOutputDto warehouse) {
		try {
			String materialRequest = objectMapper.writeValueAsString(material);
			String brokerMaterial = brokerBridge.postToBroker(materialRequest,
					new StringBuilder(BrokerApiPath.WAREHOUSE.getPath()).append(Constant.SLASH)
							.append(warehouse.getId()).append(BrokerApiPath.MATERIAL.getPath()).toString());

			BrokerWarehouseMaterialOutputDto materialOrion = objectMapper.readValue(brokerMaterial,
					BrokerWarehouseMaterialOutputDto.class);

			if (!Strings.isEmpty(materialOrion.getOrionId())) {
				return materialOrion.getOrionId();
			}
		} catch (JsonProcessingException e) {
			log.error("Error creating work order material", e);
		}

		throw new UnableCreateMaterialException(material.getErpId());
	}

	private void updateMaterial(String id, BrokerWarehouseMaterialInputDto material) {
		try {
			String materialRequest = objectMapper.writeValueAsString(material);
			brokerBridge.patchToBroker(materialRequest, new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
					.append(BrokerApiPath.MATERIAL.getPath()).append(Constant.SLASH).append(id).toString());
		} catch (JsonProcessingException e) {
			log.error("Error updating work order material", e);
		}
	}

	public BrokerWorkOrderIntegrationOutputDto integrateWorkOrder(BrokerWorkOrderInputDto order) {
		try {
			String orderRequest = objectMapper.writeValueAsString(order);
			String brokerOrder = brokerBridge.postToBroker(orderRequest,
					new StringBuilder(BrokerApiPath.WORK_ORDER.getPath()).append(BrokerApiPath.INTEGRATE.getPath())
							.toString());

			if (brokerOrder != null) {
				BrokerWorkOrderIntegrationOutputDto orderOrion = objectMapper.readValue(brokerOrder,
						BrokerWorkOrderIntegrationOutputDto.class);
				if (!Strings.isEmpty(orderOrion.getOrionId())) {
					return orderOrion;
				}
			}
		} catch (JsonProcessingException e) {
			log.error("Error integrating work order", e);
		}

		throw new UnableIntegrateWorkOrderException(order.getErpId());
	}

	public void scheduleOrderExecution(List<WorkOrderSchedule> orders) {
		try {
			String scheduleRequest = objectMapper.writeValueAsString(orders);
			latteBridge.postToLatte(scheduleRequest, LatteApiPath.WORK_ODER_SCHEDULE.getPath());
		} catch (JsonProcessingException e) {
			log.error("Error creating work order", e);
			throw new UnableScheduleWorkOrdersException(
					orders.stream().map(WorkOrderSchedule::getId).collect(Collectors.toList()));
		}
	}

	public List<BrokerWorkOrderItemInputDto> handleMaterial(ErpWorkOrder order,
			BrokerWarehouseSummaryOutputDto warehouse) {
		List<BrokerWorkOrderItemInputDto> orderItems = new LinkedList<>();
		BrokerWarehouseMaterialInputDto warehouseMaterial = modelMapper.map(order,
				BrokerWarehouseMaterialInputDto.class);
		BrokerWorkOrderSummaryOutputDto workOrder = retrieveWorkOrder(order.getOrderId());
		if (workOrder != null && workOrder.getMaterials() != null && !workOrder.getMaterials().isEmpty()) {
			for (BrokerWorkOrderMaterialsSummaryOutputDto material : workOrder.getMaterials()) {
				updateMaterial(material.getMaterialId(), warehouseMaterial);
				orderItems.add(
						new BrokerWorkOrderItemInputDto(material.getMaterialId(), warehouseMaterial.getQuantity()));
			}
			return orderItems;
		}

		String materialId = createMaterial(warehouseMaterial, warehouse);
		orderItems.add(new BrokerWorkOrderItemInputDto(materialId, warehouseMaterial.getQuantity()));
		return orderItems;
	}

	private BrokerWorkOrderSummaryOutputDto retrieveWorkOrder(String orderId) {
		try {
			String brokerWorkOrder = brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
					.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(orderId).toString());
			if (Strings.isEmpty(brokerWorkOrder)) {
				return null;
			}
			BrokerWorkOrderSummaryOutputDto workOrder = objectMapper.readValue(brokerWorkOrder,
					BrokerWorkOrderSummaryOutputDto.class);
			if (workOrder == null) {
				return null;
			}

			return workOrder;
		} catch (JsonProcessingException e) {
			log.error("Error retrieving work order", e);
		}

		return null;
	}
}
