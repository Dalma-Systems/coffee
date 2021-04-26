package com.dalma.broker.service;

import com.dalma.broker.contract.dto.robot.notification.BrokerRobotNotificationInputDto;
import com.dalma.broker.fiware.orion.connector.entity.robot.Robot;
import com.dalma.broker.fiware.orion.connector.entity.station.idle.IdleStation;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.material.WarehouseMaterial;
import com.dalma.broker.service.notification.RobotNotificationHandler;
import com.dalma.broker.service.publisher.RobotPublisher;
import com.dalma.broker.service.reader.IdleStationReader;
import com.dalma.broker.service.reader.RobotReader;
import com.dalma.broker.service.reader.WarehouseMaterialReader;
import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.dalma.contract.dto.robot.BrokerRobotInputDto;
import com.dalma.contract.dto.robot.BrokerRobotOutputDto;
import com.dalma.contract.dto.robot.BrokerRobotSummaryOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RobotService extends
        BaseOrionCrudService<BrokerRobotInputDto, BrokerRobotOutputDto, BrokerRobotSummaryOutputDto, Robot, RobotPublisher, RobotReader> {
    private static final String REF_ROBOT = "refRobot";
    private final RobotPublisher publisher;
    private final RobotReader reader;
    private final RobotNotificationHandler notificationHandler;
    private final WarehouseMaterialReader materialReader;
    private final IdleStationReader idleStationReader;
    private final WorkOrderService workOrderService;

    public RobotService(RobotPublisher publisher, RobotReader reader, RobotNotificationHandler notificationHandler,
                        WarehouseMaterialReader materialReader, IdleStationReader idleStationReader,
                        WorkOrderService workOrderService) {
        super(publisher, reader);
        this.reader = reader;
        this.publisher = publisher;
        this.notificationHandler = notificationHandler;
        this.materialReader = materialReader;
        this.idleStationReader = idleStationReader;
        this.workOrderService = workOrderService;
    }

    @Override
    protected String getType() {
        return Robot.ENTITY_TYPE;
    }

    @Override
    protected BrokerRobotOutputDto mapOrionEntityToEntity(Robot orionEntity) {
        return new BrokerRobotOutputDto(orionEntity.getId(), orionEntity.getEntityType());
    }

    @Override
    public List<BrokerRobotSummaryOutputDto> getAll() {
        List<BrokerRobotSummaryOutputDto> robots = super.getAll();
        robots.stream().forEach(this::retrieveIdleStation);
        return robots;
    }

    @Override
    public BrokerRobotSummaryOutputDto get(String id) {
        BrokerRobotSummaryOutputDto orionEntity = reader.readObject(id);

        List<WarehouseMaterial> workorders = materialReader.readOrionEntityQueryListRelationship(REF_ROBOT,
                orionEntity.getId(), WarehouseMaterial.ENTITY_TYPE);
        if (!workorders.isEmpty()) {
            orionEntity.setMaterials(workorders.stream().map(WarehouseMaterial::getId).collect(Collectors.toList()));
        }

        retrieveIdleStation(orionEntity);

        return orionEntity;
    }

    private void retrieveIdleStation(BrokerRobotSummaryOutputDto orionEntity) {
        List<IdleStation> idleStations = idleStationReader.readOrionEntityQueryListRelationship(REF_ROBOT,
                orionEntity.getId(), IdleStation.ENTITY_TYPE);
        if (!idleStations.isEmpty()) {
            BaseOrionAttributeOutputDto<String> idleStation = new BaseOrionAttributeOutputDto<>();
            idleStation.setValue(idleStations.get(0).getId());
            orionEntity.setIdleStation(idleStation);
        }
    }

    @Override
    public void update(String id, BrokerRobotInputDto entity) {
        super.update(id, entity);

        // Associate destination (warehouse / workstation) and materials
        if (!Strings.isEmpty(entity.getDestination()) || entity.getPayload() != null && !entity.getPayload().isEmpty()
                || !Strings.isEmpty(entity.getWorkOrderId())) {
            List<String> relatedIds = new LinkedList<>();

            if (!Strings.isEmpty(entity.getDestination())) {
                relatedIds.add(entity.getDestination());
            } else {
                relatedIds.add(Strings.EMPTY);
            }

            if (!Strings.isEmpty(entity.getWorkOrderId())) {
                relatedIds.add(entity.getWorkOrderId());
            } else {
                relatedIds.add(Strings.EMPTY);
            }

            if (entity.getPayload() != null && !entity.getPayload().isEmpty()) {
                relatedIds.addAll(entity.getPayload());
            }

            publisher.append(reader.readOrionEntity(id), relatedIds);
        }

        if (!Strings.isEmpty(entity.getAction())) {
            updateWorkOrderAction(id, entity);
        }
    }

    private void updateWorkOrderAction(String id, BrokerRobotInputDto entity) {
        BrokerRobotSummaryOutputDto robot = get(id);
        if (robot.getWorkOrder() != null && !Strings.isEmpty(robot.getWorkOrder().getValue())) {
            BrokerWorkOrderInputDto orderInput = new BrokerWorkOrderInputDto();
            orderInput.setAction(entity.getAction());
            workOrderService.update(robot.getWorkOrder().getValue(), orderInput);
        }
    }

    public void notificationStatus(BrokerRobotNotificationInputDto status) {
        notificationHandler.handleNotification(status);
    }
}
