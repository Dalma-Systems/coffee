package com.dalma.coffee.erp.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.erp.error.ErpExceptionError.ERP_NOT_SUPPORTED;

@Slf4j
public class ErpNotSupportedException extends RestResponseException {

	private static final long serialVersionUID = -24611070898306606L;

	public ErpNotSupportedException(String type) {
		super(ERP_NOT_SUPPORTED.message(type));
		log.error(ERP_NOT_SUPPORTED.message(type));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getErrorCode() {
		return HttpStatus.BAD_REQUEST.toString();
	}
}
