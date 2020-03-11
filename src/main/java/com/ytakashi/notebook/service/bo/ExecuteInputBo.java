package com.ytakashi.notebook.service.bo;

/**
 * 
 * Execution input busines object.
 * 
 * @author Takashi
 *
 */
public class ExecuteInputBo {

	private String language;
	private String instruction;
	private String sessionId;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
