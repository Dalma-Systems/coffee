package com.dalma.contract.dto.work.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class BrokerWorkOrderIntegrationOutputDto extends BrokerWorkOrderOutputDto {

    public BrokerWorkOrderIntegrationOutputDto(String orionId, List<String> scheduleIds) {
        super.setOrionId(orionId);
        this.scheduleIds = scheduleIds;
    }

    @NotBlank
    private List<String> scheduleIds;
}
