package com.dalma.broker.contract.dto.robot.notification;

import com.dalma.broker.contract.dto.base.BaseInputDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerRobotNotificationData extends BaseInputDto {
	private String id;
	private String type;
	private BrokerRobotNotificationBatteryData battery;
	private BrokerRobotNotificationStatusData status;
	private BrokerRobotNotificationLocationData location;
}
