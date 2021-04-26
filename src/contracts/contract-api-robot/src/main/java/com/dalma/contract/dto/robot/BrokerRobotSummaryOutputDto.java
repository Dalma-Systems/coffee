package com.dalma.contract.dto.robot;

import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.dalma.contract.dto.base.BaseOrionSummaryOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrokerRobotSummaryOutputDto extends BaseOrionSummaryOutputDto {
    @NotBlank
    private BaseOrionAttributeOutputDto<Boolean> available;

    @NotBlank
    private BaseOrionAttributeOutputDto<Integer> battery;

    private List<String> materials;

    private BaseOrionAttributeOutputDto<String> destination;

    private BaseOrionAttributeOutputDto<String> workOrder;

    private BaseOrionAttributeOutputDto<String> action;

    private BaseOrionAttributeOutputDto<String> idleStation;

    private BaseOrionAttributeOutputDto<String> version;
}
