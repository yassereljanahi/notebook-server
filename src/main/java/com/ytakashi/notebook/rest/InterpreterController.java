package com.ytakashi.notebook.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ytakashi.notebook.rest.dto.ExecuteInputDto;
import com.ytakashi.notebook.rest.dto.ExecuteOutputDto;
import com.ytakashi.notebook.service.InterpreterFacade;
import com.ytakashi.notebook.service.bo.ExecuteOutputBo;
import com.ytakashi.notebook.util.Utils;

@RestController
public class InterpreterController {

	@Autowired
	private InterpreterFacade interpreterFacade;

	@PostMapping("/execute")
	public ResponseEntity<ExecuteOutputDto> execute(@RequestBody ExecuteInputDto input) {

		Utils.validateInput(input);
		ExecuteOutputBo resultBo = interpreterFacade.execute(Utils.parseInput(input));
		ExecuteOutputDto resultDto = new ExecuteOutputDto();
		BeanUtils.copyProperties(resultBo, resultDto);

		return ResponseEntity.ok(resultDto);

	}

}