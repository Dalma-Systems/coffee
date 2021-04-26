package com.dalma.broker.service.publisher;

import com.dalma.broker.fiware.orion.connector.OrionConnectorConfiguration;
import com.dalma.broker.fiware.orion.connector.entity.OrionRelationship;
import com.dalma.broker.fiware.orion.connector.entity.OrionRelationshipEntity;
import com.dalma.broker.fiware.orion.connector.entity.robot.Robot;
import com.dalma.broker.fiware.orion.connector.entity.robot.RobotDestinationRelationship;
import com.dalma.broker.fiware.orion.connector.entity.robot.RobotOrionPublisher;
import com.dalma.broker.fiware.orion.connector.entity.robot.RobotWarehouseMaterialRelationship;
import com.dalma.broker.fiware.orion.connector.entity.robot.RobotWorkOrderRelationship;
import com.dalma.broker.fiware.orion.connector.entity.station.work.WorkStation;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.Warehouse;
import com.dalma.broker.fiware.orion.connector.entity.work.order.WorkOrder;
import com.dalma.broker.service.mapper.OrionBaseMapper;
import com.dalma.broker.service.reader.WarehouseReader;
import com.dalma.broker.service.reader.WorkStationReader;
import com.dalma.contract.dto.robot.BrokerRobotInputDto;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RobotPublisher extends RobotOrionPublisher<BrokerRobotInputDto> {

	private final ModelMapper modelMapper;
	private final WarehouseReader warehouseReader;
	private final WorkStationReader workstationReader;

	private static final String WAREHOUSE_PREFIX_ID = OrionBaseMapper.ORION_ID_PREFIX + Warehouse.ENTITY_TYPE;
	private static final String WORKSTATION_PREFIX_ID = OrionBaseMapper.ORION_ID_PREFIX + WorkStation.ENTITY_TYPE;
	private static final String WORK_ORDER_PREFIX_ID = OrionBaseMapper.ORION_ID_PREFIX + WorkOrder.ENTITY_TYPE;

	public RobotPublisher(OrionConnectorConfiguration config, ModelMapper modelMapper, WarehouseReader warehouseReader,
			WorkStationReader workstationReader) {
		super(config);
		this.modelMapper = modelMapper;
		this.warehouseReader = warehouseReader;
		this.workstationReader = workstationReader;
	}

	@Override
	public Robot mapEntityToOrionEntity(BrokerRobotInputDto entity) {
		return modelMapper.map(entity, Robot.class);
	}

	@Override
	protected Robot mapEntityIdAndDate(Robot mapEntityToOrionEntity) {
		mapEntityToOrionEntity
				.setId(mapEntityToOrionEntity.getId().replace(OrionBaseMapper.ORION_TYPE_MACRO, Robot.ENTITY_TYPE));
		return mapEntityToOrionEntity;
	}

	@Override
	public OrionRelationship<OrionRelationshipEntity> mapEntityToOrionRelationshipEntity(Robot orionEntity,
			List<String> relatedIds) {
		OrionRelationship<OrionRelationshipEntity> relationship = new OrionRelationship<>();
		List<OrionRelationshipEntity> relationshipEntities = new LinkedList<>();

		// Destination is in first position
		if (!Strings.EMPTY.equals(relatedIds.get(0))) {
			if (relatedIds.get(0).startsWith(WAREHOUSE_PREFIX_ID)) {
				RobotDestinationRelationship warehouse = modelMapper
						.map(warehouseReader.readOrionEntity(relatedIds.get(0)), RobotDestinationRelationship.class);
				warehouse.setId(orionEntity.getId());
				warehouse.setType(Robot.ENTITY_TYPE);
				relationshipEntities.add(warehouse);
			} else if (relatedIds.get(0).startsWith(WORKSTATION_PREFIX_ID)) {
				RobotDestinationRelationship workstation = modelMapper
						.map(workstationReader.readOrionEntity(relatedIds.get(0)), RobotDestinationRelationship.class);
				workstation.setId(orionEntity.getId());
				workstation.setType(Robot.ENTITY_TYPE);
				relationshipEntities.add(workstation);
			} else {
				RobotDestinationRelationship positionZero = new RobotDestinationRelationship();
				positionZero.setRefDestination(OrionBaseMapper.createOrionRelationship(relatedIds.get(0)));
				positionZero.setId(orionEntity.getId());
				positionZero.setType(Robot.ENTITY_TYPE);
				relationshipEntities.add(positionZero);
			}
		}
		relatedIds.remove(0);

		// Work order is now the first position
		if (!Strings.EMPTY.equals(relatedIds.get(0))) {
			RobotWorkOrderRelationship workOrder = new RobotWorkOrderRelationship();
			if (relatedIds.get(0).startsWith(WORK_ORDER_PREFIX_ID)) {
				workOrder.setRefWorkOrder(OrionBaseMapper.createOrionRelationship(relatedIds.get(0)));
			} else {
				workOrder.setRefWorkOrder(OrionBaseMapper.createOrionRelationship(Strings.EMPTY));
			}
			workOrder.setId(orionEntity.getId());
			workOrder.setType(Robot.ENTITY_TYPE);
			relationshipEntities.add(workOrder);
		}
		relatedIds.remove(0);

		// Materials are in remaining positions
		RobotWarehouseMaterialRelationship material = new RobotWarehouseMaterialRelationship();
		material.setRefPayload(OrionBaseMapper.createOrionRelationshipArray(relatedIds.toArray(new String[0])));
		material.setId(orionEntity.getId());
		material.setType(Robot.ENTITY_TYPE);
		relationshipEntities.add(material);

		relationship.setEntities(relationshipEntities);
		return relationship;
	}
}
