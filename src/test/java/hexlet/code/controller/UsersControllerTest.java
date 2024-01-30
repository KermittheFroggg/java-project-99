package hexlet.code.controller;

import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users/{id}", testUser.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isObject();
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/api/users")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPassword()).isNotEqualTo(testUser.getPassword());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testShowWithoutAuth() throws Exception {

        userRepository.save(testUser);

        var request = get("/api/users/{id}", testUser.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoadUserByUsername() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getPassword()).thenReturn("testPassword");

        UserDetails returnedUserDetails = userDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", returnedUserDetails.getUsername());
        assertEquals("testPassword", returnedUserDetails.getPassword());
    }

    @Test
    public void testPasswordEncoder() {
        String rawPassword = "testPassword";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testAuthenticate() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedTestPassword";

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username)
                .password(encodedPassword)
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(mockPasswordEncoder.encode(password)).thenReturn(encodedPassword);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(username, password));

        assertEquals(username, authentication.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        User user = userRepository.save(testUser);
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setEmail("newEmail" + System.currentTimeMillis() + "@example.com");
        user.setPasswordDigest("newPassword");

        var request = put("/api/users/{id}", user.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(user));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedUser = userRepository.findById(user.getId()).get();

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(updatedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(updatedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(updatedUser.getPasswordDigest()).isNotEqualTo("newPassword");
    }

    @Test
    public void testDelete() throws Exception {
        User user = new User();
        user.setEmail("testEmail" + System.currentTimeMillis() + "@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPasswordDigest("TestPassword");

        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        Optional<User> destroyedUser = userRepository.findById(user.getId());
        assertThat(destroyedUser.isPresent()).isFalse();
    }
}

