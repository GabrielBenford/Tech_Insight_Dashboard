package com.example.TechInsightDashBoard.DTO.GithubTopicsDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubTopicResponseDTO(String name, @JsonProperty("display_name") String display_name, String description, String created_by,
                                     String released, @JsonProperty("created_at") String createdAt) {
}
