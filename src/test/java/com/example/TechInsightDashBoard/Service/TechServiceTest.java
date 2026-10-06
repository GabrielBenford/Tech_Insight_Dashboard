package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.DTO.TechDTO.TechRequestDTO;
import com.example.TechInsightDashBoard.Entity.TechEntity;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Repository.TechRepository;
import com.example.TechInsightDashBoard.Repository.UserRepository;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechServiceTest {

    @Mock TechRepository techRepository;
    @Mock UserRepository userRepository;
    @InjectMocks TechService techService;

    @Test
    void createsTrimmedTechnologyForUser() {
        UserEntity owner = user(5L);
        when(userRepository.findById(5L)).thenReturn(Optional.of(owner));
        when(techRepository.save(any(TechEntity.class))).thenAnswer(invocation -> {
            TechEntity tech = invocation.getArgument(0);
            tech.setId(12L);
            return tech;
        });

        var response = techService.create(5L, new TechRequestDTO(" Java ", " Learning "));

        assertEquals(12L, response.id());
        assertEquals("Java", response.techName());
        assertEquals("Learning", response.history());
        assertEquals(5L, response.userId());
    }

    @Test
    void cannotCreateTechnologyForMissingUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> techService.create(99L, new TechRequestDTO("Java", "Learning")));
        verify(techRepository, never()).save(any());
    }

    @Test
    void listsTechnologiesForUser() {
        when(techRepository.findAllByUserIdOrderByTechNameAsc(5L))
                .thenReturn(List.of(tech(1L, "Java", 5L), tech(2L, "Kotlin", 5L)));

        var results = techService.findAllByUser(5L);

        assertEquals(List.of("Java", "Kotlin"), results.stream().map(r -> r.techName()).toList());
    }

    @Test
    void findsTechnologyOnlyForItsOwner() {
        when(techRepository.findById(2L)).thenReturn(Optional.of(tech(2L, "Java", 5L)));
        assertEquals("Java", techService.findById(5L, 2L).techName());
        assertThrows(ResourceNotFoundException.class, () -> techService.findById(6L, 2L));
    }

    @Test
    void updatesOwnedTechnologyAndRejectsForeignTechnology() {
        TechEntity technology = tech(2L, "Java", 5L);
        when(techRepository.findById(2L)).thenReturn(Optional.of(technology));

        var result = techService.update(5L, 2L, new TechRequestDTO(" Kotlin ", " Updated "));
        assertEquals("Kotlin", result.techName());
        assertEquals("Updated", result.history());
        assertThrows(ResourceNotFoundException.class,
                () -> techService.update(7L, 2L, new TechRequestDTO("Go", "History")));
    }

    @Test
    void deletesOwnedTechnologyAndRejectsForeignTechnology() {
        TechEntity technology = tech(2L, "Java", 5L);
        when(techRepository.findById(2L)).thenReturn(Optional.of(technology));

        techService.delete(5L, 2L);
        verify(techRepository).delete(technology);
        assertThrows(ResourceNotFoundException.class, () -> techService.delete(9L, 2L));
    }

    private static UserEntity user(Long id) {
        return UserEntity.builder().id(id).username("Ana").email("ana@example.com").password("hash").build();
    }

    private static TechEntity tech(Long id, String name, Long userId) {
        return TechEntity.builder().id(id).techName(name).history("learning")
                .user(user(userId)).build();
    }
}
