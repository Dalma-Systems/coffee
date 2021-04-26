package com.dalma.contract.dto.subscription;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrokerSubscriptionEntityInputDto {
    @NotBlank
    private String id;

    @NotBlank
    private String type;
}
