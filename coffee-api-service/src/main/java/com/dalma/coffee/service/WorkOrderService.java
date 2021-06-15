package com.dalma.coffee.service;

import com.dalma.coffee.erp.Erp;
import com.dalma.coffee.erp.factory.ErpFactory;
import com.dalma.coffee.service.util.WorkOrderUtil;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import com.dalma.contract.dto.warehouse.BrokerWarehouseSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrateOutputDto;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import com.dalma.contract.dto.work.order.erp.IntegrateWorkOrderOutputDto;
import com.dalma.contract.dto.work.order.item.BrokerWorkOrderItemInputDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkOrderService {
	@Value("${erp.work.orders.type}")
	private String erpType;

	private final ErpFactory erpFactory;
	private final ModelMapper modelMapper;
	private final WorkOrderUtil workOrderUtil;

	public WorkOrderService(ErpFactory erpFactory, ModelMapper modelMapper, WorkOrderUtil workOrderUtil) {
		this.erpFactory = erpFactory;
		this.modelMapper = modelMapper;
		this.workOrderUtil = workOrderUtil;
	}

	public IntegrateWorkOrderOutputDto integrateWorkOrders() {
		Erp erp = erpFactory.getErp(erpType);
		List<ErpWorkOrder> erpOrders = erp.integrateWorkOrders();
		return integrateOrders(erpOrders);
	}
	
	public IntegrateWorkOrderOutputDto insertGeneratedWorkOrders(List<ErpWorkOrder> orders) {
		return integrateOrders(orders);
	}

	private IntegrateWorkOrderOutputDto integrateOrders(List<ErpWorkOrder> erpOrders) {
		IntegrateWorkOrderOutputDto output = new IntegrateWorkOrderOutputDto();
		List<BrokerWorkOrderInputDto> ordersToIntegrate = new ArrayList<>(erpOrders.size());
		erpOrders.stream().forEach(order -> {
			try {
				// Ensure that workstation exists
				BrokerWorkStationSummaryOutputDto workstation = workOrderUtil
						.retrieveWorkstation(order.getWorkStationId());
				// Ensure that warehouse exists
				BrokerWarehouseSummaryOutputDto warehouse = workOrderUtil.retrieveWarehouse(order.getWarehouseId());

				BrokerWorkOrderInputDto brokerOrder = modelMapper.map(order, BrokerWorkOrderInputDto.class);
				brokerOrder.setWarehouseId(warehouse.getId());
				brokerOrder.setWorkingStationId(workstation.getId());

				// Create or update material in warehouse
				List<BrokerWorkOrderItemInputDto> material = workOrderUtil.handleMaterial(order, warehouse, brokerOrder.getScheduledAt());
				brokerOrder.setMaterials(material);

				// Store order to integrate all at once
				ordersToIntegrate.add(brokerOrder);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				output.getErrors().add(new StringBuilder("ErpOrderId ").append(order.getOrderId())
						.append(": ").append(e.getMessage()).toString());
			}
		});
		
		// Integrate work orders in orion
		if (!ordersToIntegrate.isEmpty()) {
			List<BrokerWorkOrderIntegrateOutputDto> workOrders = workOrderUtil.integrateWorkOrder(ordersToIntegrate);
			workOrders.stream().forEach(order -> {
				if (order.isSuccess()) {
					output.getWorkOrderIds().add(order.getOrionId());
				} else {
					output.getErrors().add(new StringBuilder("ErpOrderId ").append(order.getOrionId()).toString());
				}
			});
		}

		if (!output.getErrors().isEmpty()) {
			log.error("Error integrating work orders: {}", output.getErrors().toString());
		}
		return output;
	}
}
