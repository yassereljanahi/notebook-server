package com.ytakashi.notebook.service;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;

public interface InterpreterFacade {

	public ExecuteOutputBo execute(ExecuteInputBo input);
	
}
