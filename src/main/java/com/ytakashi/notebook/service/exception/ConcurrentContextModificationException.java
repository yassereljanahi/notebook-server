package com.ytakashi.notebook.service.exception;

/**
 * This exception may be thrown by service method if any concurrent access to
 * the same InterpreterContext.
 * 
 * @author Takashi
 *
 */
public class ConcurrentContextModificationException extends InterpreterException {

	private static final long serialVersionUID = 1L;

	public ConcurrentContextModificationException(String message, String sessionId, Object[] args) {
		super(message, sessionId, args);
	}

	public ConcurrentContextModificationException(String message, String sessionId) {
		super(message, sessionId);
	}

	public ConcurrentContextModificationException(String message) {
		super(message);
	}

}
