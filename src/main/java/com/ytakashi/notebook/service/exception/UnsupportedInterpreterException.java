package com.ytakashi.notebook.service.exception;

public class UnsupportedInterpreterException extends InterpreterException {

	private static final long serialVersionUID = 1L;
	
	public UnsupportedInterpreterException(String message, String sessionId, Object[] args) {
		super(message, sessionId, args);
	}

	public UnsupportedInterpreterException(String message, String sessionId) {
		super(message, sessionId);
	}

	public UnsupportedInterpreterException(String message) {
		super(message);
	}

}
