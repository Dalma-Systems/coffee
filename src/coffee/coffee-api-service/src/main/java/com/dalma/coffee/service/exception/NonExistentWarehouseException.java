package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.WAREHOUSE_NOT_EXISTS;

@Slf4j
public class NonExistentWarehouseException extends RestResponseException {

	private static final long serialVersionUID = 1412286269955473595L;

	public NonExistentWarehouseException(String id) {
		super(WAREHOUSE_NOT_EXISTS.message(id));
		log.error(WAREHOUSE_NOT_EXISTS.message(id));
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
