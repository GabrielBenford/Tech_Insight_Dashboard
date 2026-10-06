package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplementationTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserDetailsImplementation service;

    @Test
    void findsUserByEmailForSpringSecurity() {
        UserEntity user = UserEntity.builder().id(1L).username("Ana")
                .email("ana@example.com").password("hash").build();
        when(userRepository.findByEmail("ana@example.com")).thenReturn(user);

        assertSame(user, service.loadUserByUsername("ana@example.com"));
    }

    @Test
    void throwsWhenEmailDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com"));
    }
}
