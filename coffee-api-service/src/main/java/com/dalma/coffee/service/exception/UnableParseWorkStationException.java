package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.UNABLE_PARSE_WORKSTATION;

@Slf4j
public class UnableParseWorkStationException extends RestResponseException {

	private static final long serialVersionUID = 2933956885782321892L;

	public UnableParseWorkStationException(String id, String content) {
		super(MessageFormat.format(UNABLE_PARSE_WORKSTATION.getMessage(), id, content));
		log.error(MessageFormat.format(UNABLE_PARSE_WORKSTATION.getMessage(), id, content));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorCode() {
		return UNABLE_PARSE_WORKSTATION.code();
	}
}
