package com.ytakashi.notebook.service.interpreter.impl;

import java.util.HashSet;
import java.util.Set;

import com.ytakashi.notebook.service.interpreter.InterpreterService;

public abstract class BaseInterpreterService implements InterpreterService {

	private final Set<String> languages = new HashSet<>();

	@Override
	public Set<String> getSupportedLanguages() {
		return languages;
	}

}
