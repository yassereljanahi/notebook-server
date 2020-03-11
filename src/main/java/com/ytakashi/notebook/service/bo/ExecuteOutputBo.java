package com.ytakashi.notebook.service.bo;

/**
 * 
 * Execution output business object.
 * 
 * @author Takashi
 *
 */
public class ExecuteOutputBo {

	private String result;
	private String error;
	private String sessionId;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
