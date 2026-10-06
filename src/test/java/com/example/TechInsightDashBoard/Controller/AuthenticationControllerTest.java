package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.AuthDTO.LoginDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Service.TokenService;
import com.example.TechInsightDashBoard.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock TokenService tokenService;
    @Mock UserService userService;
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthenticationController(authenticationManager, tokenService, userService);
    }

    @Test
    void loginAuthenticatesCredentialsAndReturnsToken() {
        UserEntity user = UserEntity.builder().id(3L).username("Ana")
                .email("ana@example.com").password("hash").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.creatingToken(user)).thenReturn("jwt-token");

        var response = controller.login(new LoginDTO("ana@example.com", "password"));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("jwt-token", response.getBody());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).creatingToken(user);
    }

    @Test
    void registrationDelegatesToUserService() {
        var response = controller.register(new UserRequestDTO("Ana", "ana@example.com", "password"));

        assertEquals(201, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody());
        verify(userService).createUser(any(UserRequestDTO.class));
    }
}
