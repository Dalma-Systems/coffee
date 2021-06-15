package com.dalma.coffee.api.controller;

import com.dalma.coffee.service.WorkOrderService;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import com.dalma.contract.dto.work.order.erp.IntegrateWorkOrderOutputDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.dalma.coffee.contract.Paths.BASE_PATH;
import static com.dalma.coffee.contract.Paths.INSERT;
import static com.dalma.coffee.contract.Paths.INTEGRATE;
import static com.dalma.coffee.contract.Paths.WORK_ORDER;

@Validated
@RestController
@RequestMapping(BASE_PATH + WORK_ORDER)
public class WorkOrderController {

	private final WorkOrderService workOrderService;

	public WorkOrderController(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	@PostMapping(INTEGRATE)
	@ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Endpoint to force SAP work orders integration", notes = "It executes the same integration executed automatically every day")
	public IntegrateWorkOrderOutputDto integrateWorkOrders() {
		return workOrderService.integrateWorkOrders();
	}

	@PostMapping(INSERT)
	@ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Endpoint to force the integration of a specific work orders", notes = "This endpoint is used by SAP simulator")
	public IntegrateWorkOrderOutputDto insertWorkOrders(@RequestBody List<ErpWorkOrder> orders) {
		return workOrderService.insertGeneratedWorkOrders(orders);
	}
}
