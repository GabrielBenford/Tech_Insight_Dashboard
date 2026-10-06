package com.example.TechInsightDashBoard.DTO.TechDTO;

import java.time.Instant;

public record SearchHistoryResponseDTO(
        Long id,
        Long technologyId,
        String technologyName,
        String provider,
        String query,
        Integer resultCount,
        Instant searchedAt) {
}
