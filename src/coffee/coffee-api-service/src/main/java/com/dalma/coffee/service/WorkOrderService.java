package com.dalma.coffee.service;

import com.dalma.coffee.contract.work.order.dto.IntegrateWorkOrderOutputDto;
import com.dalma.coffee.erp.Erp;
import com.dalma.coffee.erp.api.ErpWorkOrder;
import com.dalma.coffee.erp.factory.ErpFactory;
import com.dalma.coffee.service.util.WorkOrderUtil;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import com.dalma.contract.dto.warehouse.BrokerWarehouseSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrationOutputDto;
import com.dalma.contract.dto.work.order.item.BrokerWorkOrderItemInputDto;
import com.dalma.contract.dto.work.order.schedule.WorkOrderSchedule;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class WorkOrderService {
	@Value("${erp.work.orders.type}")
	private String erpType;

	public static final String SCHEDULE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private final ErpFactory erpFactory;
	private final ModelMapper modelMapper;
	private final WorkOrderUtil workOrderUtil;

	public WorkOrderService(ErpFactory erpFactory, ModelMapper modelMapper, WorkOrderUtil workOrderUtil) {
		this.erpFactory = erpFactory;
		this.modelMapper = modelMapper;
		this.workOrderUtil = workOrderUtil;
	}

	public IntegrateWorkOrderOutputDto integrateWorkOrders() {
		IntegrateWorkOrderOutputDto output = new IntegrateWorkOrderOutputDto();
		Erp erp = erpFactory.getErp(erpType);
		List<ErpWorkOrder> erpOrders = erp.integrateWorkOrders();
		List<WorkOrderSchedule> orders = new LinkedList<>();
		erpOrders.stream().forEach(order -> {
			try {
				// Ensure that workstation exists
				BrokerWorkStationSummaryOutputDto workstation = workOrderUtil
						.retrieveWorkstation(order.getWorkStationId());
				BrokerWarehouseSummaryOutputDto warehouse = workOrderUtil.retrieveWarehouse(order.getWarehouseId());

				List<BrokerWorkOrderItemInputDto> material = workOrderUtil.handleMaterial(order, warehouse);

				BrokerWorkOrderInputDto brokerOrder = modelMapper.map(order, BrokerWorkOrderInputDto.class);
				brokerOrder.setMaterials(material);
				brokerOrder.setWarehouseId(warehouse.getId());
				brokerOrder.setWorkingStationId(workstation.getId());

				BrokerWorkOrderIntegrationOutputDto workOrder = workOrderUtil.integrateWorkOrder(brokerOrder);
				WorkOrderSchedule scheduleData = new WorkOrderSchedule();
				scheduleData.setId(workOrder.getOrionId());
				scheduleData.setDate(new SimpleDateFormat(SCHEDULE_FORMAT).parse(brokerOrder.getScheduledAt()));
				scheduleData.setScheduleIds(workOrder.getScheduleIds());
				orders.add(scheduleData);
				output.getWorkOrderIds().add(workOrder.getOrionId());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				output.getErrors().add(new StringBuilder("ErpOrderId ").append(order.getOrderId())
						.append(": ").append(e.getMessage()).toString());
			}
		});

		// Requires LATTE up
		//if (!orders.isEmpty()) {
		//	workOrderUtil.scheduleOrderExecution(orders);
		//}
		
		if (!output.getErrors().isEmpty()) {
			log.error("Error integrating work orders: {}", output.getErrors().toString());
		}
		return output;
	}
}
