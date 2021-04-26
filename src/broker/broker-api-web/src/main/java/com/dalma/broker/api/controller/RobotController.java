package com.dalma.broker.api.controller;

import com.dalma.broker.contract.dto.robot.notification.BrokerRobotNotificationInputDto;
import com.dalma.broker.fiware.orion.connector.entity.robot.Robot;
import com.dalma.broker.service.RobotService;
import com.dalma.broker.service.publisher.RobotPublisher;
import com.dalma.broker.service.reader.RobotReader;
import com.dalma.contract.dto.robot.BrokerRobotInputDto;
import com.dalma.contract.dto.robot.BrokerRobotOutputDto;
import com.dalma.contract.dto.robot.BrokerRobotSummaryOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.NOTIFICATION;
import static com.dalma.broker.contract.Paths.ROBOT;
import static com.dalma.broker.contract.Paths.STATUS;

@Validated
@RestController
@RequestMapping(BASE_PATH + ROBOT)
public class RobotController extends
        BaseOrionCrudController<BrokerRobotInputDto, BrokerRobotOutputDto, BrokerRobotSummaryOutputDto, Robot, RobotPublisher, RobotReader, RobotService> {

    private final RobotService robotService;

    public RobotController(RobotService robotService) {
        super(robotService);
        this.robotService = robotService;
    }

    @PostMapping(NOTIFICATION + STATUS)
    @ResponseStatus(HttpStatus.OK)
    public void notificationStatus(@RequestBody BrokerRobotNotificationInputDto status) {
        robotService.notificationStatus(status);
    }
}
