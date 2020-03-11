package com.ytakashi.notebook.service.storage.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.graalvm.polyglot.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ytakashi.notebook.config.ConfigProperties;
import com.ytakashi.notebook.service.bo.GraalInterpreterContext;
import com.ytakashi.notebook.util.Utils;

@Service
public class GraalInterpreterSessionStorage extends BaseSessionStorage<String, GraalInterpreterContext> {

	@Autowired
	private ConfigProperties config;

	@Override
	public GraalInterpreterContext create(String key) {

		GraalInterpreterContext interpreter = new GraalInterpreterContext();

		String sessionId;
		if (key == null || key.isEmpty()) {
			sessionId = Utils.generateSessionId();
		} else {
			sessionId = key;
		}

		interpreter.setSessionId(sessionId);
		interpreter.setOut(new ByteArrayOutputStream());
		interpreter.setLock(new ReentrantLock());
		interpreter.setTimeout(config.getGraalInterpreterTimeout());
		interpreter.setContext(Context.newBuilder().out(interpreter.getOut()).build());

		getSessionStorage().put(sessionId, interpreter);

		return interpreter;

	}

	@Override
	public void evict(String key) {

		Optional<GraalInterpreterContext> interpreter = find(key);
		if (interpreter.isPresent()) {
			freeResources(interpreter.get());
			super.evict(key);
		}

	}

	@Override
	public GraalInterpreterContext findOrCreate(String key) {

		Optional<GraalInterpreterContext> interpreter = find(key);
		if (!interpreter.isPresent()) {
			return create(key);
		} else {
			return interpreter.get();
		}

	}

	@Override
	public void freeResources(GraalInterpreterContext item) {
		item.getContext().close(true);
		try {
			item.getOut().close();
		} catch (IOException e) {
			
		}
	}

}
