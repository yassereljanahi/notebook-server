package com.ytakashi.notebook.service.exception;

public class InterpreterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String sessionId;
	private final Object[] args;

	public InterpreterException(String message) {
		this(message, null, null);
	}

	public InterpreterException(String message, String sessionId) {
		this(message, sessionId, null);
	}
	
	public InterpreterException(String message, String sessionId, Object[] args) {
		super(message);
		this.sessionId = sessionId;
		this.args = args;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public Object[] getArgs() {
		return args;
	}

}
