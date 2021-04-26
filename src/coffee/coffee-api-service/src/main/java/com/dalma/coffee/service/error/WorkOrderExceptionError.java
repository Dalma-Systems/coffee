package com.dalma.coffee.service.error;

import com.dalma.coffee.base.error.BaseExceptionError;

import static com.dalma.coffee.service.error.BaseExceptionError.WORK_ORDER_ERROR_CODE;

public enum WorkOrderExceptionError implements BaseExceptionError {
    WAREHOUSE_NOT_EXISTS(WORK_ORDER_ERROR_CODE.code() + "101", "Missing warehouse {0}"),
    UNABLE_CREATE_MATERIAL(WORK_ORDER_ERROR_CODE.code() + "102", "Unable to create material {0}"),
    UNABLE_INTEGRATE_WORK_ORDER(WORK_ORDER_ERROR_CODE.code() + "103", "Unable to integrate work order {0}"),
    BROKER_ACCESS(WORK_ORDER_ERROR_CODE.code() + "104", "Error accessing broker"),
    UNABLE_SCHEDULE_WORK_ORDER(WORK_ORDER_ERROR_CODE.code() + "105", "Unable to schedule work orders {0}"),
    LATTE_ACCESS(WORK_ORDER_ERROR_CODE.code() + "106", "Error accessing latte"),
    UNABLE_PARSE_WAREHOUSE(WORK_ORDER_ERROR_CODE.code() + "107", "Error decoding warehouse {0}: {1}"),
    WORKSTATION_NOT_EXISTS(WORK_ORDER_ERROR_CODE.code() + "108", "Missing workstation {0}"),
    UNABLE_PARSE_WORKSTATION(WORK_ORDER_ERROR_CODE.code() + "109", "Error decoding workstation {0}: {1}"),
    ;

    private String code;
    private String message;

    private WorkOrderExceptionError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }
}
