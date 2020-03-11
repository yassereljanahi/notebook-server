package com.ytakashi.notebook.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytakashi.notebook.rest.dto.ExecuteInputDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InterpreterControllerIT {

	private static final String URI = "/execute";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void invalidInputTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("js");

		mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}

	@Test
	public void timeoutTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%js while(true);");

		mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.REQUEST_TIMEOUT.value()));
	}

	@Test
	public void concurrentSessionTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%js for (i = 0; i < 10000; i++){ print(i); }");
		inputDto.setSessionId("12345678");

		ExecutorService executor = Executors.newFixedThreadPool(2);

		executor.submit(() -> {
			try {
				mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		executor.submit(() -> {
			try {
				Thread.sleep(1000);
				mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.CONFLICT.value()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		executor.shutdown();
	}

	@Test
	public void operationNotImplementedTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%sql SELECT * FROM TAB");

		mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.NOT_IMPLEMENTED.value()));

	}

	@Test
	public void unsupportedInterpreterTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%C int a=2;");

		mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

	}
	
	@Test
	public void interpreterExceptionTest() throws Exception {

		ExecuteInputDto inputDto = new ExecuteInputDto();
		inputDto.setCode("%js print(2");

		mockMvc.perform(post(URI).content(asJsonString(inputDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

	}
	
	private String asJsonString(final Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

}
