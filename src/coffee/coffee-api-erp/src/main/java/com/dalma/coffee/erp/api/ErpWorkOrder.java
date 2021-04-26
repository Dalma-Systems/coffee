package com.dalma.coffee.erp.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErpWorkOrder {
	
	private String orderId;
	
	private String materialId;

	private String materialDesc;
	
	private Double quantity;
	
	private String unit;

	private String batch;
	
	private String workStationId;
	
	private String warehouseId;
	
	private String day;
	
	private String hour;
	
	@Override
	public String toString() {
		return new StringBuilder("ErpWorkOrder: order=").append(orderId)
				.append(", material=").append(materialId)
				.append(", batch=").append(batch)
				.append(", materialDesc=").append(materialDesc)
				.append(", quantity=").append(quantity)
				.append(", unit=").append(unit)
				.append(", workStation=").append(workStationId)
				.append(", warehouse=").append(warehouseId)
				.append(", day=").append(day)
				.append(", hour=").append(hour)
				.toString();
	}
}
