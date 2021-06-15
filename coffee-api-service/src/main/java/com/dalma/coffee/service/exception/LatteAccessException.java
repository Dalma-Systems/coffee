package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.LATTE_ACCESS;

@Slf4j
public class LatteAccessException extends RestResponseException {

	private static final long serialVersionUID = -1551174140654665664L;

	public LatteAccessException() {
		super(LATTE_ACCESS.getMessage());
		log.error(LATTE_ACCESS.getMessage());
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorCode() {
		return LATTE_ACCESS.code();
	}
}
