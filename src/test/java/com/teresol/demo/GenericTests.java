package com.teresol.demo;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc 
class GenericTests {

	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Autowired 
	private MockMvc mockMvc;

	@Test
	void shouldEncodePassword() {
		
		String rawPassword = "Password123";

		String encoded = passwordEncoder.encode(rawPassword);

		assertNotEquals(rawPassword, encoded);

		assertTrue(
			passwordEncoder.matches(rawPassword, encoded)
		);
	}

	// @Test
	// void shouldRegisterUser() throws Exception {

	// 	mockMvc.perform(
	// 		post("/auth/register")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content("""
	// 					{
	// 						"username": "john",
	// 						"password": "Password123",
	// 						"email": "john@example.com"
	// 					}
	// 					""")
	// 	).andExpect(status().isCreated());
	// }

}
