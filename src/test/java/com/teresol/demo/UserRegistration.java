package com.teresol.demo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teresol.demo.entity.User;
import com.teresol.demo.repository.UserRepository;
import com.teresol.demo.user.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for:
 *
 * POST /auth/register
 *
 * These tests verify the complete registration flow:
 *
 * HTTP Request
 *      ↓
 * Controller
 *      ↓
 * Service
 *      ↓
 * PasswordEncoder
 *      ↓
 * Repository
 *      ↓
 * Database
 */
@SpringBootTest
@AutoConfigureMockMvc 
@ActiveProfiles("test")
@Transactional
class UserRegistrationIntegrationTest {

    private static final String REGISTER_URL = "/auth/register";

    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "Password123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ============================================================
    // Successful registration
    // ============================================================

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                EMAIL,
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value(USERNAME))
        .andExpect(jsonPath("$.email").value(EMAIL));

        User savedUser = findUserByUsername(USERNAME);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(USERNAME);
        assertThat(savedUser.getEmail()).isEqualTo(EMAIL);

        // Password must NEVER be stored as plain text.
        assertThat(savedUser.getPassword())
                .isNotEqualTo(PASSWORD);

        // Verify that the stored hash can authenticate the original password.
        assertThat(
                passwordEncoder.matches(
                        PASSWORD,
                        savedUser.getPassword()
                )
        ).isTrue();

        // Verify default role if your application assigns USER during registration.
        assertThat(savedUser.getRole())
                .isEqualTo(Role.USER);
    }

    // ============================================================
    // Duplicate username
    // ============================================================

    @Test
    void shouldRejectDuplicateUsername() throws Exception {

        createUser(
                USERNAME,
                "first@example.com",
                PASSWORD
        );

        String requestBody = createRegistrationRequest(
                USERNAME,
                "second@example.com",
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isConflict());

        // Only the original user should exist.
        assertThat(userRepository.count())
                .isEqualTo(1);

        User existingUser = findUserByUsername(USERNAME);

        assertThat(existingUser.getEmail())
                .isEqualTo("first@example.com");
    }

    // ============================================================
    // Duplicate email
    // ============================================================

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        createUser(
                "first-user",
                EMAIL,
                PASSWORD
        );

        String requestBody = createRegistrationRequest(
                "second-user",
                EMAIL,
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isConflict());

        assertThat(userRepository.count())
                .isEqualTo(1);
    }

    // ============================================================
    // Blank username
    // ============================================================

    @Test
    void shouldRejectBlankUsername() throws Exception {

        String requestBody = createRegistrationRequest(
                "",
                EMAIL,
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Blank email
    // ============================================================

    @Test
    void shouldRejectBlankEmail() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                "",
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Invalid email
    // ============================================================

    @Test
    void shouldRejectInvalidEmail() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                "not-an-email",
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Blank password
    // ============================================================

    @Test
    void shouldRejectBlankPassword() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                EMAIL,
                ""
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Short password
    // ============================================================

    @Test
    void shouldRejectShortPassword() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                EMAIL,
                "123"
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Missing request body
    // ============================================================

    @Test
    void shouldRejectMissingRequestBody() throws Exception {

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Wrong content type
    // ============================================================

    @Test
    void shouldRejectUnsupportedContentType() throws Exception {

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("username=john")
        )
        .andExpect(status().is4xxClientError());

        assertThat(userRepository.count())
                .isZero();
    }

    // ============================================================
    // Password must be hashed
    // ============================================================

    @Test
    void shouldHashPasswordBeforeSavingUser() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                EMAIL,
                PASSWORD
        );

        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isCreated());

        User savedUser = findUserByUsername(USERNAME);

        assertThat(savedUser.getPassword())
                .isNotEqualTo(PASSWORD);

        assertThat(savedUser.getPassword())
                .isNotBlank();

        assertThat(
                passwordEncoder.matches(
                        PASSWORD,
                        savedUser.getPassword()
                )
        ).isTrue();
    }

    // ============================================================
    // Response must not expose password
    // ============================================================

    @Test
    void shouldNotExposePasswordInResponse() throws Exception {

        String requestBody = createRegistrationRequest(
                USERNAME,
                EMAIL,
                PASSWORD
        );

        String response = mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        assertThat(json.has("password"))
                .isFalse();

        assertThat(json.has("passwordHash"))
                .isFalse();
    }

    // ============================================================
    // Helper: create JSON registration request
    // ============================================================

    private String createRegistrationRequest(
            String username,
            String email,
            String password
    ) throws Exception {

        return objectMapper.writeValueAsString(
                new RegistrationRequest(
                        username,
                        email,
                        password
                )
        );
    }

    // ============================================================
    // Helper: create user directly in database
    //
    // Used when preparing duplicate-user tests.
    // ============================================================

    private User createUser(
            String username,
            String email,
            String password
    ) {

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        // Important:
        // Test data should also use the real PasswordEncoder.
        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // ============================================================
    // Helper: find user
    // ============================================================

    private User findUserByUsername(String username) {

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new AssertionError(
                                "User not found: " + username
                        )
                );
    }

    // ============================================================
    // Test DTO
    //
    // If your production application already has a
    // RegisterRequest DTO, use that instead.
    // ============================================================

    private record RegistrationRequest(
            String username,
            String email,
            String password
    ) {
    }
}