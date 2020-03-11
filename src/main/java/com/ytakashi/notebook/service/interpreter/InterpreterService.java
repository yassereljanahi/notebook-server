package com.ytakashi.notebook.service.interpreter;

import java.util.Set;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;

public interface InterpreterService {

	public void init();
	
	public Set<String> getSupportedLanguages();

	public ExecuteOutputBo execute(ExecuteInputBo input);

}
