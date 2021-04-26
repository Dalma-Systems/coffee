package com.dalma.broker.service.exception.workorder;

import com.dalma.broker.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.broker.service.error.WorkOrderExceptionError.UPDATE_NOT_ALLOWED;

@Slf4j
public class WorkOrderUpdateNotAllowedException extends RestResponseException {

    private static final long serialVersionUID = 417861781455993855L;

    public WorkOrderUpdateNotAllowedException(String state) {
        super(UPDATE_NOT_ALLOWED.message(state));
        log.error(UPDATE_NOT_ALLOWED.message(state));
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }

    @Override
    public String getErrorCode() {
        return HttpStatus.NOT_ACCEPTABLE.toString();
    }
}
