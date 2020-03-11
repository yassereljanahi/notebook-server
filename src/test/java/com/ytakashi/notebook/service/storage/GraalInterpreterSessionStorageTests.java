package com.ytakashi.notebook.service.storage;

import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ytakashi.notebook.service.bo.GraalInterpreterContext;
import com.ytakashi.notebook.service.storage.impl.GraalInterpreterSessionStorage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraalInterpreterSessionStorageTests {

	@Autowired
	private GraalInterpreterSessionStorage sessionStorage;

	@Test
	public void createWithoutKeyTest() {
		GraalInterpreterContext interpreter = sessionStorage.create(null);

		Assert.assertThat(sessionStorage.find(interpreter.getSessionId()), CoreMatchers.notNullValue());
	}

	@Test
	public void createWithKeyTest() {
		GraalInterpreterContext interpreter = sessionStorage.create("123456789");

		Assert.assertThat(interpreter.getSessionId(), CoreMatchers.equalTo("123456789"));
		Assert.assertThat(sessionStorage.find(interpreter.getSessionId()), CoreMatchers.notNullValue());
	}

	@Test
	public void findTest() {

		sessionStorage.create("123456789");

		Optional<GraalInterpreterContext> interpreter = sessionStorage.find("123456789");

		Assert.assertThat(interpreter.isPresent(), CoreMatchers.equalTo(true));
	}

	@Test
	public void evictTest() {

		sessionStorage.create("123456789");
		
		sessionStorage.evict("123456789");

		Optional<GraalInterpreterContext> interpreter = sessionStorage.find("123456789");

		Assert.assertThat(interpreter.isPresent(), CoreMatchers.equalTo(false));

	}

}
