package com.dalma.coffee.erp.error;

public enum BaseExceptionError {
    ERP_ERROR_CODE("ERP"),
    SAP_ERROR_CODE("ERP_SAP"),
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
