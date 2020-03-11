package com.ytakashi.notebook.service.bo;

public class ExecuteInputBo {

	private String interpreterName;
	private String instruction;
	private String sessionId;

	public String getInterpreterName() {
		return interpreterName;
	}

	public void setInterpreterName(String interpreterName) {
		this.interpreterName = interpreterName;
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
