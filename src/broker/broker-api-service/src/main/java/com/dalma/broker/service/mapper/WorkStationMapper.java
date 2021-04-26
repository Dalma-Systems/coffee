package com.dalma.broker.service.mapper;

import com.dalma.broker.contract.dto.station.work.BrokerWorkStationInputDto;
import com.dalma.broker.fiware.orion.connector.entity.attribute.OrionBaseAttribute;
import com.dalma.broker.fiware.orion.connector.entity.robot.RobotDestinationRelationship;
import com.dalma.broker.fiware.orion.connector.entity.station.work.WorkStation;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class WorkStationMapper extends OrionBaseMapper {
    private WorkStationMapper() {
    }

    @Component
    public static class WorkstationInputDtoToWorkstation extends PropertyMap<BrokerWorkStationInputDto, WorkStation> {

        @Override
        protected void configure() {
            using(idToOrionId).map(source).setId(null);
            using(coordinatesToOrionLocation).map(source).setLocation(null);
            using(stringToOrionString).map(source.getName()).setName(null);
            using(stringToOrionString).map(source.getStatus()).setStatus(null);
            using(stringToOrionIdString).map(source.getErpId()).setErpId(null);
        }
    }

    @Component
    public static class WorkstationToWorkstationSummaryOutputDto
            extends PropertyMap<WorkStation, BrokerWorkStationSummaryOutputDto> {

        @Override
        protected void configure() {
            using(strAttrToAttrOutput).map(source.getName()).setName(null);
            using(strAttrToAttrOutput).map(source.getStatus()).setStatus(null);
            using(strIdAttrToAttrOutput).map(source.getErpId()).setErpId(null);
            using(geoCoordinatesToAttrOutput).map(source.getLocation()).setCoordinates(null);
            using(doubleAttrToAttrOutput).map(source.getLocation().getMetadata().getAngle()).setAngle(null);
            using(dateToDateOutput).map(source.getDateCreated()).setDateCreated(null);
            using(dateToDateOutput).map(source.getDateModified()).setDateModified(null);
        }
    }

    @Component
    public static class WorkstationToRobotDestinationRelationship
            extends PropertyMap<WorkStation, RobotDestinationRelationship> {

        private final Converter<WorkStation, OrionBaseAttribute<String>> entityToRelationshipEntity = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return createOrionRelationship(context.getSource().getId());
        };

        @Override
        protected void configure() {
            using(entityToRelationshipEntity).map(source).setRefDestination(null);
        }
    }
}
