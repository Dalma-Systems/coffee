package com.dalma.coffee.service.error;

public enum BaseExceptionError {
    WORK_ORDER_ERROR_CODE("WORK_ORDER"),
    ;

    private static final String DELIMITER = "_";

    String code;

    BaseExceptionError(String code) {
        this.code = code;
    }

    public String code() {
        return code + DELIMITER;
    }
}
