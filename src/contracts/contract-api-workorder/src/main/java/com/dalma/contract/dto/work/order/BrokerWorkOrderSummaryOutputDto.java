package com.dalma.contract.dto.work.order;

import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrokerWorkOrderSummaryOutputDto {
    @NotBlank
    private String id;

    @NotBlank
    private String type;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> startedAt;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> endedAt;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> scheduledAt;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> status;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> erpId;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> dateCreated;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> dateModified;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> workstationId;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> workstation;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> warehouseId;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> warehouse;

    @NotBlank
    private List<BrokerWorkOrderMaterialsSummaryOutputDto> materials;

    private BaseOrionAttributeOutputDto<String> robotId;

    private BaseOrionAttributeOutputDto<String> action;
}
