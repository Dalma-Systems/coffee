package com.dalma.coffee.erp;

import com.dalma.coffee.erp.soap.client.gen.ZFTSPLANResponse;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Sap implements Erp {
	@Value("${erp.work.orders.retries}")
	private Integer erpNumberRetries;
	
	@Value("${erp.work.orders.retry.interval}")
	private Integer retryIntervalSeconds;
	
	private static final String SUCCESS_CODE = "OK";
	private final SapClient sapClient;
	private final ModelMapper modelMapper;

	public Sap(SapClient sapClient, ModelMapper modelMapper) {
		this.sapClient = sapClient;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ErpWorkOrder> integrateWorkOrders() {
		int attempt = 1;
		long sleepTime = retryIntervalSeconds * 1000L;
		while (attempt <= erpNumberRetries) {
			log.info("Integrating with SAP attempt {} of {}", attempt, erpNumberRetries);
			try {
				ZFTSPLANResponse response = sapClient.getWorkOrders();
				return parseResponse(response);
			} catch (Exception e) {
				log.error("Error calling SAP to integrate work orders", e);
			}
			attempt += 1;
			try {
				log.info("Waitting {} seconds to try again", retryIntervalSeconds);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) { // NOSONAR
				log.error("Unable to sleep between SAP calls", e);
			}
		}
		return Collections.emptyList();
	}

	public List<ErpWorkOrder> parseResponse(ZFTSPLANResponse response) {
		if (!SUCCESS_CODE.equals(response.getEXERRO())) {
			log.error("SAP returned error: {}", response.getEXERRO());
			return Collections.emptyList();
		}

		if (response.getTBLPLANODIARIO() == null || response.getTBLPLANODIARIO().getItem() == null
				|| response.getTBLPLANODIARIO().getItem().isEmpty()) {
			log.error("SAP returned success but no orders");
			return Collections.emptyList();
		}

		return response.getTBLPLANODIARIO().getItem().stream().map(order -> modelMapper.map(order, ErpWorkOrder.class))
				.collect(Collectors.toList());
	}
}
