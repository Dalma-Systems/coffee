package com.dalma.coffee.contract.work.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IntegrateWorkOrderOutputDto {
	private List<String> workOrderIds = new ArrayList<>();
	private List<String> errors = new ArrayList<>();
}
