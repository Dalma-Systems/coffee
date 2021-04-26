package com.dalma.contract.dto.robot.notification;

import com.dalma.contract.dto.base.BaseLocationInputDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RobotNotificationLocationDto extends BaseLocationInputDto {

    public RobotNotificationLocationDto(Double latitude, Double longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
}
