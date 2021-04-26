package com.dalma.broker.api.controller;

import com.dalma.broker.contract.dto.station.work.BrokerWorkStationInputDto;
import com.dalma.broker.contract.dto.station.work.BrokerWorkStationOutputDto;
import com.dalma.broker.fiware.orion.connector.entity.station.work.WorkStation;
import com.dalma.broker.service.WorkStationService;
import com.dalma.broker.service.publisher.WorkStationPublisher;
import com.dalma.broker.service.reader.WorkStationReader;
import com.dalma.contract.dto.station.work.BrokerWorkStationSummaryOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.BY_ID;
import static com.dalma.broker.contract.Paths.EXTERNAL;
import static com.dalma.broker.contract.Paths.STATION;
import static com.dalma.broker.contract.Paths.WORK_STATION;

@Validated
@RestController
@RequestMapping(BASE_PATH + STATION + WORK_STATION)
public class WorkStationController extends
        BaseOrionCrudController<BrokerWorkStationInputDto, BrokerWorkStationOutputDto, BrokerWorkStationSummaryOutputDto, WorkStation, WorkStationPublisher, WorkStationReader, WorkStationService> {
	private final WorkStationService workstationService;
	
    public WorkStationController(WorkStationService workstationService) {
        super(workstationService);
        this.workstationService = workstationService;
    }
    
    @GetMapping(EXTERNAL + BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public BrokerWorkStationSummaryOutputDto getByExternalId(@PathVariable("id") String id) {
        return workstationService.getByExternalId(id);
    }
}
