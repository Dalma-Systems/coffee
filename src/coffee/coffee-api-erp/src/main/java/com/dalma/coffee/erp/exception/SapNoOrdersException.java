package com.dalma.coffee.erp.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.erp.error.SapExceptionError.SAP_NO_ORDERS_RETURNED;

@Slf4j
public class SapNoOrdersException extends RestResponseException {

	private static final long serialVersionUID = -3623334939318777066L;

	public SapNoOrdersException() {
		super(SAP_NO_ORDERS_RETURNED.getMessage());
		log.error(SAP_NO_ORDERS_RETURNED.getMessage());
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
