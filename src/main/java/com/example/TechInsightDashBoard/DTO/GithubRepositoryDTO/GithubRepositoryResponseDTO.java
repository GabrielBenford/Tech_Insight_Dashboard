package com.example.TechInsightDashBoard.DTO.GithubRepositoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepositoryResponseDTO(Long id, String name, @JsonProperty("full_name") String fullName, String description,
                                          @JsonProperty("html_url") String html_url, String language,
                                          @JsonProperty("stargazers_count") int stargazersCount, @JsonProperty("forks_count") int forksCount,
                                           @JsonProperty("created_at") String createdAt,
                                           @JsonProperty("private") boolean isPrivate) {
}
