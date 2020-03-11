package com.ytakashi.notebook.service.exception;

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
