package com.dalma.broker.fiware.orion.connector.entity.work.order;

import com.dalma.broker.fiware.orion.connector.entity.BaseOrionEntity;
import com.dalma.broker.fiware.orion.connector.entity.OrionEntity;
import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionAttribute;
import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionBaseAttribute;
import com.dalma.broker.fiware.orion.connector.entity.common.OrionDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class WorkOrder extends BaseOrionEntity implements OrionEntity, Serializable {

    private static final long serialVersionUID = 5437811258179074954L;
    public static final String ENTITY_TYPE = "WorkOrder";

    private OrionAttribute<String> status;
    private OrionAttribute<String> erpId;
    private OrionDateTime scheduledAt;
    private OrionDateTime startedAt;
    private OrionDateTime endedAt;
    private OrionAttribute<String> action;
    private OrionAttribute<ArrayList<String>> scheduleIds;

    private OrionBaseAttribute<String> refWorkstation;
    private OrionBaseAttribute<String> refWarehouse;

    @Override
    public String getEntityType() {
        return ENTITY_TYPE;
    }
}
