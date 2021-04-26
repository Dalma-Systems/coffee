package com.dalma.broker.service.mapper;

import com.dalma.broker.fiware.orion.connector.entity.robot.Robot;
import com.dalma.contract.dto.robot.BrokerRobotInputDto;
import com.dalma.contract.dto.robot.BrokerRobotSummaryOutputDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class RobotMapper extends OrionBaseMapper {
    private RobotMapper() {
        // Mapping class
    }

    @Component
    public static class RobotInputDtoToRobot extends PropertyMap<BrokerRobotInputDto, Robot> {

        @Override
        protected void configure() {
            using(idToOrionId).map(source).setId(null);
            using(coordinatesToOrionLocation).map(source).setLocation(null);
            using(stringToOrionString).map(source.getName()).setName(null);
            using(stringToOrionString).map(source.getStatus()).setStatus(null);
            using(integerToOrionInteger).map(source.getBattery()).setBattery(null);
            using(booleanToOrionBoolean).map(source.getAvailable()).setAvailable(null);
            using(stringToOrionString).map(source.getAction()).setAction(null);
            using(stringToOrionString).map(source.getVersion()).setVersion(null);
        }
    }

    @Component
    public static class RobotToRobotSummaryOutputDto extends PropertyMap<Robot, BrokerRobotSummaryOutputDto> {

        @Override
        protected void configure() {
            using(strAttrToAttrOutput).map(source.getName()).setName(null);
            using(strAttrToAttrOutput).map(source.getStatus()).setStatus(null);
            using(intAttrToAttrOutput).map(source.getBattery()).setBattery(null);
            using(geoCoordinatesToAttrOutput).map(source.getLocation()).setCoordinates(null);
            using(doubleAttrToAttrOutput).map(source.getLocation().getMetadata().getAngle()).setAngle(null);
            using(dateToDateOutput).map(source.getDateCreated()).setDateCreated(null);
            using(dateToDateOutput).map(source.getDateModified()).setDateModified(null);
            using(booleanAttrToAttrOutput).map(source.getAvailable()).setAvailable(null);
            using(strAttrToAttrOutput).map(source.getRefWorkOrder()).setWorkOrder(null);
            using(strAttrToAttrOutput).map(source.getRefDestination()).setDestination(null);
            using(strAttrToAttrOutput).map(source.getAction()).setAction(null);
            using(strAttrToAttrOutput).map(source.getVersion()).setVersion(null);
        }
    }
}
