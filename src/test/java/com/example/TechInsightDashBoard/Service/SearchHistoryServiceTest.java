package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.Entity.SearchHistoryEntity;
import com.example.TechInsightDashBoard.Entity.TechEntity;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Repository.SearchHistoryRepository;
import com.example.TechInsightDashBoard.Repository.TechRepository;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchHistoryServiceTest {

    @Mock SearchHistoryRepository historyRepository;
    @Mock TechRepository techRepository;
    @InjectMocks SearchHistoryService service;

    @Test
    void skipsRecordingWhenSearchHasNoUserOrTechnologyLink() {
        service.recordIfLinked(null, null, "GITHUB", "java", 20);
        verifyNoInteractions(techRepository, historyRepository);
    }

    @Test
    void requiresBothLinkIds() {
        var exception = assertThrows(ResponseStatusException.class,
                () -> service.recordIfLinked(1L, null, "GITHUB", "java", 20));
        assertEquals(400, exception.getStatusCode().value());
        verifyNoInteractions(historyRepository);
    }

    @Test
    void recordsSuccessfulSearchUnderUsersTechnology() {
        TechEntity technology = technology(4L, 2L);
        when(techRepository.findById(4L)).thenReturn(Optional.of(technology));
        when(historyRepository.save(any(SearchHistoryEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.recordIfLinked(2L, 4L, "GITHUB_REPOSITORIES", "java", 42);

        ArgumentCaptor<SearchHistoryEntity> captor = ArgumentCaptor.forClass(SearchHistoryEntity.class);
        verify(historyRepository).save(captor.capture());
        SearchHistoryEntity saved = captor.getValue();
        assertSame(technology, saved.getTechnology());
        assertEquals("GITHUB_REPOSITORIES", saved.getProvider());
        assertEquals("java", saved.getQuery());
        assertEquals(42, saved.getResultCount());
        assertNotNull(saved.getSearchedAt());
    }

    @Test
    void doesNotRecordSearchForMissingOrAnotherUsersTechnology() {
        when(techRepository.findById(4L)).thenReturn(Optional.of(technology(4L, 3L)));

        assertThrows(ResourceNotFoundException.class,
                () -> service.recordIfLinked(2L, 4L, "ADZUNA_JOBS", "java", 1));
        verifyNoInteractions(historyRepository);
    }

    @Test
    void returnsTechnologyHistoryNewestFirstAsResponseDtos() {
        Instant searchedAt = Instant.parse("2026-01-01T10:00:00Z");
        TechEntity technology = technology(4L, 2L);
        SearchHistoryEntity history = SearchHistoryEntity.builder().id(8L).technology(technology)
                .provider("ADZUNA_JOBS").query("what=java").resultCount(3).searchedAt(searchedAt).build();
        when(techRepository.existsByIdAndUserId(4L, 2L)).thenReturn(true);
        when(historyRepository.findAllByTechnologyIdAndTechnologyUserIdOrderBySearchedAtDesc(4L, 2L))
                .thenReturn(List.of(history));

        var results = service.findByUserAndTechnology(2L, 4L);

        assertEquals(1, results.size());
        assertEquals(4L, results.getFirst().technologyId());
        assertEquals("Java", results.getFirst().technologyName());
        assertEquals("ADZUNA_JOBS", results.getFirst().provider());
        assertEquals("what=java", results.getFirst().query());
        assertEquals(3, results.getFirst().resultCount());
        assertEquals(searchedAt, results.getFirst().searchedAt());
    }

    @Test
    void rejectsHistoryLookupForTechnologyNotOwnedByUser() {
        when(techRepository.existsByIdAndUserId(4L, 2L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.findByUserAndTechnology(2L, 4L));
        verifyNoInteractions(historyRepository);
    }

    private static TechEntity technology(Long id, Long userId) {
        UserEntity user = UserEntity.builder().id(userId).username("Ana")
                .email("ana@example.com").password("hash").build();
        return TechEntity.builder().id(id).techName("Java").history("learning").user(user).build();
    }
}
