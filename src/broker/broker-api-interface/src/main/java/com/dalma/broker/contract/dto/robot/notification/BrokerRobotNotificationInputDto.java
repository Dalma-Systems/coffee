package com.dalma.broker.contract.dto.robot.notification;

import com.dalma.broker.contract.dto.base.BaseInputDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BrokerRobotNotificationInputDto extends BaseInputDto {
	private String subscriptionId;
	private List<BrokerRobotNotificationData> data;
}
