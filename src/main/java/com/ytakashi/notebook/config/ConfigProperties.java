package com.ytakashi.notebook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

	@Value("${graal.interpreter.timeout}")
	private long graalInterpreterTimeout;

	public long getGraalInterpreterTimeout() {
		return graalInterpreterTimeout;
	}

}
