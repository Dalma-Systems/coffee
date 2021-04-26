package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.UNABLE_INTEGRATE_WORK_ORDER;

@Slf4j
public class UnableIntegrateWorkOrderException extends RestResponseException {

	private static final long serialVersionUID = 1254579045639614615L;

	public UnableIntegrateWorkOrderException(String orderId) {
		super(UNABLE_INTEGRATE_WORK_ORDER.message(orderId));
		log.error(UNABLE_INTEGRATE_WORK_ORDER.message(orderId));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.PRECONDITION_FAILED;
	}

	@Override
	public String getErrorCode() {
		return HttpStatus.PRECONDITION_FAILED.toString();
	}
}
