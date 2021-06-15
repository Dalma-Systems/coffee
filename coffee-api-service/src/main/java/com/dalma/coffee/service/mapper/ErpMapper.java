package com.dalma.coffee.service.mapper;

import com.dalma.common.util.Constant;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ErpMapper {

	public static final String DATE_TIME_SEPARATOR = "T";

	private ErpMapper() {
		// Mapper class
	}

	@Component
	public static class ErpWorkOrderToBrokerWarehouseMaterialInputDto
			extends PropertyMap<ErpWorkOrder, BrokerWarehouseMaterialInputDto> {

		@Override
		protected void configure() {
			map(source.getMaterialDesc(), destination.getMaterial());
			map(source.getMaterialId(), destination.getErpId());
		}
	}

	@Component
	public static class ErpWorkOrderToBrokerWorkOrderInputDto
			extends PropertyMap<ErpWorkOrder, BrokerWorkOrderInputDto> {

		public static final Converter<ErpWorkOrder, String> dayAndHourToScheduleDate = context -> {
			if (context.getSource() == null) {
				return null;
			}

			try {
				DateFormat formatter = new SimpleDateFormat(Constant.SCHEDULE_FORMAT);
				Date date = formatter.parse(new StringBuilder(context.getSource().getDay()).append(DATE_TIME_SEPARATOR).append(context.getSource().getHour()).toString());
				return formatter.format(date);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return null;
			}
		};

		@Override
		protected void configure() {
			map(source.getOrderId(), destination.getErpId());
			map(source.getWorkStationId(), destination.getWorkingStationId());
			using(dayAndHourToScheduleDate).map(source).setScheduledAt(null);
		}
	}
}
