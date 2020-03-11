package com.ytakashi.notebook.service.bo;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.Lock;

import org.graalvm.polyglot.Context;

/**
 * Graal Interpreter state holder.
 * 
 * @author Takashi
 *
 */
public class GraalInterpreterContext {

	private String sessionId;
	private Context context;
	private Lock lock;
	private ByteArrayOutputStream out;
	private Long timeout;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public ByteArrayOutputStream getOut() {
		return out;
	}

	public void setOut(ByteArrayOutputStream out) {
		this.out = out;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

}
