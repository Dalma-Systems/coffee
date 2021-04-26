package com.dalma.broker.contract.dto.robot.notification;

import com.dalma.broker.contract.dto.base.BaseInputDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerRobotNotificationBatteryData extends BaseInputDto {
	private String type;
	private Integer value;
}
