package com.dalma.contract.dto.subscription;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
public class BrokerSubscriptionInputDto {
    @NotBlank
    private List<BrokerSubscriptionEntityInputDto> entities;

    @NotBlank
    private List<String> attributes;

    @NotBlank
    private String notificationUrl;

    @NotBlank
    private List<String> notificationAttributes;

    private String description;
    private String expirationDate;
    private Integer throttling;
}
