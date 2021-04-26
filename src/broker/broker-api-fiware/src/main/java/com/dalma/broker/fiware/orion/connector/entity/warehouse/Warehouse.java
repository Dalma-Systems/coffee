package com.dalma.broker.fiware.orion.connector.entity.warehouse;

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
public class Warehouse extends BaseOrionEntity implements OrionEntity, Serializable {

    private static final long serialVersionUID = 4193857777649244887L;
    public static final String ENTITY_TYPE = "Warehouse";

    private OrionLocationAttribute<LocationPoint> location;
    private OrionAttribute<String> name;
    private OrionAttribute<String> status;
    private OrionAttribute<String> erpId;

    @Override
    public String getEntityType() {
        return ENTITY_TYPE;
    }
}
