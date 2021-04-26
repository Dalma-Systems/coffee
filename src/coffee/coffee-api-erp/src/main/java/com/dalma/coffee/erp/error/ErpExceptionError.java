package com.dalma.coffee.erp.error;

import com.dalma.coffee.base.error.BaseExceptionError;

import static com.dalma.coffee.erp.error.BaseExceptionError.ERP_ERROR_CODE;

public enum ErpExceptionError implements BaseExceptionError {
    ERP_NOT_SUPPORTED(ERP_ERROR_CODE.code() + "101", "ERP {0} not supported"),
    ;

    private String code;
    private String message;

    private ErpExceptionError(String code, String message) {
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
