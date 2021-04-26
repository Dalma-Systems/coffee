package com.dalma.broker.api.controller;

import com.dalma.broker.fiware.orion.connector.entity.work.order.WorkOrder;
import com.dalma.broker.service.WorkOrderService;
import com.dalma.broker.service.exception.workorder.WorkOrderUpdateNotSupportedException;
import com.dalma.broker.service.publisher.WorkOrderPublisher;
import com.dalma.broker.service.reader.WorkOrderReader;
import com.dalma.common.util.Constant;
import com.dalma.common.workorder.enums.WorkOrderStatus;
import com.dalma.contract.dto.work.order.BrokerWorkOrderInputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderIntegrationOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderOutputDto;
import com.dalma.contract.dto.work.order.BrokerWorkOrderSummaryOutputDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.BY_ID;
import static com.dalma.broker.contract.Paths.EXTERNAL;
import static com.dalma.broker.contract.Paths.FILTER;
import static com.dalma.broker.contract.Paths.INTEGRATE;
import static com.dalma.broker.contract.Paths.WORKORDER;
import static com.dalma.common.util.Constant.END;
import static com.dalma.common.util.Constant.START;

@Validated
@RestController
@RequestMapping(BASE_PATH + WORKORDER)
public class WorkOrderController extends
        BaseOrionCrudController<BrokerWorkOrderInputDto, BrokerWorkOrderOutputDto, BrokerWorkOrderSummaryOutputDto, WorkOrder, WorkOrderPublisher, WorkOrderReader, WorkOrderService> {
    private final WorkOrderService workorderService;

    public WorkOrderController(WorkOrderService workorderService) {
        super(workorderService);
        this.workorderService = workorderService;
    }

    @Override
    public BrokerWorkOrderOutputDto create(@RequestBody BrokerWorkOrderInputDto input) {
        if (Strings.isEmpty(input.getStatus())) {
            input.setStatus(WorkOrderStatus.SCHEDULED.getStatus());
        }
        return super.create(input);
    }

    @Override
    public void update(@PathVariable(Constant.ID) String id, @RequestBody BrokerWorkOrderInputDto input) {
        if (input.getMaterials() != null) {
            throw new WorkOrderUpdateNotSupportedException();
        }
        super.update(id, input);
    }

    @PostMapping(INTEGRATE)
    @ResponseStatus(HttpStatus.OK)
    public BrokerWorkOrderIntegrationOutputDto integrate(@RequestBody BrokerWorkOrderInputDto input) {
        if (Strings.isEmpty(input.getStatus())) {
            input.setStatus(WorkOrderStatus.SCHEDULED.getStatus());
        }
        return workorderService.integrate(input);
    }

    @GetMapping(FILTER)
    @ResponseStatus(HttpStatus.OK)
    public List<BrokerWorkOrderSummaryOutputDto> getAll(@RequestParam(START) String start, @RequestParam(END) String end) {
        return workorderService.getAll(start, end);
    }

    @GetMapping(EXTERNAL + BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public BrokerWorkOrderSummaryOutputDto getByExternalId(@PathVariable(Constant.ID) String id) {
        return workorderService.getByExternalId(id);
    }
}
