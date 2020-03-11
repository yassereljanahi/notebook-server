package com.ytakashi.notebook.rest.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExecuteInputDto {

	@Pattern(regexp = "^%\\w+\\s+.*", message = "code should respect the following format : %<interpreter-name><whitespace><code>")
	@NotEmpty
	private String code;
	private String sessionId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "ExecuteInputDto [code=" + code + ", sessionId=" + sessionId + "]";
	}

}
