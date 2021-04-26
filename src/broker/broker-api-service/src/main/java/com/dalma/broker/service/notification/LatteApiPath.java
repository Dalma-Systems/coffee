package com.dalma.broker.service.notification;

public enum LatteApiPath {
	ROBOT_STATUS_NOTIFICATION(LatteApiPath.ROBOT_NOTIF_URL + LatteApiPath.ID_MACRO + "/status"),
	ROBOT_BATTERY_NOTIFICATION(LatteApiPath.ROBOT_NOTIF_URL + LatteApiPath.ID_MACRO + "/battery"),
	ROBOT_LOCATION_NOTIFICATION(LatteApiPath.ROBOT_NOTIF_URL + LatteApiPath.ID_MACRO + "/location"),;

	private static final String ROBOT_NOTIF_URL = "/api/latte/notification/robot/";
	private static final String ID_MACRO = "{id}";
	private String path;

	private LatteApiPath(String path) {
		this.path = path;
	}

	public String getPath(String id) {
		return path.replace(ID_MACRO, id);
	}
}
