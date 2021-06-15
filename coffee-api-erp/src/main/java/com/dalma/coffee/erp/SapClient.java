package com.dalma.coffee.erp;

import com.dalma.coffee.erp.soap.client.gen.TABLEOFZFTSPLNPLANO;
import com.dalma.coffee.erp.soap.client.gen.ZFTSPLAN;
import com.dalma.coffee.erp.soap.client.gen.ZFTSPLANResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.StringWriter;

@Slf4j
public class SapClient extends WebServiceGatewaySupport {

	private final String url;

	public SapClient(String url) {
		this.url = url;
	}

	public ZFTSPLANResponse getWorkOrders() {
		ZFTSPLAN request = new ZFTSPLAN();
		request.setTBLPLANODIARIO(new TABLEOFZFTSPLNPLANO());
		ZFTSPLANResponse response = (ZFTSPLANResponse) getWebServiceTemplate().marshalSendAndReceive(url, request);
		logResponse(response);
		return response;
	}

	private void logResponse(ZFTSPLANResponse response) {
		if (!log.isInfoEnabled()) {
			return;
		}
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ZFTSPLANResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter output = new StringWriter();
			jaxbMarshaller.marshal(response, output);
			log.info("Sap response {}", output.toString());
		} catch (JAXBException e) {
			log.error("Error logging Sap response", e);
		}
	}
}
