package com.dalma.coffee.service.mapper;

import com.dalma.coffee.erp.soap.client.gen.ZFTSPLNPLANO;
import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SapMapper {

	private SapMapper() {
		// Mapper class
	}

	@Component
	public static class ZFTSPLNPLANOToErpWorkOrder extends PropertyMap<ZFTSPLNPLANO, ErpWorkOrder> {

		private static final String HOUR_FORMAT = "HH:mm:ss";

		private static final Converter<ZFTSPLNPLANO, Double> quantityConverter = context -> {
			if (context.getSource() == null || context.getSource().getQUANTIDADE() == null) {
				return null;
			}

			return context.getSource().getQUANTIDADE().doubleValue();
		};
		
		private static final Converter<ZFTSPLNPLANO, String> hourConverter = context -> {
			if (context.getSource() == null || context.getSource().getHORA() == null) {
				return null;
			}

			Date time = context.getSource().getHORA().toGregorianCalendar().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat(HOUR_FORMAT);
			return sdf.format(time);
		};
		
		@Override
		protected void configure() {
			map(source.getLOTE(), destination.getBatch());
			map(source.getDIA(), destination.getDay());
			map(source.getTXTMATERIAL(), destination.getMaterialDesc());
			map(source.getMATERIAL(), destination.getMaterialId());
			map(source.getORDEM(), destination.getOrderId());
			map(source.getUNIDADE(), destination.getUnit());
			map(source.getDEPOSITO(), destination.getWarehouseId());
			map(source.getESTACAO(), destination.getWorkStationId());
			using(quantityConverter).map(source).setQuantity(null);
			using(hourConverter).map(source).setHour(null);
		}
	}
}
