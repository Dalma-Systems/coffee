package com.dalma.contract.dto.robot.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RobotNotificationBatteryDto {
	@NotBlank
	private Integer battery;
}
