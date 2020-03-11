package com.ytakashi.notebook.service.interpreter;

import java.util.Set;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.service.exception.ConcurrentContextModificationException;

/**
 * Interpreter service for executing instructions.
 * 
 * @author Takashi
 *
 */
public interface InterpreterService {

	/**
	 * Post construction initialization.
	 */
	public void init();

	/**
	 * Get set of supported languages by the interpreter service.
	 * 
	 * @return set of supported languages.
	 */
	public Set<String> getSupportedLanguages();

	/**
	 * Execute instruction.
	 * 
	 * @param input
	 *            execution input. @return execution result.
	 * @throws ConcurrentContextModificationException
	 *             if Interpreter lock is locked.
	 * @throws InterpreterTimeoutException
	 *             is interpreter execution times out.
	 * @throws InterpreterException
	 *             if any interpretation errors.
	 */
	public ExecuteOutputBo execute(ExecuteInputBo input);

}
