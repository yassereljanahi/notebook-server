package com.ytakashi.notebook.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of rest and exception handler methods.
 * 
 * @author Takashi
 *
 */
@Aspect
@Component
public class LogginAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogginAspect.class);

	/**
	 * Advice that logs when rest method is entered.
	 * 
	 * @param joinPoint
	 *            join point for advice.
	 * @param input
	 *            rest method input.
	 */
	@Before(value = "within(com.ytakashi.notebook.rest.*) and args(input)", argNames = "input")
	public void beginRestLogging(JoinPoint joinPoint, Object input) {
		LOGGER.info("[RestController] Enter method '{}' with input '{}'", joinPoint.getSignature().getName(), input);
	}

	/**
	 * Advice that logs when rest method is exited.
	 * 
	 * @param joinPoint
	 *            join point for advice.
	 * @param output
	 *            rest method output.
	 */
	@AfterReturning(pointcut = "within(com.ytakashi.notebook.rest.*)", returning = "output")
	public void endRestLogging(JoinPoint joinPoint, Object output) {
		LOGGER.info("[RestController] Exit method '{}' with output '{}'", joinPoint.getSignature().getName(), output);
	}

	/**
	 * Advice that logs when exception handler method is exited.
	 * 
	 * @param joinPoint
	 *            join point for advice.
	 * @param output
	 *            exception handler method output.
	 */
	@AfterReturning(pointcut = "within(com.ytakashi.notebook.rest.handler.*)", returning = "output")
	public void exceptionHandlerLogging(JoinPoint joinPoint, Object output) {
		LOGGER.error("[ExceptionHandler] End execution with output '{}'", output);
	}

}
