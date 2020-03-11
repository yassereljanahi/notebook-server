package com.ytakashi.notebook.service.interpreter.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;

@Service
public class SqlInterpreterService extends BaseInterpreterService {

	@PostConstruct
	@Override
	public void init() {
		getSupportedLanguages().add("sql");
	}

	@Override
	public ExecuteOutputBo execute(ExecuteInputBo input) {
		throw new UnsupportedOperationException("Sql interpretor not yet implemented");
	}

}
