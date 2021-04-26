package com.dalma.broker.service.publisher;

import com.dalma.broker.contract.dto.warehouse.BrokerWarehouseInputDto;
import com.dalma.broker.fiware.orion.connector.OrionConnectorConfiguration;
import com.dalma.broker.fiware.orion.connector.entity.OrionRelationship;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.Warehouse;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.WarehouseOrionPublisher;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.material.WarehouseMaterial;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.material.WarehouseMaterialRelationship;
import com.dalma.broker.service.mapper.OrionBaseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WarehousePublisher extends WarehouseOrionPublisher<BrokerWarehouseInputDto> {

    private final ModelMapper modelMapper;

    public WarehousePublisher(OrionConnectorConfiguration config, ModelMapper modelMapper) {
        super(config);
        this.modelMapper = modelMapper;
    }

    @Override
    public Warehouse mapEntityToOrionEntity(BrokerWarehouseInputDto entity) {
        return modelMapper.map(entity, Warehouse.class);
    }

    @Override
    public OrionRelationship<WarehouseMaterialRelationship> mapEntityToOrionRelationshipEntity(Warehouse orionEntity,
                                                                                               List<String> relatedIds) {
        OrionRelationship<WarehouseMaterialRelationship> relationship = new OrionRelationship<>();
        List<WarehouseMaterialRelationship> materials = new ArrayList<>(relatedIds.size());
        for (String relatedId : relatedIds) {
            WarehouseMaterialRelationship material = modelMapper.map(orionEntity, WarehouseMaterialRelationship.class);
            material.setId(relatedId);
            material.setType(WarehouseMaterial.ENTITY_TYPE);
            materials.add(material);
        }
        relationship.setEntities(materials);
        return relationship;
    }

    @Override
    protected Warehouse mapEntityIdAndDate(Warehouse mapEntityToOrionEntity) {
        mapEntityToOrionEntity
                .setId(mapEntityToOrionEntity.getId().replace(OrionBaseMapper.ORION_TYPE_MACRO, Warehouse.ENTITY_TYPE));
        return mapEntityToOrionEntity;
    }
}
