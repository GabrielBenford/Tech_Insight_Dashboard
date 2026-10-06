package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Repository.UserRepository;
import com.example.TechInsightDashBoard.exception.EmailAlreadyInUseException;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;

    @Test
    void createsTrimmedUserAndEncodesPassword() {
        var request = new UserRequestDTO("  Ana  ", " ana@example.com ", "plain-password");
        when(userRepository.findByEmailIgnoreCase("ana@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain-password")).thenReturn("hashed-password");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(7L);
            return user;
        });

        var response = userService.createUser(request);

        assertEquals(7L, response.id());
        assertEquals("Ana", response.username());
        assertEquals("ana@example.com", response.email());
        verify(passwordEncoder).encode("plain-password");
        verify(userRepository).save(argThat(user -> user.getPassword().equals("hashed-password")));
    }

    @Test
    void rejectsDuplicateEmailIgnoringCase() {
        when(userRepository.findByEmailIgnoreCase("ana@example.com"))
                .thenReturn(Optional.of(user(1L, "ana@example.com")));

        assertThrows(EmailAlreadyInUseException.class,
                () -> userService.createUser(request(" Ana ", " ana@example.com ")));
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void findsUserAndMapsDisplayName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1L, "ana@example.com")));

        var result = userService.findUserById(1L);

        assertEquals("Ana", result.username());
        assertEquals("ana@example.com", result.email());
    }

    @Test
    void throwsWhenFindingMissingUser() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(404L));
    }

    @Test
    void updatesUserAndAllowsKeepingOwnEmail() {
        UserEntity existing = user(1L, "ana@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmailIgnoreCase("ana@example.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("new-password")).thenReturn("new-hash");

        var result = userService.updateUser(1L,
                new UserRequestDTO(" Ana Maria ", " ana@example.com ", "new-password"));

        assertEquals("Ana Maria", result.username());
        assertEquals("new-hash", existing.getPassword());
        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsChangingEmailToAnotherUsersEmail() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1L, "ana@example.com")));
        when(userRepository.findByEmailIgnoreCase("bia@example.com"))
                .thenReturn(Optional.of(user(2L, "bia@example.com")));

        assertThrows(EmailAlreadyInUseException.class,
                () -> userService.updateUser(1L, request("Ana", "bia@example.com")));
    }

    @Test
    void updateRequiresExistingUser() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(404L, request("Ana", "ana@example.com")));
        verify(userRepository, never()).findByEmailIgnoreCase(any());
    }

    @Test
    void deletesExistingUserAndRejectsMissingUser() {
        UserEntity existing = user(1L, "ana@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        userService.deleteUser(1L);
        verify(userRepository).delete(existing);

        when(userRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(9L));
    }

    private static UserRequestDTO request(String name, String email) {
        return new UserRequestDTO(name, email, "password");
    }

    private static UserEntity user(Long id, String email) {
        return UserEntity.builder().id(id).username("Ana").email(email).password("hash").build();
    }
}
