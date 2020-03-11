package com.ytakashi.notebook.util;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ytakashi.notebook.rest.dto.ExecuteInputDto;
import com.ytakashi.notebook.service.bo.ExecuteInputBo;
import com.ytakashi.notebook.service.exception.InvalidInputException;

/**
 * Global utils class
 * 
 * @author Takashi
 *
 */
public final class Utils {

	/**
	 * Parse input (Dto) into business object. 
	 * 
	 * @param input execution input
	 * @return
	 */
	public static ExecuteInputBo parseInput(ExecuteInputDto input) {

		ExecuteInputBo bo = new ExecuteInputBo();
		bo.setLanguage(input.getCode().substring(1, input.getCode().indexOf(Constants.WHITE_SPACE)));
		String instruction = input.getCode().substring(input.getCode().indexOf(Constants.WHITE_SPACE) + 1,
				input.getCode().length());
		bo.setInstruction(instruction.trim());
		bo.setSessionId(input.getSessionId());

		return bo;

	}

	/**
	 * Input validation method.
	 * 
	 * @param input execution input
	 */
	public static void validateInput(ExecuteInputDto input) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ExecuteInputDto>> violations = validator.validate(input);

		if (!violations.isEmpty()) {
			throw new InvalidInputException(violations.iterator().next().getMessage(), input.getSessionId());
		}

	}

	/**
	 * Session id generator using UUID.
	 * 
	 * @return session id
	 */
	public static String generateSessionId() {
		return UUID.randomUUID().toString();
	}
	
	private Utils() {
	}

}
