package com.ytakashi.notebook.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogginAspect.class);

	@Before(value = "execution(* com.ytakashi.notebook.rest.*.*(..)) and args(input)", argNames = "input")
	public void beginRestLogging(JoinPoint joinPoint, Object input) {
		LOGGER.info("[RestController] Begin call to method '/{}' with input '{}'", joinPoint.getSignature().getName(),
				input);
	}

	@AfterReturning(pointcut = "execution(* com.ytakashi.notebook.rest.*.*(..))", returning = "output")
	public void endRestLogging(JoinPoint joinPoint, Object output) {
		LOGGER.info("[RestController] End call to method : '/{}' with output '{}'", joinPoint.getSignature().getName(),
				output);
	}

	@AfterReturning(pointcut = "execution(* com.ytakashi.notebook.rest.handler.*.*(..))", returning = "output")
	public void exceptionHandlerLogging(JoinPoint joinPoint, Object output) {
		LOGGER.error("[ExceptionHandler] End execution with output '{}'", output);
	}

	@Before(value = "execution(* com.ytakashi.notebook.service.impl.*.*(..))")
	public void interpreterServiceLogging(JoinPoint joinPoint) {
		LOGGER.info("[InterpreterService] Calling method '{}'", joinPoint.getSignature());
	}
	
	//@Before(value = "execution(* com.ytakashi.notebook.service.storage.impl.*.*(..))")
	public void sessionStorageLogging(JoinPoint joinPoint) {
		LOGGER.info("[SessionStorage] Calling method '{}'", joinPoint.getSignature().getName());
	}

}
