package com.dalma.contract.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseOrionSummaryOutputDto {
    @NotBlank
    private String id;

    @NotBlank
    private String type;

    @NotBlank
    private BaseOrionAttributeOutputDto<Double[]> coordinates;

    @NotBlank
    private BaseOrionAttributeOutputDto<Double> angle;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> name;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> status;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> dateCreated;

    @NotBlank
    private BaseOrionAttributeOutputDto<String> dateModified;
}
