package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.UNABLE_PARSE_WAREHOUSE;

@Slf4j
public class UnableParseWarehouseException extends RestResponseException {

	private static final long serialVersionUID = -7522344786357575210L;

	public UnableParseWarehouseException(String id, String content) {
		super(MessageFormat.format(UNABLE_PARSE_WAREHOUSE.getMessage(), id, content));
		log.error(MessageFormat.format(UNABLE_PARSE_WAREHOUSE.getMessage(), id, content));
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
