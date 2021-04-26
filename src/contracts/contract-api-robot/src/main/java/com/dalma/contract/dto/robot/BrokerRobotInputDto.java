package com.dalma.contract.dto.robot;

import com.dalma.contract.dto.base.BaseLocationInputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
public class BrokerRobotInputDto extends BaseLocationInputDto {
    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String status;

    @NotBlank
    @NotNull
    private Integer battery;

    @NotBlank
    @NotNull
    private String version;

    private Boolean available;

    private String destination;

    private List<String> payload;

    private String workOrderId;

    private String action;
}
