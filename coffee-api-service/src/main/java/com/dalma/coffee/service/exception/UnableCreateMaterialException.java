package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.UNABLE_CREATE_MATERIAL;

@Slf4j
public class UnableCreateMaterialException extends RestResponseException {

	private static final long serialVersionUID = -7523765026041104769L;

	public UnableCreateMaterialException(String materialId) {
		super(UNABLE_CREATE_MATERIAL.message(materialId));
		log.error(UNABLE_CREATE_MATERIAL.message(materialId));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.PRECONDITION_FAILED;
	}

	@Override
	public String getErrorCode() {
		return UNABLE_CREATE_MATERIAL.code();
	}
}
