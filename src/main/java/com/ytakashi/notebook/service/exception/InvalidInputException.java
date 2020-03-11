package com.ytakashi.notebook.service.exception;

/**
 * 
 * This exception may be thrown by validation method if REST service input is
 * invalid.
 * 
 * @author Takashi
 *
 */
public class InvalidInputException extends InterpreterException {

	private static final long serialVersionUID = 1L;

	public InvalidInputException(String message, String sessionId, Object[] args) {
		super(message, sessionId, args);
	}

	public InvalidInputException(String message, String sessionId) {
		super(message, sessionId);
	}

	public InvalidInputException(String message) {
		super(message);
	}

}
