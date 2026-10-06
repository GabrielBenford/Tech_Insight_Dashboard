package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserResponseDTO;
import com.example.TechInsightDashBoard.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock UserService userService;
    @InjectMocks UserController controller;

    @Test
    void returnsCreatedResponseForNewUser() {
        UserRequestDTO request = new UserRequestDTO("Ana", "ana@example.com", "password");
        UserResponseDTO response = new UserResponseDTO(3L, "Ana", "ana@example.com");
        when(userService.createUser(request)).thenReturn(response);

        var result = controller.createUser(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void getsUserById() {
        UserResponseDTO response = new UserResponseDTO(3L, "Ana", "ana@example.com");
        when(userService.findUserById(3L)).thenReturn(response);

        assertEquals(response, controller.getUserById(3L));
    }

    @Test
    void deletesUserAndReturnsNoContent() {
        var result = controller.deleteUser(3L);
        assertEquals(204, result.getStatusCode().value());
        verify(userService).deleteUser(3L);
    }
}
