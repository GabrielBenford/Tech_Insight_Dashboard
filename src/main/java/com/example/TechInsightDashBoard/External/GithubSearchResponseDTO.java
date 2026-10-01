package com.example.TechInsightDashBoard.External;

import com.example.TechInsightDashBoard.DTO.GithubRepositoryResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GithubSearchResponseDTO(
        @JsonProperty("total_count") int totalCount,
        @JsonProperty("incomplete_results") boolean incompleteResults,
        List<GithubRepositoryResponseDTO> items
) {
}
