package com.dalma.common.robot.enums;

public enum RobotOutputStatus {
	IDLE, WORKING, CHARGING;

	public static RobotOutputStatus getRobotOutputStatusByStatus(String status) {
		RobotStatus robotStatus = RobotStatus.getRobotStatus(status);
		if (status == null) {
			return null;
		}

		switch (robotStatus) {
		case IDLE:
			return RobotOutputStatus.IDLE;

		case CHARGING:
			return RobotOutputStatus.CHARGING;

		case MOVING:
		case READY:
		case STOPPED:
		case EMERGENCY_STOPPED:
		default:
			return RobotOutputStatus.WORKING;
		}
	}
}
