package com.dalma.coffee.base.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {

    private String status;
    private String error;
    private String message;
    private String traceId;
    private String code;
}
