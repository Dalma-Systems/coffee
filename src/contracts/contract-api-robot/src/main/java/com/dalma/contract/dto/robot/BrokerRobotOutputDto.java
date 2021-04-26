package com.dalma.contract.dto.robot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerRobotOutputDto {

    @NotNull
    private String orionId;

    @NotNull
    private String type;
}
