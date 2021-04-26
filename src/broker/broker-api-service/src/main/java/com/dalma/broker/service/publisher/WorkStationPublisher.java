package com.dalma.broker.service.publisher;

import com.dalma.broker.contract.dto.station.work.BrokerWorkStationInputDto;
import com.dalma.broker.fiware.orion.connector.OrionConnectorConfiguration;
import com.dalma.broker.fiware.orion.connector.entity.OrionRelationship;
import com.dalma.broker.fiware.orion.connector.entity.OrionRelationshipEntity;
import com.dalma.broker.fiware.orion.connector.entity.station.work.WorkStation;
import com.dalma.broker.fiware.orion.connector.entity.station.work.WorkStationOrionPublisher;
import com.dalma.broker.service.mapper.OrionBaseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkStationPublisher extends WorkStationOrionPublisher<BrokerWorkStationInputDto> {

    private final ModelMapper modelMapper;

    public WorkStationPublisher(OrionConnectorConfiguration config, ModelMapper modelMapper) {
        super(config);
        this.modelMapper = modelMapper;
    }

    @Override
    public WorkStation mapEntityToOrionEntity(BrokerWorkStationInputDto entity) {
        return modelMapper.map(entity, WorkStation.class);
    }

    @Override
    protected WorkStation mapEntityIdAndDate(WorkStation mapEntityToOrionEntity) {
        mapEntityToOrionEntity.setId(
                mapEntityToOrionEntity.getId().replace(OrionBaseMapper.ORION_TYPE_MACRO, WorkStation.ENTITY_TYPE));
        return mapEntityToOrionEntity;
    }

    @Override
    public OrionRelationship<? extends OrionRelationshipEntity> mapEntityToOrionRelationshipEntity(
            WorkStation orionEntity, List<String> relatedIds) {
        return null;
    }
}
