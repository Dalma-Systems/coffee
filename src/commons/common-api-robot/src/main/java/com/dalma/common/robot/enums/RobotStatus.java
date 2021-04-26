package com.dalma.common.robot.enums;

import lombok.Getter;

@Getter
public enum RobotStatus {
	IDLE("idle"), // identify that robot has nothing to do and is in position zero
	MOVING("moving"), // identify that robot is moving to destination (warehouse/working station)
	STOPPED("stopped"), // identify that robot is stopped in destination waiting for manual trigger
	READY("ready"), // identify that robot is in destination ready to go to next destination
	EMERGENCY_STOPPED("emergencyStopped"), // identify that robot was manually stopped
	CHARGING("charging"), // robot does not knows how to communicate this state yet
	;

	private String status;

	private RobotStatus(String status) {
		this.status = status;
	}

	public static RobotStatus getRobotStatus(String status) {
		if (status == null) {
			return null;
		}

		for (RobotStatus robotStatus : values()) {
			if (robotStatus.getStatus().equals(status)) {
				return robotStatus;
			}
		}
		return null;
	}
}
