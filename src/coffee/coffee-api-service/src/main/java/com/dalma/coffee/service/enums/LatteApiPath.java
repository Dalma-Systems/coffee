package com.dalma.coffee.service.enums;

import lombok.Getter;

@Getter
public enum LatteApiPath {
	WORK_ODER_SCHEDULE("/api/latte/workorder/schedule"),
	;
	
	private String path;

	private LatteApiPath(String path) {
		this.path = path;
	}
}
