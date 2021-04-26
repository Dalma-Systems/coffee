package com.dalma.coffee.service.mapper;

import com.dalma.coffee.erp.api.ErpWorkOrder;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ErpMapper {
	
    private ErpMapper() {
    	// Mapper class
    }
    
    @Component
    public static class ErpWorkOrderToBrokerWarehouseMaterialInputDto extends PropertyMap<ErpWorkOrder, BrokerWarehouseMaterialInputDto> {

    	 @Override
         protected void configure() {
    		 map(source.getMaterialDesc(), destination.getMaterial());
    		 map(source.getMaterialId(), destination.getErpId());
         }
    }
    
    @Component
    public static class ErpWorkOrderToBrokerWorkOrderInputDto extends PropertyMap<ErpWorkOrder, BrokerWorkOrderInputDto> {

        public static final Converter<ErpWorkOrder, String> dayAndHourToScheduleDate = context -> {
            if (context.getSource() == null) {
                return null;
            }

            // This will be changed (for sure) when we have the final format from SAP.
            return new StringBuilder(context.getSource().getDay())
            		.append("T")
            		.append(context.getSource().getHour())
            		.toString();
        };

        @Override
        protected void configure() {
        	map(source.getOrderId(), destination.getErpId());
        	map(source.getWorkStationId(), destination.getWorkingStationId());
        	using(dayAndHourToScheduleDate).map(source).setScheduledAt(null);
        }
    }
}
