package com.lpu.auth_service.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lpu.auth_service.config.JwtUtil;
import com.lpu.auth_service.controller.UserController;
import com.lpu.auth_service.entity.User;
import com.lpu.auth_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserController userController;

   
    @Test
    void testRegisterSuccess() {

        User user = new User();
        user.setEmail("amit@lpu.com");
        user.setPassword("pass123");

        Mockito.when(repo.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode("pass123"))
                .thenReturn("encodedPass");

        ResponseEntity<?> response = userController.register(user);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("User registered successfully", response.getBody());

        Mockito.verify(repo).save(Mockito.any(User.class));
    }

   
    @Test
    void testRegister_UserAlreadyExists() {

        User user = new User();
        user.setEmail("amit@lpu.com");

        Mockito.when(repo.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.register(user);

        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertEquals("User already exists", response.getBody());

        Mockito.verify(repo, Mockito.never()).save(Mockito.any());
    }

   
    @Test
    void testLoginSuccess() {

        String email = "amit@lpu.com";
        String password = "pass123";

        User user = new User();
        user.setEmail(email);
        user.setRole("USER");

        // authentication passes (no exception)
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(null);

        Mockito.when(repo.findByEmail(email))
                .thenReturn(Optional.of(user));

        Mockito.when(jwtUtil.generateToken(email, "USER"))
                .thenReturn("mocked-jwt-token");

        ResponseEntity<?> response = userController.login(email, password);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("mocked-jwt-token", response.getBody());

        Mockito.verify(jwtUtil).generateToken(email, "USER");
    }

 
    @Test
    void testLoginInvalidCredentials() {

        String email = "amit@lpu.com";
        String password = "wrongpass";

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        ResponseEntity<?> response = userController.login(email, password);

        Assertions.assertEquals(401, response.getStatusCodeValue());
        Assertions.assertEquals("Invalid credentials", response.getBody());
    }
}
