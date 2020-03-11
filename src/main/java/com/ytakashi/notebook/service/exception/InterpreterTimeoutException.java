package com.ytakashi.notebook.service.exception;

/**
 * 
 * This exception may be thrown by service method if interpreter execution times
 * out.
 * 
 * @author Takashi
 *
 */
public class InterpreterTimeoutException extends InterpreterException {

	private static final long serialVersionUID = 1L;

	public InterpreterTimeoutException(String message, String sessionId, Object[] args) {
		super(message, sessionId, args);
	}

	public InterpreterTimeoutException(String message, String sessionId) {
		super(message, sessionId);
	}

	public InterpreterTimeoutException(String message) {
		super(message);
	}

}
