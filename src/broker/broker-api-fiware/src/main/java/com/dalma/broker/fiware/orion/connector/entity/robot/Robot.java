package com.dalma.broker.fiware.orion.connector.entity.robot;

import com.dalma.broker.fiware.orion.connector.entity.BaseOrionEntity;
import com.dalma.broker.fiware.orion.connector.entity.OrionEntity;
import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionAttribute;
import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionLocationAttribute;
import com.dalma.broker.fiware.orion.connector.entity.common.LocationPoint;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Robot extends BaseOrionEntity implements OrionEntity, Serializable {

    private static final long serialVersionUID = 4500161370016162121L;
    public static final String ENTITY_TYPE = "AMR";

    private OrionLocationAttribute<LocationPoint> location;
    private OrionAttribute<String> name;
    private OrionAttribute<String> status;
    private OrionAttribute<Integer> battery;
    private OrionAttribute<Boolean> available;
    private OrionAttribute<String> action;
    private OrionAttribute<String> version;

    private OrionAttribute<String> refDestination;
    private OrionAttribute<String> refWorkOrder;

    @Override
    public String getEntityType() {
        return ENTITY_TYPE;
    }
}
