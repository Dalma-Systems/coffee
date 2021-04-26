package com.dalma.coffee.erp.error;

import com.dalma.coffee.base.error.BaseExceptionError;

import static com.dalma.coffee.erp.error.BaseExceptionError.SAP_ERROR_CODE;

public enum SapExceptionError implements BaseExceptionError {
    SAP_ERROR(SAP_ERROR_CODE.code() + "101", "SAP returned error: {0}"),
    SAP_NO_ORDERS_RETURNED(SAP_ERROR_CODE.code() + "102", "SAP returned success but no orders."),
    ;

    private String code;
    private String message;

    private SapExceptionError(String code, String message) {
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
