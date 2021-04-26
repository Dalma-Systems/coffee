package com.dalma.coffee.api.controller;

import com.dalma.coffee.contract.work.order.dto.IntegrateWorkOrderOutputDto;
import com.dalma.coffee.service.WorkOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.coffee.contract.Paths.BASE_PATH;
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
	public IntegrateWorkOrderOutputDto integrateWorkOrders() {
		return workOrderService.integrateWorkOrders();
	}
}
