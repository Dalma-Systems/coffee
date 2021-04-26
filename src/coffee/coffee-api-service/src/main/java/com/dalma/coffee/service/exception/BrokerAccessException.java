package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.BROKER_ACCESS;

@Slf4j
public class BrokerAccessException extends RestResponseException {

	private static final long serialVersionUID = 6562509476871452857L;

	public BrokerAccessException() {
		super(BROKER_ACCESS.getMessage());
		log.error(BROKER_ACCESS.getMessage());
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.toString();
	}
}
