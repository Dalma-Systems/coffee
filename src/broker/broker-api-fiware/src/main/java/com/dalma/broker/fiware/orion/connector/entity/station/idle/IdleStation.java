package com.dalma.broker.fiware.orion.connector.entity.station.idle;

import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionAttribute;
import com.dalma.broker.fiware.orion.connector.entity.station.Station;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdleStation extends Station {

	private static final long serialVersionUID = 7847684059242372816L;
	public static final String ENTITY_TYPE = "Idlestation";

    private OrionAttribute<String> refRobot;

    @Override
    public String getEntityType() {
        return ENTITY_TYPE;
    }
}
