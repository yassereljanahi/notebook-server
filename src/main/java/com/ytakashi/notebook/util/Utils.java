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

public final class Utils {

	public static ExecuteInputBo parseInput(ExecuteInputDto dto) {

		ExecuteInputBo bo = new ExecuteInputBo();
		bo.setInterpreterName(dto.getCode().substring(1, dto.getCode().indexOf(Constants.WHITE_SPACE)));
		String instruction = dto.getCode().substring(dto.getCode().indexOf(Constants.WHITE_SPACE) + 1,
				dto.getCode().length());
		bo.setInstruction(instruction.trim());
		bo.setSessionId(dto.getSessionId());

		return bo;

	}

	public static void validateInput(ExecuteInputDto dto) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ExecuteInputDto>> violations = validator.validate(dto);

		if (!violations.isEmpty()) {
			throw new InvalidInputException(violations.iterator().next().getMessage(), dto.getSessionId());
		}

	}

	public static String generateSessionId() {
		return UUID.randomUUID().toString();
	}
	
	private Utils() {
	}

}
