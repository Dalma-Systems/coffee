package com.dalma.coffee.service.exception;

import com.dalma.coffee.base.error.exception.RestResponseException;
import com.dalma.common.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.dalma.coffee.service.error.WorkOrderExceptionError.UNABLE_SCHEDULE_WORK_ORDER;

@Slf4j
public class UnableScheduleWorkOrdersException extends RestResponseException {

	private static final long serialVersionUID = -2385208380006490812L;

	public UnableScheduleWorkOrdersException(List<String> ids) {
		super(UNABLE_SCHEDULE_WORK_ORDER.message(ids.stream().collect(Collectors.joining(Constant.COMMA))));
		log.error(UNABLE_SCHEDULE_WORK_ORDER.message(ids.stream().collect(Collectors.joining(Constant.COMMA))));
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
