package com.example.TechInsightDashBoard.DTO.GithubTopicsDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GithubSearchForTopicsDTO(
       @JsonProperty("total_count") Integer totalCount,
        @JsonProperty("incomplete_results") Boolean incompleteResults,
        List<GithubTopicResponseDTO> items
) {
}
