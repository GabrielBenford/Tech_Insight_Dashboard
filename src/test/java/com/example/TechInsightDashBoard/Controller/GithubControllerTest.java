package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.GithubRepositoryDTO.GithubSearchResponseDTO;
import com.example.TechInsightDashBoard.DTO.GithubTopicsDTO.GithubSearchForTopicsDTO;
import com.example.TechInsightDashBoard.External.GithubRepositories.GithubRepositoryClient;
import com.example.TechInsightDashBoard.External.GithubTopics.GithubTopicClient;
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
class GithubControllerTest {

    @Mock GithubRepositoryClient repositoryClient;
    @Mock GithubTopicClient topicClient;
    @Mock SearchHistoryService searchHistoryService;
    private GithubController controller;

    @BeforeEach
    void setUp() {
        controller = new GithubController(repositoryClient, topicClient, searchHistoryService);
    }

    @Test
    void forwardsRepositorySearchAndRecordsLinkedSearch() {
        var response = new GithubSearchResponseDTO(20, false, List.of());
        when(repositoryClient.getRepositories(null, "java", "stars", "desc", 5, 2)).thenReturn(response);

        var result = controller.getUserRepositories("java", "stars", "desc", 2, 5, 7L, 8L);

        assertEquals(response, result.getBody());
        verify(searchHistoryService).recordIfLinked(7L, 8L, "GITHUB_REPOSITORIES", "java", 20);
    }

    @Test
    void repositorySearchWithoutLinkIdsDoesNotCreateHistory() {
        when(repositoryClient.getRepositories(null, "java", null, null, 10, 1))
                .thenReturn(new GithubSearchResponseDTO(0, false, List.of()));

        controller.getUserRepositories("java", null, null, 1, 10, null, null);

        verify(searchHistoryService).recordIfLinked(null, null, "GITHUB_REPOSITORIES", "java", 0);
    }

    @Test
    void forwardsTopicSearchAndRecordsIt() {
        var response = new GithubSearchForTopicsDTO(4, false, List.of());
        when(topicClient.getTopics(null, "spring", 3, 1)).thenReturn(response);

        var result = controller.getTopics("spring", 3, 1, 7L, 8L);

        assertEquals(response, result.getBody());
        verify(searchHistoryService).recordIfLinked(7L, 8L, "GITHUB_TOPICS", "spring", 4);
    }
}
