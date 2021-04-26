package com.dalma.broker.api.controller;

import com.dalma.broker.contract.dto.warehouse.material.BrokerWarehouseMaterialSummaryOutputDto;
import com.dalma.broker.fiware.orion.connector.entity.warehouse.material.WarehouseMaterial;
import com.dalma.broker.service.WarehouseMaterialService;
import com.dalma.broker.service.publisher.WarehouseMaterialPublisher;
import com.dalma.broker.service.reader.WarehouseMaterialReader;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialInputDto;
import com.dalma.contract.dto.warehouse.material.BrokerWarehouseMaterialOutputDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.MATERIAL;
import static com.dalma.broker.contract.Paths.WAREHOUSE;

@Validated
@RestController
@RequestMapping(BASE_PATH + WAREHOUSE + MATERIAL)
public class WarehouseMaterialController extends
        BaseOrionCrudController<BrokerWarehouseMaterialInputDto, BrokerWarehouseMaterialOutputDto, BrokerWarehouseMaterialSummaryOutputDto, WarehouseMaterial, WarehouseMaterialPublisher, WarehouseMaterialReader, WarehouseMaterialService> {

    public WarehouseMaterialController(WarehouseMaterialService warehouseService) {
        super(warehouseService);
    }
}
