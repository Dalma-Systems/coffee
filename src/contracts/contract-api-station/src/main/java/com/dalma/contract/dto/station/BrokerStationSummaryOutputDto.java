package com.dalma.contract.dto.station;

import com.dalma.contract.dto.base.BaseOrionSummaryOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrokerStationSummaryOutputDto extends BaseOrionSummaryOutputDto {
    private String workorderId;
}
