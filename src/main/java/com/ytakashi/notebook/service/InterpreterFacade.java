package com.ytakashi.notebook.service;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.service.exception.UnsupportedInterpreterException;

/**
 * Interpreter facade that lookup for an InterpreterService that can handle the
 * execution request.
 * 
 * @author Takashi
 *
 */
public interface InterpreterFacade {

	/**
	 * Execute statements using fetched InterpreterService instance.
	 * 
	 * @param input
	 *            execution input.
	 * @return execution result.
	 * @throws UnsupportedInterpreterException
	 *             if no InterpreterService found to handle the request.
	 */
	public ExecuteOutputBo execute(ExecuteInputBo input);

}
