package com.ytakashi.notebook.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ytakashi.notebook.service.InterpreterFacade;
import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.service.exception.UnsupportedInterpreterException;
import com.ytakashi.notebook.service.interpreter.InterpreterService;
import com.ytakashi.notebook.util.Constants;

@Service
public class InterpreterFacadeImpl implements InterpreterFacade {

	@Autowired
	private List<InterpreterService> interpreters;

	@Override
	public ExecuteOutputBo execute(ExecuteInputBo input) {

		Optional<InterpreterService> interpreter = interpreters.stream()
				.filter(item -> item.getSupportedLanguages().contains(input.getLanguage())).findFirst();

		if (interpreter.isPresent()) {
			return interpreter.get().execute(input);
		} else {
			Set<String> languages = interpreters.stream().flatMap(item -> item.getSupportedLanguages().stream())
					.collect(Collectors.toSet());
			throw new UnsupportedInterpreterException(Constants.UNSUPPORTED_INTERPRETER_EXCEPTION, input.getSessionId(),
					new Object[] { input.getLanguage(), languages.toString() });
		}

	}

}
