package com.dalma.coffee.api.controller;

import com.dalma.coffee.api.BaseITest;
import com.dalma.coffee.erp.Sap;
import com.dalma.coffee.erp.soap.client.gen.ZFTSPLANResponse;
import com.dalma.coffee.erp.soap.client.gen.ZFTSPLNPLANO;
import com.dalma.coffee.service.bridge.BrokerBridge;
import com.dalma.coffee.service.enums.BrokerApiPath;
import com.dalma.coffee.service.error.WorkOrderExceptionError;
import com.dalma.coffee.service.mapper.ErpMapper;
import com.dalma.common.util.Constant;
import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import com.dalma.contract.dto.warehouse.BrokerWarehouseSummaryOutputDto;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialInputDto;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrateOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderMaterialsSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderSummaryOutputDto;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import com.dalma.contract.dto.work.order.erp.IntegrateWorkOrderOutputDto;
import com.dalma.contract.dto.work.order.item.BrokerWorkOrderItemInputDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.dalma.coffee.contract.Paths.BASE_PATH;
import static com.dalma.coffee.contract.Paths.INTEGRATE;
import static com.dalma.coffee.contract.Paths.WORK_ORDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkOrderControllerITest extends BaseITest {
	private static final String SPACES_REGEX = "[\\n\\t ]";
	private static final String ORDER_HOUR = "15:00:01";
	private static final String ORDER_DAY = "2050-01-01";
	private static final String LOG_MACRO = "{0}";
	private static final String MATERIAL_DESC = "material desc";
	private static final String UNIT = "unit";
	private static final double QUANTITY = 1.0;
	private static final String MATERIAL_ID = "material";
	private static final String BATCH = "1";
	private static final String ORDER_ERP_ID = "order_id";
	private static final String WORKSTATION_ERP_ID = "workstation_id";
	private static final String WAREHOUSE_ERP_ID = "warehouse_id";

	@Autowired
	private Gson gson;

	@Autowired
	private ModelMapper modelMapper;

	@MockBean
	private BrokerBridge brokerBridge;

	@MockBean
	private Sap sap;

	private static final String WORK_ORDER_CONTROLLER_PATH = BASE_PATH + WORK_ORDER;
	private static Gson gsonNulls;

	@BeforeAll
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		gsonNulls = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	}

	@Test
	@Order(1)
	public void testIntegrateEmptyOrders() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(Collections.emptyList());

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getWorkOrderIds().size());
		assertEquals(0, response.getBody().getErrors().size());
	}

	@Test
	@Order(1)
	public void testIntegrateOrdersWithErpExamples() throws IOException, JAXBException, DatatypeConfigurationException {
		Mockito.when(sap.parseResponse(Mockito.any(ZFTSPLANResponse.class))).thenCallRealMethod();
		XmlMapper xmlMapper = new XmlMapper();

		String response = new String(Files.readAllBytes(
				new File(getClass().getClassLoader().getResource("sap_responses/error.xml").getFile()).toPath()));
		ZFTSPLANResponse input = xmlMapper.readValue(response, ZFTSPLANResponse.class);
		assertEquals(0, sap.parseResponse(input).size());

		String response2 = new String(Files.readAllBytes(
				new File(getClass().getClassLoader().getResource("sap_responses/no_error.xml").getFile()).toPath()));
		ZFTSPLANResponse input2 = xmlMapper.readValue(response2, ZFTSPLANResponse.class);
		assertEquals(0, sap.parseResponse(input2).size());

		String response3 = new String(Files.readAllBytes(
				new File(getClass().getClassLoader().getResource("sap_responses/no_planning.xml").getFile()).toPath()));
		ZFTSPLANResponse input3 = xmlMapper.readValue(response3, ZFTSPLANResponse.class);
		assertEquals(0, sap.parseResponse(input3).size());

		ZFTSPLNPLANO plano = new ZFTSPLNPLANO();
		plano.setAGRUPADOR("0000012");
		plano.setDEPOSITO("0075");
		plano.setTXTDEPOSITO("Arm.Autom. Graus");
		plano.setESTACAO("0161");
		plano.setTXTESTACAO("K50_1");
		plano.setORDEM("000001139832");
		plano.setMATERIAL("000000000001001679");
		plano.setTXTMATERIAL("GD40 C/ PARAFINA");
		plano.setLOTE("H0003");
		plano.setDIA("2020-09-22");
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		XMLGregorianCalendar date;
		date = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		plano.setHORA(date);
		plano.setQUANTIDADE(BigDecimal.valueOf(15.198));
		plano.setUNIDADE("KG");
		plano.setESTADO("WAITING");

		ErpWorkOrder parseResponse = modelMapper.map(plano, ErpWorkOrder.class);
		assertNotNull(parseResponse);
		assertEquals(plano.getLOTE(), parseResponse.getBatch());
		assertEquals(plano.getDIA(), parseResponse.getDay());
		assertNotNull(plano.getAGRUPADOR());
		Date time = plano.getHORA().toGregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		assertEquals(sdf.format(time), parseResponse.getHour());
		assertEquals(plano.getTXTMATERIAL(), parseResponse.getMaterialDesc());
		assertEquals(plano.getMATERIAL(), parseResponse.getMaterialId());
		assertEquals(plano.getORDEM(), parseResponse.getOrderId());
		assertEquals(plano.getQUANTIDADE(), BigDecimal.valueOf(parseResponse.getQuantity()));
		assertNotNull(plano.getESTADO());
		assertEquals(plano.getUNIDADE(), parseResponse.getUnit());
		assertNotNull(plano.getTXTDEPOSITO());
		assertEquals(plano.getDEPOSITO(), parseResponse.getWarehouseId());
		assertNotNull(plano.getTXTESTACAO());
		assertEquals(plano.getESTACAO(), parseResponse.getWorkStationId());
	}

	@Test
	@Order(1)
	public void testIntegrateWithUnknownWorkStation() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());
		Mockito.when(brokerBridge.getFromBroker(Mockito.anyString())).thenReturn(Strings.EMPTY);

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getWorkOrderIds().size());
		assertTrue(response.getBody().getErrors().size() > 0);

		assertTrue(response.getBody().getErrors().get(0).contains(
				WorkOrderExceptionError.WORKSTATION_NOT_EXISTS.getMessage().replace(LOG_MACRO, WORKSTATION_ERP_ID)));
	}

	@Test
	@Order(2)
	public void testIntegrateWithUnknownWarehouse() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_STATION.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WORKSTATION_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWorkstationBrokerOutput()));

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getWorkOrderIds().size());
		assertTrue(response.getBody().getErrors().size() > 0);

		assertTrue(response.getBody().getErrors().get(0).contains(
				WorkOrderExceptionError.WAREHOUSE_NOT_EXISTS.getMessage().replace(LOG_MACRO, WAREHOUSE_ERP_ID)));
	}

	@Test
	@Order(3)
	public void testCreateInvalidMaterial() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());

		// Workstation
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_STATION.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WORKSTATION_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWorkstationBrokerOutput()));
		// Warehouse
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWarehouseBrokerOutput()));
		// Material
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(ORDER_ERP_ID).toString()))
				.thenReturn(null);
		BrokerWarehouseMaterialInputDto materialRequest = createMaterialRequest();
		Mockito.when(brokerBridge.postToBroker(gson.toJson(materialRequest),
				new StringBuilder(BrokerApiPath.WAREHOUSE.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID)
						.append(BrokerApiPath.MATERIAL.getPath()).toString()))
				.thenReturn(gson.toJson(createMaterialBrokerInvalidOutput()));

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getWorkOrderIds().size());
		assertTrue(response.getBody().getErrors().size() > 0);

		assertTrue(response.getBody().getErrors().get(0).contains(WorkOrderExceptionError.UNABLE_CREATE_MATERIAL
				.getMessage().replace(LOG_MACRO, materialRequest.getErpId())));
	}

	@Test
	@Order(3)
	public void testUpdateMaterial() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());

		// Workstation
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_STATION.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WORKSTATION_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWorkstationBrokerOutput()));
		// Warehouse
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWarehouseBrokerOutput()));
		// Work Order
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(ORDER_ERP_ID)
				.append(Constant.QUERY).append("scheduledAt").append(Constant.EQUAL).append(ORDER_DAY).append("T").append(ORDER_HOUR).toString()))
				.thenReturn(gson.toJson(retrieveWorkOrderBrokerOutput()));
		BrokerWarehouseMaterialInputDto materialRequest = updateMaterialRequest();
		// Material
		Mockito.when(brokerBridge.patchToBroker(gson.toJson(materialRequest),
				new StringBuilder(BrokerApiPath.WAREHOUSE.getPath()).append(BrokerApiPath.MATERIAL.getPath())
						.append(Constant.SLASH).append(materialRequest.getMaterial()).toString()))
				.thenReturn(Strings.EMPTY);
		// Work order
		Mockito.when(
				brokerBridge
						.postToBroker(
								gsonNulls.toJson(createWorkOrderRequest()).replaceAll(SPACES_REGEX, Strings.EMPTY),
								new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
										.append(BrokerApiPath.INTEGRATE.getPath()).toString()))
				.thenReturn(gson.toJson(createWorkOrderBrokerOutput(Boolean.TRUE.booleanValue())));

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getErrors().size());
		assertTrue(response.getBody().getWorkOrderIds().size() > 0);
		assertTrue(response.getBody().getWorkOrderIds().get(0).contains(ORDER_ERP_ID));
	}

	@Test
	@Order(3)
	public void testErrorIntegratingWorkOrder() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());

		// Workstation
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_STATION.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WORKSTATION_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWorkstationBrokerOutput()));
		// Warehouse
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWarehouseBrokerOutput()));
		// Material
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(ORDER_ERP_ID).toString()))
				.thenReturn(null);
		BrokerWarehouseMaterialInputDto materialRequest = createMaterialRequest();
		Mockito.when(brokerBridge.postToBroker(gson.toJson(materialRequest),
				new StringBuilder(BrokerApiPath.WAREHOUSE.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID)
						.append(BrokerApiPath.MATERIAL.getPath()).toString()))
				.thenReturn(gson.toJson(createMaterialBrokerOutput()));
		// Work order
		Mockito.when(
				brokerBridge
						.postToBroker(
								gsonNulls.toJson(createWorkOrderRequest()).replaceAll(SPACES_REGEX, Strings.EMPTY),
								new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
										.append(BrokerApiPath.INTEGRATE.getPath()).toString()))
				.thenReturn(gson.toJson(createWorkOrderBrokerOutput(Boolean.FALSE.booleanValue())));

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getWorkOrderIds().size());
		assertTrue(response.getBody().getErrors().size() > 0);
	}

	@Test
	@Order(3)
	public void testIntegrateWorkOrder() {
		Mockito.when(sap.integrateWorkOrders()).thenReturn(createOrdersResponse());

		// Workstation
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_STATION.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WORKSTATION_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWorkstationBrokerOutput()));
		// Warehouse
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WAREHOUSE.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID).toString()))
				.thenReturn(gson.toJson(retrieveWarehouseBrokerOutput()));
		// Material
		Mockito.when(brokerBridge.getFromBroker(new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
				.append(BrokerApiPath.EXTERNAL.getPath()).append(Constant.SLASH).append(ORDER_ERP_ID).toString()))
				.thenReturn(null);
		BrokerWarehouseMaterialInputDto materialRequest = createMaterialRequest();
		Mockito.when(brokerBridge.postToBroker(gson.toJson(materialRequest),
				new StringBuilder(BrokerApiPath.WAREHOUSE.getPath()).append(Constant.SLASH).append(WAREHOUSE_ERP_ID)
						.append(BrokerApiPath.MATERIAL.getPath()).toString()))
				.thenReturn(gson.toJson(createMaterialBrokerOutput()));
		// Work order
		Mockito.when(
				brokerBridge
						.postToBroker(
								gsonNulls.toJson(createWorkOrderRequest()).replaceAll(SPACES_REGEX, Strings.EMPTY),
								new StringBuilder(BrokerApiPath.WORK_ORDER.getPath())
										.append(BrokerApiPath.INTEGRATE.getPath()).toString()))
				.thenReturn(gson.toJson(createWorkOrderBrokerOutput(Boolean.TRUE.booleanValue())));

		ResponseEntity<IntegrateWorkOrderOutputDto> response = restTemplate.exchange(
				HTTP_LOCALHOST + port + WORK_ORDER_CONTROLLER_PATH + INTEGRATE, HttpMethod.POST, null,
				IntegrateWorkOrderOutputDto.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getWorkOrderIds());
		assertNotNull(response.getBody().getErrors());
		assertEquals(0, response.getBody().getErrors().size());
		assertTrue(response.getBody().getWorkOrderIds().size() > 0);
		assertTrue(response.getBody().getWorkOrderIds().get(0).contains(ORDER_ERP_ID));
	}

	private List<BrokerWorkOrderIntegrateOutputDto> createWorkOrderBrokerOutput(boolean success) {
		BrokerWorkOrderIntegrateOutputDto output = new BrokerWorkOrderIntegrateOutputDto();
		output.setOrionId(ORDER_ERP_ID);
		output.setSuccess(success);
		return List.of(output);
	}

	private List<BrokerWorkOrderInputDto> createWorkOrderRequest() {
		BrokerWorkOrderInputDto request = new BrokerWorkOrderInputDto();
		request.setScheduledAt(ORDER_DAY + ErpMapper.DATE_TIME_SEPARATOR + ORDER_HOUR);
		request.setWarehouseId(WAREHOUSE_ERP_ID);
		request.setWorkingStationId(WORKSTATION_ERP_ID);
		request.setErpId(ORDER_ERP_ID);

		BrokerWorkOrderItemInputDto material = new BrokerWorkOrderItemInputDto();
		material.setId(MATERIAL_ID);
		material.setQuantity(QUANTITY);
		request.setMaterials(List.of(material));

		return List.of(request);
	}

	private BrokerWarehouseMaterialInputDto createMaterialRequest() {
		BrokerWarehouseMaterialInputDto material = new BrokerWarehouseMaterialInputDto();
		material.setBatch(BATCH);
		material.setMaterial(MATERIAL_DESC);
		material.setQuantity(QUANTITY);
		material.setUnit(UNIT);
		material.setErpId(MATERIAL_ID);
		return material;
	}

	private BrokerWarehouseMaterialOutputDto createMaterialBrokerInvalidOutput() {
		BrokerWarehouseMaterialOutputDto materialOutput = new BrokerWarehouseMaterialOutputDto();
		materialOutput.setOrionId(Strings.EMPTY);
		return materialOutput;
	}

	private BrokerWarehouseMaterialOutputDto createMaterialBrokerOutput() {
		BrokerWarehouseMaterialOutputDto materialOutput = new BrokerWarehouseMaterialOutputDto();
		materialOutput.setOrionId(MATERIAL_ID);
		return materialOutput;
	}

	private BrokerWarehouseSummaryOutputDto retrieveWarehouseBrokerOutput() {
		BrokerWarehouseSummaryOutputDto warehouseOutput = new BrokerWarehouseSummaryOutputDto();
		BaseOrionAttributeOutputDto<String> erpId = new BaseOrionAttributeOutputDto<>();
		erpId.setValue(WAREHOUSE_ERP_ID);
		warehouseOutput.setErpId(erpId);
		warehouseOutput.setId(WAREHOUSE_ERP_ID);
		return warehouseOutput;
	}

	private BrokerWorkStationSummaryOutputDto retrieveWorkstationBrokerOutput() {
		BrokerWorkStationSummaryOutputDto workstationOutput = new BrokerWorkStationSummaryOutputDto();
		BaseOrionAttributeOutputDto<String> erpId = new BaseOrionAttributeOutputDto<>();
		erpId.setValue(WORKSTATION_ERP_ID);
		workstationOutput.setErpId(erpId);
		workstationOutput.setId(WORKSTATION_ERP_ID);
		return workstationOutput;
	}

	private List<ErpWorkOrder> createOrdersResponse() {
		List<ErpWorkOrder> orders = new ArrayList<>(3);

		ErpWorkOrder order1 = new ErpWorkOrder();
		order1.setBatch(BATCH);
		order1.setDay(ORDER_DAY);
		order1.setHour(ORDER_HOUR);
		order1.setMaterialDesc(MATERIAL_DESC);
		order1.setMaterialId(MATERIAL_ID);
		order1.setOrderId(ORDER_ERP_ID);
		order1.setQuantity(QUANTITY);
		order1.setUnit(UNIT);
		order1.setWarehouseId(WAREHOUSE_ERP_ID);
		order1.setWorkStationId(WORKSTATION_ERP_ID);

		orders.add(order1);
		return orders;
	}

	private BrokerWarehouseMaterialInputDto updateMaterialRequest() {
		BrokerWarehouseMaterialInputDto request = new BrokerWarehouseMaterialInputDto();
		request.setBatch(BATCH);
		request.setErpId(MATERIAL_ID);
		request.setMaterial(MATERIAL_ID);
		request.setQuantity(QUANTITY);
		request.setUnit(UNIT);
		return request;
	}

	private BrokerWorkOrderSummaryOutputDto retrieveWorkOrderBrokerOutput() {
		BrokerWorkOrderSummaryOutputDto output = new BrokerWorkOrderSummaryOutputDto();
		BrokerWorkOrderMaterialsSummaryOutputDto material = new BrokerWorkOrderMaterialsSummaryOutputDto();
		material.setMaterialId(MATERIAL_ID);
		output.setMaterials(List.of(material));
		return output;
	}
}
