package com.ytakashi.notebook.service.interpreter.impl;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.service.bo.GraalInterpreterContext;
import com.ytakashi.notebook.service.exception.ConcurrentSessionException;
import com.ytakashi.notebook.service.exception.InterpreterException;
import com.ytakashi.notebook.service.exception.InterpreterTimeoutException;
import com.ytakashi.notebook.service.storage.impl.GraalInterpreterSessionStorage;
import com.ytakashi.notebook.util.Constants;

@Service
public class GraalInterpreterService extends BaseInterpreterService {

	@Autowired
	private GraalInterpreterSessionStorage sessionStorage;

	@PostConstruct
	@Override
	public void init() {
		try (Context context = Context.create()) {
			getSupportedLanguages().addAll(context.getEngine().getLanguages().keySet());
		}
	}

	@Override
	public ExecuteOutputBo execute(ExecuteInputBo executionInput) {

		GraalInterpreterContext interpreter = sessionStorage.findOrCreate(executionInput.getSessionId());

		if (interpreter.getLock().tryLock()) {
			try {
				return executeInstruction(interpreter, executionInput);
			} finally {
				interpreter.getLock().unlock();
			}
		} else {
			throw new ConcurrentSessionException(Constants.CONCURRENT_EXCEPTION, interpreter.getSessionId(),
					new Object[] { interpreter.getSessionId() });
		}
	}

	private ExecuteOutputBo executeInstruction(GraalInterpreterContext interpreter, ExecuteInputBo executionInput) {

		ExecuteOutputBo output = new ExecuteOutputBo();
		output.setSessionId(interpreter.getSessionId());

		Timer timer = startTimer(interpreter);
		try {
			interpreter.getContext().eval(executionInput.getInterpreterName(), executionInput.getInstruction());
		} catch (PolyglotException e) {
			if (e.isCancelled()) {
				throw new InterpreterTimeoutException(Constants.TIMEOUT_EXCEPTION, interpreter.getSessionId(),
						new Object[] { interpreter.getTimeout() });
			} else {
				throw new InterpreterException(e.getMessage(), interpreter.getSessionId());
			}
		} finally {
			timer.cancel();
		}
		output.setResult(new String(interpreter.getOut().toByteArray()));
		interpreter.getOut().reset();

		return output;
	}

	private Timer startTimer(GraalInterpreterContext interpreter) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				sessionStorage.evict(interpreter.getSessionId());
			}
		}, interpreter.getTimeout());
		return timer;
	}

}
