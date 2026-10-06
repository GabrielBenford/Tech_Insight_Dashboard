package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.TechDTO.SearchHistoryResponseDTO;
import com.example.TechInsightDashBoard.DTO.TechDTO.TechRequestDTO;
import com.example.TechInsightDashBoard.DTO.TechDTO.TechResponseDTO;
import com.example.TechInsightDashBoard.Service.SearchHistoryService;
import com.example.TechInsightDashBoard.Service.TechService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechControllerTest {

    @Mock TechService techService;
    @Mock SearchHistoryService historyService;
    @InjectMocks TechController controller;

    @Test
    void createsTechnologyWithCreatedStatus() {
        TechRequestDTO request = new TechRequestDTO("Java", "Learning");
        TechResponseDTO response = new TechResponseDTO(8L, "Java", "Learning", 2L);
        when(techService.create(2L, request)).thenReturn(response);

        var result = controller.create(2L, request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void returnsSearchHistoryForUserTechnology() {
        var history = new SearchHistoryResponseDTO(1L, 8L, "Java", "GITHUB_REPOSITORIES",
                "java", 25, Instant.parse("2026-01-01T10:00:00Z"));
        when(historyService.findByUserAndTechnology(2L, 8L)).thenReturn(List.of(history));

        assertEquals(List.of(history), controller.findSearchHistory(2L, 8L));
    }

    @Test
    void deleteReturnsNoContentAndDelegates() {
        var result = controller.delete(2L, 8L);
        assertEquals(204, result.getStatusCode().value());
        verify(techService).delete(2L, 8L);
    }
}
