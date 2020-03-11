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
import com.ytakashi.notebook.service.exception.ConcurrentContextModificationException;
import com.ytakashi.notebook.service.exception.InterpreterException;
import com.ytakashi.notebook.service.exception.InterpreterTimeoutException;
import com.ytakashi.notebook.service.exception.InvalidInputException;
import com.ytakashi.notebook.service.exception.UnsupportedInterpreterException;

/**
 * Handle application exceptions and return specific response based on the
 * message key.
 * 
 * @author Takashi
 *
 */
@ControllerAdvice
public class RestExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	/**
	 * Handle concurrent session access exception.
	 * 
	 * @param exception
	 *            concurrent session exception
	 * @param locale
	 * @return responseEntity with status code 409 (CONFLICT) and error message in
	 *         body
	 */
	@ExceptionHandler(value = { ConcurrentContextModificationException.class })
	protected ResponseEntity<ExecuteOutputDto> handleConcurrentSessionException(ConcurrentContextModificationException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorOutput(exception, locale));
	}

	/**
	 * Handle interpreter execution timeout exception.
	 * 
	 * @param exception
	 *            interpreter execution
	 * @param locale
	 * @return responseEntity with status code 408 (REQUEST_TIMEOUT) and error
	 *         message in body
	 */
	@ExceptionHandler(value = { InterpreterTimeoutException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInterpreterTimeoutException(InterpreterTimeoutException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(generateErrorOutput(exception, locale));
	}

	/**
	 * Handle interpreter exception.
	 * 
	 * @param exception
	 * @param locale
	 * @return responseEntity with status code 400 (BAD_REQUEST) and error message
	 *         in body
	 */
	@ExceptionHandler(value = { InterpreterException.class, UnsupportedInterpreterException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInterpretationException(InterpreterException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorOutput(exception, locale));
	}

	/**
	 * Handle validation exception.
	 * 
	 * @param exception
	 *            invalid input
	 * @param locale
	 * @return responseEntity with status code 422 (UNPROCESSABLE_ENTITY) and error
	 *         message in body
	 */
	@ExceptionHandler(value = { InvalidInputException.class })
	protected ResponseEntity<ExecuteOutputDto> handleInvalidInputException(InvalidInputException exception,
			Locale locale) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(generateErrorOutput(exception, locale));
	}

	/**
	 * Handle UnsupportedOperationException.
	 * 
	 * @param exception
	 *            unsupported operation
	 * @param locale
	 * @return responseEntity with status code 501 (NOT_IMPLEMENTED) and error
	 *         message in body
	 */
	@ExceptionHandler(value = { UnsupportedOperationException.class })
	protected ResponseEntity<ExecuteOutputDto> handleUnsupportedOperationException(
			UnsupportedOperationException exception) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(generateErrorOutput(exception));
	}

	/**
	 * Handle unexpected exceptions.
	 * 
	 * @param exception
	 *            any sub class of Exception
	 * @param locale
	 * @return responseEntity with status code 500 (INTERNAL_SERVER_ERROR) and error
	 *         message in body
	 */
	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ExecuteOutputDto> handleUnexpectedException(Exception exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateErrorOutput(exception));
	}

	/**
	 * Generate error output response. Error message is loaded from MessageSource
	 * using exception message.
	 * 
	 * @param exception
	 * @param locale
	 * @return responseEntity with error message
	 */
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

	/**
	 * Generate error output response.
	 * 
	 * @param exception
	 *            any sub class of Excpetion
	 * @return responseEntity with error message
	 */
	private ExecuteOutputDto generateErrorOutput(Exception exception) {
		ExecuteOutputDto errorOutput = new ExecuteOutputDto();
		errorOutput.setError(exception.getMessage());

		return errorOutput;
	}

}
