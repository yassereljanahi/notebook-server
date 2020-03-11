package com.ytakashi.notebook.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.ytakashi.notebook.rest.dto.ExecuteInputDto;
import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.exception.InvalidInputException;

public class UtilsTests {

	@Test(expected = InvalidInputException.class)
	public void invalidNullCodeTest() {

		ExecuteInputDto input = new ExecuteInputDto();
		input.setCode(null);

		Utils.validateInput(input);
	}

	@Test(expected = InvalidInputException.class)
	public void invalidCodePatternTest() {

		ExecuteInputDto input = new ExecuteInputDto();
		input.setCode("%python");

		Utils.validateInput(input);
	}

	@Test
	public void validInputTest() {

		ExecuteInputDto input = new ExecuteInputDto();
		input.setCode("%python print(a)");

		Utils.validateInput(input);
	}

	@Test
	public void parseInputTest() {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%python print(a)");
		inputDto.setSessionId("123456");

		ExecuteInputBo inputBo = Utils.parseInput(inputDto);

		Assert.assertThat(inputBo.getLanguage(), CoreMatchers.equalTo("python"));
		Assert.assertThat(inputBo.getInstruction(), CoreMatchers.equalTo("print(a)"));
		Assert.assertThat(inputBo.getSessionId(), CoreMatchers.equalTo(inputDto.getSessionId()));
	}
	
	@Test
	public void sessionIdGenerationTest() {
		
		String sessionId = Utils.generateSessionId();
		
		Assert.assertThat(sessionId, CoreMatchers.notNullValue());
	}

}
