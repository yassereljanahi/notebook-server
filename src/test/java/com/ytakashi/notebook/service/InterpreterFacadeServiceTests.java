package com.ytakashi.notebook.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.exception.UnsupportedInterpreterException;
import com.ytakashi.notebook.service.interpreter.impl.GraalInterpreterService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterpreterFacadeServiceTests {

	@Autowired
	private InterpreterFacade interpreterFacade;

	@MockBean
	private GraalInterpreterService service;

	@Before
	public void setUp() {

		Set<String> languages = new HashSet<>();
		languages.add("js");

		Mockito.when(service.getSupportedLanguages()).thenReturn(languages);
	}

	@Test
	public void supportedLanguageTest() {

		ExecuteInputBo inputBo = new ExecuteInputBo();
		inputBo.setInterpreterName("js");
		inputBo.setInstruction("print(1);");

		interpreterFacade.execute(inputBo);

	}

	@Test(expected = UnsupportedInterpreterException.class)
	public void unsupportedLanguageTest() {

		ExecuteInputBo inputBo = new ExecuteInputBo();
		inputBo.setInterpreterName("python");
		inputBo.setInstruction("print(1);");

		interpreterFacade.execute(inputBo);

	}

}
