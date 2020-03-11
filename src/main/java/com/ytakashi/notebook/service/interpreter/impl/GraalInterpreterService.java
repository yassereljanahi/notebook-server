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
import com.ytakashi.notebook.service.exception.ConcurrentContextModificationException;
import com.ytakashi.notebook.service.exception.InterpreterException;
import com.ytakashi.notebook.service.exception.InterpreterTimeoutException;
import com.ytakashi.notebook.service.storage.impl.GraalSessionStorage;
import com.ytakashi.notebook.util.Constants;

/**
 * Graal interpreter service based on GraalVM.
 * 
 * @author Takashi
 *
 */
@Service
public class GraalInterpreterService extends BaseInterpreterService {

	@Autowired
	private GraalSessionStorage sessionStorage;

	@PostConstruct
	@Override
	public void init() {
		try (Context context = Context.create()) {
			getSupportedLanguages().addAll(context.getEngine().getLanguages().keySet());
		}
	}

	@Override
	public ExecuteOutputBo execute(ExecuteInputBo executionInput) {

		GraalInterpreterContext interpreterContext = sessionStorage.findOrCreate(executionInput.getSessionId());

		if (interpreterContext.getLock().tryLock()) {
			try {
				return executeInstruction(interpreterContext, executionInput);
			} finally {
				interpreterContext.getLock().unlock();
			}
		} else {
			throw new ConcurrentContextModificationException(Constants.CONCURRENT_EXCEPTION,
					interpreterContext.getSessionId(), new Object[] { interpreterContext.getSessionId() });
		}
	}

	/**
	 * Execute instruction.
	 * 
	 * @param interpreterContext
	 *            interpreter context
	 * @param executionInput
	 *            execution input
	 * @return execution result
	 */
	private ExecuteOutputBo executeInstruction(GraalInterpreterContext interpreterContext,
			ExecuteInputBo executionInput) {

		ExecuteOutputBo output = new ExecuteOutputBo();
		output.setSessionId(interpreterContext.getSessionId());

		Timer timer = startTimer(interpreterContext);
		try {
			interpreterContext.getContext().eval(executionInput.getLanguage(), executionInput.getInstruction());
		} catch (PolyglotException e) {
			if (e.isCancelled()) {
				throw new InterpreterTimeoutException(Constants.TIMEOUT_EXCEPTION, interpreterContext.getSessionId(),
						new Object[] { interpreterContext.getTimeout() });
			} else {
				throw new InterpreterException(e.getMessage(), interpreterContext.getSessionId());
			}
		} finally {
			timer.cancel();
		}
		output.setResult(new String(interpreterContext.getOut().toByteArray()));
		interpreterContext.getOut().reset();

		return output;
	}

	/**
	 * Create and start a new timer task to control interpreter execution timeout.
	 * 
	 * @param interpreterContext
	 *            interpreter context.
	 * @return timer
	 */
	private Timer startTimer(GraalInterpreterContext interpreterContext) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				sessionStorage.evict(interpreterContext.getSessionId());
			}
		}, interpreterContext.getTimeout());
		return timer;
	}

}
