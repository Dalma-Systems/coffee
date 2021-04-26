package com.dalma.coffee.service.bridge;

import com.dalma.coffee.service.exception.LatteAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LatteBridge extends BaseBridge {

	@Value("${dalma.latte.api.url}")
	private String latteUrl;

	@Value("${dalma.latte.api.connect.timeout}")
	private Integer connectTimeout;

	@Value("${dalma.latte.api.read.timeout}")
	private Integer readTimeout;

	public String postToLatte(String payload, String path) {
		try {
			return call(payload, latteUrl, path, HttpMethod.POST, connectTimeout, readTimeout);
		} catch (IOException e) {
			throw new LatteAccessException();
		}
	}
}
