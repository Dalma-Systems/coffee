package com.dalma.contract.dto.work.order;

import com.dalma.contract.dto.work.order.item.BrokerWorkOrderItemInputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
public class BrokerWorkOrderInputDto {
    @NotBlank
    private String status;

    @NotBlank
    private String scheduledAt;

    @NotBlank
    private String workingStationId;

    @NotBlank
    private String warehouseId;

    @NotBlank
    private List<BrokerWorkOrderItemInputDto> materials;

    private String erpId;

    private String startedAt;

    private String endAt;

    private String action;

    private List<String> scheduleIds;
}
