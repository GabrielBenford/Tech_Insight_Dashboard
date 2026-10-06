package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.AdzunaDTO.AdzunaProperties;
import com.example.TechInsightDashBoard.DTO.AdzunaDTO.AdzunaSearchingResponseDTO;
import com.example.TechInsightDashBoard.External.Adzuna.AdzunaClient;
import com.example.TechInsightDashBoard.Service.SearchHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdzunaControllerTest {

    @Mock AdzunaClient adzunaClient;
    @Mock SearchHistoryService historyService;
    private AdzunaController controller;

    @BeforeEach
    void setUp() {
        controller = new AdzunaController(adzunaClient,
                new AdzunaProperties("app-id", "app-key"), historyService);
    }

    @Test
    void forwardsSearchWithServerCredentialsAndRecordsItForTechnology() {
        var response = new AdzunaSearchingResponseDTO(9, null, List.of());
        when(adzunaClient.searchJobs("br", 1, 10, "app-id", "app-key",
                "java", "developer", "senior", "Sao Paulo", 30)).thenReturn(response);

        var result = controller.searchForJobs("br", 1, 10, "java", "developer",
                "senior", "Sao Paulo", 30, 4L, 5L);

        assertEquals(response, result.getBody());
        verify(historyService).recordIfLinked(4L, 5L, "ADZUNA_JOBS",
                "what=java; what_and=developer; what_exclude=senior; where=Sao Paulo", 9);
    }

    @Test
    void usesCurrentPageResultsWhenAdzunaCountIsNull() {
        var response = new AdzunaSearchingResponseDTO(null, null, List.of());
        when(adzunaClient.searchJobs("br", 1, 10, "app-id", "app-key",
                "java", "developer", "senior", "Dhaka", 30)).thenReturn(response);

        controller.searchForJobs("br", 1, 10, "java", "developer", "senior", "Dhaka", 30, 4L, 5L);

        verify(historyService).recordIfLinked(4L, 5L, "ADZUNA_JOBS",
                "what=java; what_and=developer; what_exclude=senior; where=Dhaka", 0);
    }
}
