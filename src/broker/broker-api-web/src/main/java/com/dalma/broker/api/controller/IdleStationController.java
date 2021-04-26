package com.dalma.broker.api.controller;

import com.dalma.broker.contract.dto.station.idle.BrokerIdleStationInputDto;
import com.dalma.broker.contract.dto.station.idle.BrokerIdleStationOutputDto;
import com.dalma.broker.contract.dto.station.idle.BrokerIdleStationSummaryOutputDto;
import com.dalma.broker.fiware.orion.connector.entity.station.idle.IdleStation;
import com.dalma.broker.service.IdleStationService;
import com.dalma.broker.service.publisher.IdleStationPublisher;
import com.dalma.broker.service.reader.IdleStationReader;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.IDLE_STATION;
import static com.dalma.broker.contract.Paths.STATION;

@Validated
@RestController
@RequestMapping(BASE_PATH + STATION + IDLE_STATION)
public class IdleStationController extends
        BaseOrionCrudController<BrokerIdleStationInputDto, BrokerIdleStationOutputDto, BrokerIdleStationSummaryOutputDto, IdleStation, IdleStationPublisher, IdleStationReader, IdleStationService> {

    public IdleStationController(IdleStationService idleStationService) {
        super(idleStationService);
    }
}
