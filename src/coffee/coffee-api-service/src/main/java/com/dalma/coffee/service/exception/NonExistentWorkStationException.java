package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.WORKSTATION_NOT_EXISTS;

@Slf4j
public class NonExistentWorkStationException extends RestResponseException {

	private static final long serialVersionUID = -7751630619085969615L;

	public NonExistentWorkStationException(String id) {
		super(WORKSTATION_NOT_EXISTS.message(id));
		log.error(WORKSTATION_NOT_EXISTS.message(id));
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
