package com.dalma.broker.contract.dto.subscription;

import com.dalma.broker.contract.dto.base.BaseOutputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerSubscriptionOutputDto extends BaseOutputDto {

}
