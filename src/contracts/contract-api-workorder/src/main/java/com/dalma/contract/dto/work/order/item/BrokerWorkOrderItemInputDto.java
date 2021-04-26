package com.dalma.contract.dto.work.order.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerWorkOrderItemInputDto {
    @NotBlank
    private String id;

    @NotBlank
    private Double quantity;
}
