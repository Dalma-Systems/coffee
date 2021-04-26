package com.dalma.contract.dto.station;

import com.dalma.contract.dto.base.BaseLocationInputDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BrokerStationInputDto extends BaseLocationInputDto {
    @NotBlank
    private String name;

    @NotBlank
    private String status;

    private String robotId;
}
