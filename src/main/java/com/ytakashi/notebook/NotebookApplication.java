package com.ytakashi.notebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Notebook application for interpreting statements in different languages
 * (js, Python...)
 * 
 * @author Takashi
 *
 */
@SpringBootApplication
public class NotebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotebookApplication.class, args);
	}

}