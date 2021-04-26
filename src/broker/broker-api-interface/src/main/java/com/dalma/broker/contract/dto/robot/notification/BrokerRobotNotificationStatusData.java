package com.dalma.broker.contract.dto.robot.notification;

import com.dalma.broker.contract.dto.base.BaseInputDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerRobotNotificationStatusData extends BaseInputDto {
	private String type;
	private String value;
}
