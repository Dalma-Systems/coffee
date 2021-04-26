package com.dalma.coffee.service.job;

import com.dalma.coffee.service.WorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkOrderIntegrationJob {
	private final WorkOrderService workOrderService;

	public WorkOrderIntegrationJob(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	@Scheduled(cron = "${schedule.integrate.work.orders}")
	public void integrateWorkOrderJob() {
		log.info("[JOB] - Integrating work orders...");

		workOrderService.integrateWorkOrders();

		log.info("[JOB] - Work orders integration DONE!");
	}
}
