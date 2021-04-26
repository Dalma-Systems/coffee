package com.dalma.contract.dto.warehouse;

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
public class BrokerWarehouseSummaryOutputDto extends BaseOrionSummaryOutputDto {
    @NotBlank
    private BaseOrionAttributeOutputDto<String> erpId;

    @NotBlank
    private List<String> materials;
}
