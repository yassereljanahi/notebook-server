package com.ytakashi.notebook.rest.handler;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ytakashi.notebook.rest.dto.ExecuteOutputDto;
import com.ytakashi.notebook.service.exception.ConcurrentSessionException;
import com.ytakashi.notebook.service.exception.InterpreterException;
import com.ytakashi.notebook.service.exception.InterpreterTimeoutException;
import com.ytakashi.notebook.service.exception.InvalidInputException;
import com.ytakashi.notebook.service.exception.UnsupportedInterpreterException;

@ControllerAdvice
public class RestExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(value = { ConcurrentSessionException.class })
	protected ResponseEntity<ExecuteOutputDto> handleConcurrentSessionException(ConcurrentSessionException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorOutput(exception, locale));
	}

	@ExceptionHandler(value = { InterpreterTimeoutException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInterpreterTimeoutException(InterpreterTimeoutException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(generateErrorOutput(exception, locale));
	}

	@ExceptionHandler(value = { InterpreterException.class, UnsupportedInterpreterException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInterpretationException(InterpreterException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorOutput(exception, locale));
	}

	@ExceptionHandler(value = { InvalidInputException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInvalidInputException(InvalidInputException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(generateErrorOutput(exception, locale));
	}

	@ExceptionHandler(value = { UnsupportedOperationException.class })
	protected ResponseEntity<ExecuteOutputDto> handleUnsupportedOperationException(
			UnsupportedOperationException exception, Locale locale) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(generateErrorOutput(exception));
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ExecuteOutputDto> handleUnexpectedException(Exception exception, Locale locale) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateErrorOutput(exception));
	}

	private ExecuteOutputDto generateErrorOutput(InterpreterException exception, Locale locale) {
		ExecuteOutputDto errorOutput = new ExecuteOutputDto();
		try {
			String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), locale);
			errorOutput.setError(message);
		} catch (NoSuchMessageException e) {
			errorOutput.setError(exception.getMessage());
		}
		errorOutput.setSessionId(exception.getSessionId());

		return errorOutput;
	}

	private ExecuteOutputDto generateErrorOutput(Exception exception) {
		ExecuteOutputDto errorOutput = new ExecuteOutputDto();
		errorOutput.setError(exception.getMessage());

		return errorOutput;
	}

}
