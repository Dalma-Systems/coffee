package com.dalma.contract.dto.base;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BaseLocationInputDto {
    @NotBlank
    private Double latitude;

    @NotBlank
    private Double longitude;

    @NotBlank
    private Double angle;
}
