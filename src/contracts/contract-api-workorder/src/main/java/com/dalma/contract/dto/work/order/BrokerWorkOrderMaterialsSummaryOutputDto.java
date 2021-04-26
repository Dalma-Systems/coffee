package com.dalma.contract.dto.work.order;

import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrokerWorkOrderMaterialsSummaryOutputDto {
    @NotBlank
    private String materialId;

    @NotBlank
    private BaseOrionAttributeOutputDto<Double> quantity;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> type;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> unit;
}
