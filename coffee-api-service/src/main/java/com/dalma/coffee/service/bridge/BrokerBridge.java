package com.dalma.coffee.service.bridge;

import com.dalma.coffee.service.exception.BrokerAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BrokerBridge extends BaseBridge {

	@Value("${dalma.broker.api.url}")
	private String brokerUrl;

	@Value("${dalma.broker.api.connect.timeout}")
	private Integer connectTimeout;

	@Value("${dalma.broker.api.read.timeout}")
	private Integer readTimeout;

	public String postToBroker(String payload, String path) {
		try {
			return call(payload, brokerUrl, path, HttpMethod.POST, connectTimeout, readTimeout);
		} catch (IOException e) {
			throw new BrokerAccessException();
		}
	}

	public String getFromBroker(String path) {
		try {
			return call(null, brokerUrl, path, HttpMethod.GET, connectTimeout, readTimeout);
		} catch (IOException e) {
			throw new BrokerAccessException();
		}
	}
	
	public String patchToBroker(String payload, String path) {
		try {
			return call(payload, brokerUrl, path, HttpMethod.PATCH, connectTimeout, readTimeout);
		} catch (IOException e) {
			throw new BrokerAccessException();
		}
	}
}
