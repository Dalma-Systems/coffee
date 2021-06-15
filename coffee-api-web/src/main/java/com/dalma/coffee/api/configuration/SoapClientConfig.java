package com.dalma.coffee.api.configuration;

import com.dalma.coffee.erp.SapClient;
import com.dalma.coffee.erp.soap.client.gen.ZFTSPLANResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class SoapClientConfig {

	@Value("${erp.work.orders.url}")
	private String erpUrl;

	@Value("${erp.work.orders.username}")
	private String userName;

	@Value("${erp.work.orders.password}")
	private String userPassword;

	@Bean
	Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath(ZFTSPLANResponse.class.getPackageName());
		return jaxb2Marshaller;
	}

	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSender() {
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		// Set the basic authorization credentials
		httpComponentsMessageSender.setCredentials(usernamePasswordCredentials());
		return httpComponentsMessageSender;
	}

	@Bean
	public UsernamePasswordCredentials usernamePasswordCredentials() {
		return new UsernamePasswordCredentials(userName, userPassword);
	}
	
	@Bean
	public SapClient sapClient(Jaxb2Marshaller marshaller) {
		SapClient client = new SapClient(erpUrl);
		client.setDefaultUri(erpUrl);
	    client.setMarshaller(marshaller);
	    client.setUnmarshaller(marshaller);
	    client.setMessageSender(httpComponentsMessageSender());
	    return client;
	}
}
