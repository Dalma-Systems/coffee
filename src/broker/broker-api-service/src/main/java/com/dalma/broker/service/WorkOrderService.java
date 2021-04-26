package com.dalma.broker.service;

import com.dalma.broker.contract.dto.warehouse.material.BrokerWarehouseMaterialSummaryOutputDto;
import com.dalma.broker.fiware.orion.connector.entity.robot.Robot;
import com.dalma.broker.fiware.orion.connector.entity.work.order.WorkOrder;
import com.dalma.broker.fiware.orion.connector.entity.work.order.field.WorkOrderField;
import com.dalma.broker.fiware.orion.connector.entity.work.order.item.WorkOrderItem;
import com.dalma.broker.service.exception.workorder.WorkOrderNotFoundException;
import com.dalma.broker.service.exception.workorder.WorkOrderUpdateNotAllowedException;
import com.dalma.broker.service.publisher.WorkOrderPublisher;
import com.dalma.broker.service.reader.RobotReader;
import com.dalma.broker.service.reader.WorkOrderItemReader;
import com.dalma.broker.service.reader.WorkOrderReader;
import com.dalma.common.util.Constant;
import com.dalma.common.workorder.enums.WorkOrderStatus;
import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrationOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderMaterialsSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderSummaryOutputDto;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class WorkOrderService extends
        BaseOrionCrudService<BrokerWorkOrderInputDto, BrokerWorkOrderOutputDto, BrokerWorkOrderSummaryOutputDto, WorkOrder, WorkOrderPublisher, WorkOrderReader> {

    private final WorkOrderPublisher publisher;
    private final WorkOrderReader reader;
    private final WorkOrderItemService workorderItemService;
    private final WorkOrderItemReader workorderItemReader;
    private final RobotReader robotReader;
    private final ModelMapper modelMapper;
    private final WarehouseMaterialService materialService;
    private final WarehouseService warehouseService;
    private final WorkStationService workStationService;

    public WorkOrderService(WorkOrderPublisher publisher, WorkOrderReader reader,
                            WorkOrderItemService workorderItemService, ModelMapper modelMapper, WorkOrderItemReader workorderItemReader,
                            RobotReader robotReader, WarehouseMaterialService materialService, WarehouseService warehouseService, WorkStationService workStationService) {
        super(publisher, reader);
        this.publisher = publisher;
        this.reader = reader;
        this.workorderItemService = workorderItemService;
        this.modelMapper = modelMapper;
        this.workorderItemReader = workorderItemReader;
        this.robotReader = robotReader;
        this.materialService = materialService;
        this.warehouseService = warehouseService;
        this.workStationService = workStationService;
    }

    @Override
    protected String getType() {
        return WorkOrder.ENTITY_TYPE;
    }

    @Override
    protected BrokerWorkOrderOutputDto mapOrionEntityToEntity(WorkOrder orionEntity) {
        return new BrokerWorkOrderOutputDto(orionEntity.getId());
    }

    @Override
    public BrokerWorkOrderOutputDto create(BrokerWorkOrderInputDto entity) {
        WorkOrder orionEntity = publisher.create(entity);

        // Associate workstation and warehouse
        publisher.append(orionEntity, List.of(entity.getWorkingStationId(), entity.getWarehouseId()));

        // Associate materials - to identify the materials with
        // the respective quantity instead of create association
        // of order - materials create a bridge table (many to
        // many association)
        entity.getMaterials().stream().forEach(material -> workorderItemService.create(material, orionEntity.getId()));
        return mapOrionEntityToEntity(orionEntity);
    }

    @Override
    public BrokerWorkOrderSummaryOutputDto get(String id) {
        BrokerWorkOrderSummaryOutputDto orionEntity = reader.readObject(id);
        orionEntity.setMaterials(retriveOrderMaterials(orionEntity.getId()));
        orionEntity.setRobotId(retrieveWorkOrderRobot(id));
        return orionEntity;
    }

    @Override
    public void update(String id, BrokerWorkOrderInputDto entity) {
        super.update(id, entity);
        if (!Strings.isEmpty(entity.getWarehouseId()) || !Strings.isEmpty(entity.getWorkingStationId())) {
            // Associate workstation and warehouse
            WorkOrder orionEntity = reader.readOrionEntity(id);
            List<String> relationships = new LinkedList<>();
            if (!Strings.isEmpty(entity.getWorkingStationId())) {
                relationships.add(entity.getWorkingStationId());
            } else {
                relationships.add(Strings.EMPTY);
            }
            if (!Strings.isEmpty(entity.getWarehouseId())) {
                relationships.add(entity.getWarehouseId());
            } else {
                relationships.add(Strings.EMPTY);
            }
            publisher.append(orionEntity, relationships);
        }
    }

    public List<BrokerWorkOrderSummaryOutputDto> getAll(String start, String end) {
        List<WorkOrder> orders = reader.readOrionEntityQueryListBetween(WorkOrderField.SCHEDULED_AT.getField(), start,
                end, WorkOrder.ENTITY_TYPE);

        List<BrokerWorkOrderSummaryOutputDto> output = new ArrayList<>(orders.size());
        for (WorkOrder order : orders) {
            BrokerWorkOrderSummaryOutputDto orderOutput = modelMapper.map(order, BrokerWorkOrderSummaryOutputDto.class);
            orderOutput.setWarehouse(warehouseService.get(order.getRefWarehouse().getValue()).getName());
            orderOutput.setWorkstation(workStationService.get(order.getRefWorkstation().getValue()).getName());
            orderOutput.setMaterials(retriveOrderMaterials(order.getId()));
            orderOutput.setRobotId(retrieveWorkOrderRobot(order.getId()));
            output.add(orderOutput);
        }

        return output;
    }

    private BaseOrionAttributeOutputDto<String> retrieveWorkOrderRobot(String id) {
        List<Robot> robots = robotReader.readOrionEntityQueryListRelationship("refWorkOrder", id, Robot.ENTITY_TYPE);
        if (!robots.isEmpty()) {
            BaseOrionAttributeOutputDto<String> robot = new BaseOrionAttributeOutputDto<>();
            robot.setValue(robots.get(0).getId());
            return robot;
        }
        return null;
    }

    private List<BrokerWorkOrderMaterialsSummaryOutputDto> retriveOrderMaterials(String id) {
        List<WorkOrderItem> workorderItems = workorderItemReader.readOrionEntityQueryListRelationship(
                WorkOrderField.REF_WORK_ORDER.getField(), id, WorkOrderItem.ENTITY_TYPE);
        if (!workorderItems.isEmpty()) {
            List<BrokerWorkOrderMaterialsSummaryOutputDto> output = new ArrayList<>(workorderItems.size());
            for (WorkOrderItem workOrderItem : workorderItems) {
                BrokerWorkOrderMaterialsSummaryOutputDto material = modelMapper.map(workOrderItem, BrokerWorkOrderMaterialsSummaryOutputDto.class);
                BrokerWarehouseMaterialSummaryOutputDto materialBroker = materialService.get(material.getMaterialId());
                material.setType(materialBroker.getType());
                material.setUnit(materialBroker.getUnit());
                output.add(material);
            }
            return output;
        }
        return Collections.emptyList();
    }

    /**
     * Method that creates an work order if the order does not exists already (the
     * check is based on erp order id). If the order already exists, it is performed
     * an update (if the order did not started yet)
     *
     * @param input
     * @return
     */
    public BrokerWorkOrderIntegrationOutputDto integrate(BrokerWorkOrderInputDto input) {
        BrokerWorkOrderIntegrationOutputDto result = new BrokerWorkOrderIntegrationOutputDto();
        if (Strings.isEmpty(input.getErpId())) {
            result.setOrionId(create(input).getOrionId());
            return result;
        }

        List<WorkOrder> workOrder = retrieveByExternalId(input.getErpId());
        if (workOrder == null || workOrder.isEmpty()) {
            result.setOrionId(create(input).getOrionId());
            return result;
        }

        if (workOrder.get(0).getStatus() != null && !WorkOrderStatus.SCHEDULED
                .equals(WorkOrderStatus.getWorkOrderStatus(workOrder.get(0).getStatus().getValue()))) {
            throw new WorkOrderUpdateNotAllowedException(workOrder.get(0).getStatus().getValue());
        }

        update(workOrder.get(0).getId(), input);
        result.setOrionId(workOrder.get(0).getId());
        if (workOrder.get(0).getScheduleIds() != null) {
            result.setScheduleIds(workOrder.get(0).getScheduleIds().getValue());
        }
        return result;
    }

    public BrokerWorkOrderSummaryOutputDto getByExternalId(String id) {
        List<WorkOrder> workOrders = retrieveByExternalId(id);
        if (workOrders == null || workOrders.isEmpty()) {
            throw new WorkOrderNotFoundException();
        }
        return get(workOrders.get(0).getId());
    }

    private List<WorkOrder> retrieveByExternalId(String id) {
        return reader.readOrionEntityQueryListRelationship(WorkOrderField.ERP_EXTERNAL_ID.getField(),
                new StringBuilder(Constant.ID).append(id).toString(), WorkOrder.ENTITY_TYPE);
    }
}
