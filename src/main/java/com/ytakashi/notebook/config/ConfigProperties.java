package com.ytakashi.notebook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Custom application configuration properties.
 * 
 * @author Takashi
 *
 */
@Configuration
public class ConfigProperties {

	@Value("${interpreter.timeout:10000}")
	private long interpreterTimeout;

	public long getInterpreterTimeout() {
		return interpreterTimeout;
	}

}
