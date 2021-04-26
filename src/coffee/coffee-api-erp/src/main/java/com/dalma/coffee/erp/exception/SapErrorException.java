package com.dalma.coffee.erp.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.erp.error.SapExceptionError.SAP_ERROR;

@Slf4j
public class SapErrorException extends RestResponseException {

	private static final long serialVersionUID = 5882018623546983960L;

	public SapErrorException(String error) {
		super(SAP_ERROR.message(error));
		log.error(SAP_ERROR.message(error));
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
