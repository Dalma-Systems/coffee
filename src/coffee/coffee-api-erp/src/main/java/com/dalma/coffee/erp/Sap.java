package com.dalma.coffee.erp;

import com.dalma.coffee.erp.api.ErpWorkOrder;
import com.dalma.coffee.erp.api.SapWorkOrder;
import com.dalma.coffee.erp.exception.SapErrorException;
import com.dalma.coffee.erp.exception.SapNoOrdersException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Sap implements Erp {
	private static final String SUCCESS_CODE = "OK";

	@Value("${erp.work.orders.url}")
	private String sapUrl;

	@Value("${erp.work.orders.timeout}")
	private Integer connectTimeout;

	@Value("${erp.work.orders.timeout}")
	private Integer readTimeout;

	private final MappingJackson2XmlHttpMessageConverter xmlConverter;
	private final ModelMapper modelMapper;

	public Sap(MappingJackson2XmlHttpMessageConverter xmlConverter, ModelMapper modelMapper) {
		this.xmlConverter = xmlConverter;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ErpWorkOrder> integrateWorkOrders() {
		try {
			URL url = new URL(sapUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(HttpMethod.GET.name());
			con.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			con.setConnectTimeout(connectTimeout);
			con.setReadTimeout(readTimeout);

			int responseCode = con.getResponseCode();
			if (HttpStatus.valueOf(responseCode).is2xxSuccessful()) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				SapWorkOrder sapResponse = xmlConverter.getObjectMapper().readValue(response.toString(),
						SapWorkOrder.class);
				log.info(sapResponse.toString());

				if (!SUCCESS_CODE.equals(sapResponse.getError())) {
					throw new SapErrorException(sapResponse.getError());
				}

				if (sapResponse.getPlanning() == null || sapResponse.getPlanning().getItem() == null
						|| sapResponse.getPlanning().getItem().isEmpty()) {
					throw new SapNoOrdersException();
				}

				return sapResponse.getPlanning().getItem().stream()
						.map(order -> modelMapper.map(order, ErpWorkOrder.class)).collect(Collectors.toList());
			}
		} catch (IOException e) {
			log.error("Error calling SAP to integrate work orders", e);
		}
		return Collections.emptyList();
	}
}
