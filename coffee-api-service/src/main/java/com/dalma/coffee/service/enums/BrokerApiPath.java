package com.dalma.coffee.service.enums;

import lombok.Getter;

@Getter
public enum BrokerApiPath {
	WAREHOUSE("/api/broker/warehouse"),
	MATERIAL("/material"),
	EXTERNAL("/external"),
	WORK_ORDER("/api/broker/workorder"),
	WORK_STATION("/api/broker/station/work"),
	INTEGRATE("/integrate"),
	NOTIFICATION("/notification"),
	;
	
	private String path;

	private BrokerApiPath(String path) {
		this.path = path;
	}
}
