package com.dalma.contract.dto.station.work;

import com.dalma.contract.dto.base.BaseOrionAttributeOutputDto;
import com.dalma.contract.dto.station.BrokerStationSummaryOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrokerWorkStationSummaryOutputDto extends BrokerStationSummaryOutputDto {
    @NotBlank
    private BaseOrionAttributeOutputDto<String> erpId;

}
