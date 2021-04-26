package com.dalma.coffee.erp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "ZFTS_PLANResponse")
@Getter
public class SapWorkOrder {
	@JsonProperty("EX_ERRO")
	private String error;

	@JsonProperty("TBL_PLANODIARIO")
	private Planning planning;

	@Getter
	public static class Planning {
		@JacksonXmlElementWrapper(useWrapping = false)
		@JsonProperty("item")
		private List<WorkOrder> item = new ArrayList<>();

		@Getter
		public static class WorkOrder {
			@JsonProperty("AGRUPADOR")
			private String group;

			@JsonProperty("DEPOSITO")
			private String warehouseId;

			@JsonProperty("TXTDEPOSITO")
			private String warehouseDesc;

			@JsonProperty("ESTACAO")
			private String workStationId;

			@JsonProperty("TXTESTACAO")
			private String workStationDesc;

			@JsonProperty("ORDEM")
			private String orderId;

			@JsonProperty("MATERIAL")
			private String materialId;

			@JsonProperty("TXTMATERIAL")
			private String materialDesc;

			@JsonProperty("LOTE")
			private String batch;

			@JsonProperty("DIA")
			private String day;

			@JsonProperty("HORA")
			private String hour;

			@JsonProperty("QUANTIDADE")
			private String quantity;

			@JsonProperty("UNIDADE")
			private String unit;

			@JsonProperty("ESTADO")
			private String status;

			@Override
			public String toString() {
				return new StringBuilder("Group=").append(group).append(", WarehouseId=").append(warehouseId)
						.append(", WarehouseDesc=").append(warehouseDesc).append(", WorkStationId=")
						.append(workStationId).append(", WorkStationDesc=").append(workStationDesc).append(", OrderId=")
						.append(orderId).append(", MaterialId=").append(materialId).append(", MaterialDesc=")
						.append(materialDesc).append(", Batch=").append(batch).append(", Day=").append(day)
						.append(", Hour=").append(hour).append(", Quantity=").append(quantity).append(", Unity=")
						.append(unit).append(", Status=").append(status).toString();
			}
		}

		@Override
		public String toString() {
			StringBuilder items = new StringBuilder();
			if (item != null) {
				for (WorkOrder workOrder : item) {
					items.append("[");
					items.append(workOrder.toString());
					items.append("] ");
				}
			}
			return items.toString();
		}
	}

	@Override
	public String toString() {
		StringBuilder orders = new StringBuilder("SAP_WORK_ORDER Status=").append(error).append(", Planning=");
		if (planning != null) {
			orders.append(planning.toString());
		}
		return orders.toString();
	}
}
