package com.example.TechInsightDashBoard.DTO.AdzunaDTO;

import java.util.List;

public record AdzunaSearchingResponseDTO(
        Integer count,
        String mean,
        List<AdzunaJobsResponseDTO> results
) {
}
