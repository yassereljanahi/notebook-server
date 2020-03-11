package com.ytakashi.notebook.service.interpreter;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.graalvm.polyglot.Context;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.service.bo.GraalInterpreterContext;
import com.ytakashi.notebook.service.exception.InterpreterTimeoutException;
import com.ytakashi.notebook.service.interpreter.impl.GraalInterpreterService;
import com.ytakashi.notebook.service.storage.impl.GraalInterpreterSessionStorage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraalInterpreterServiceTests {

	private static final String SESSION_ID = "12345678";

	@Autowired
	private GraalInterpreterService interpreterService;

	@MockBean
	private GraalInterpreterSessionStorage sessionStorage;

	@Test
	public void supportedLanguagesTest() {

		Set<String> languages = interpreterService.getSupportedLanguages();

		Assert.assertThat(languages.isEmpty(), CoreMatchers.equalTo(false));

	}

	@Test(expected = InterpreterTimeoutException.class)
	public void timeoutInterpreterTest() {

		GraalInterpreterContext interpreter = mockSessionStorage(SESSION_ID, 5L);
		Mockito.doAnswer(invocation -> {
			interpreter.getContext().close(true);
			return null;
		}).when(sessionStorage).evict(SESSION_ID);

		ExecuteInputBo inputBo = new ExecuteInputBo();
		inputBo.setInterpreterName("js");
		inputBo.setInstruction("while(true);");
		inputBo.setSessionId(SESSION_ID);

		interpreterService.execute(inputBo);

	}

	@Test(expected = ExecutionException.class)
	public void concurrentInterpreterOnSameSessionTest() throws InterruptedException, ExecutionException {

		mockSessionStorage(SESSION_ID);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		Future<ExecuteOutputBo> future = getFuture(executor, SESSION_ID, "var a=2; print(a);");
		Future<ExecuteOutputBo> future2 = getFuture(executor, SESSION_ID, "var a=1; print(a);");

		Assert.assertThat(future.get().getResult(), CoreMatchers.equalTo("2\n"));
		future2.get().getResult();

		executor.shutdown();

	}

	@Test
	public void concurrentInterpreterOnDifferentSessionsTest() throws InterruptedException, ExecutionException {

		mockSessionStorage("12345679");
		mockSessionStorage(SESSION_ID);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		Future<ExecuteOutputBo> future = getFuture(executor, "12345679", "var a=2; print(a);");
		Future<ExecuteOutputBo> future2 = getFuture(executor, SESSION_ID, "var a=1; print(a);");

		Assert.assertThat(future.get().getResult(), CoreMatchers.equalTo("2\n"));
		Assert.assertThat(future2.get().getResult(), CoreMatchers.equalTo("1\n"));

		executor.shutdown();

	}

	private GraalInterpreterContext mockSessionStorage(String sessionId) {
		return mockSessionStorage(sessionId, 5000L);
	}

	private GraalInterpreterContext mockSessionStorage(String sessionId, long timeout) {

		GraalInterpreterContext interpreter = new GraalInterpreterContext();
		interpreter.setSessionId(sessionId);
		interpreter.setOut(new ByteArrayOutputStream());
		interpreter.setLock(new ReentrantLock());
		interpreter.setTimeout(timeout);
		interpreter.setContext(Context.newBuilder().out(interpreter.getOut()).build());
		Mockito.when(sessionStorage.findOrCreate(sessionId)).thenReturn(interpreter);
		return interpreter;

	}

	private Future<ExecuteOutputBo> getFuture(ExecutorService executor, String sessionId, String instruction) {
		return executor.submit(() -> {
			ExecuteInputBo inputBo = new ExecuteInputBo();
			inputBo.setInterpreterName("js");
			inputBo.setInstruction(instruction);
			inputBo.setSessionId(sessionId);

			return interpreterService.execute(inputBo);
		});
	}

}
