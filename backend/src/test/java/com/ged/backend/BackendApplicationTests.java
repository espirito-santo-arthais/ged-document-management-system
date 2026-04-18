package com.ged.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class BackendApplicationTests {

	@Autowired
	Environment env;

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
	    System.out.println(env.getProperty("spring.datasource.url"));
	}
}
