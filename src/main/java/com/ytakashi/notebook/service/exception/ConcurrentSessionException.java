package com.ytakashi.notebook.service.exception;

public class ConcurrentSessionException extends InterpreterException {

	private static final long serialVersionUID = 1L;

	public ConcurrentSessionException(String message, String sessionId, Object[] args) {
		super(message, sessionId, args);
	}

	public ConcurrentSessionException(String message, String sessionId) {
		super(message, sessionId);
	}

	public ConcurrentSessionException(String message) {
		super(message);
	}

}
