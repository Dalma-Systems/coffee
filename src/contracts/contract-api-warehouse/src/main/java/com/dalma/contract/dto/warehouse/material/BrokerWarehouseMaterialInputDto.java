package com.dalma.contract.dto.warehouse.material;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrokerWarehouseMaterialInputDto {
    @NotBlank
    private String material;

    private String batch;

    @NotBlank
    private Double quantity;

    @NotBlank
    private String unit;

    private String erpId;
}
