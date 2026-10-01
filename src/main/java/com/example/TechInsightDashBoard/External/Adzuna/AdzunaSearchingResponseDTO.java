package com.example.TechInsightDashBoard.External.Adzuna;

import java.util.List;

public record AdzunaSearchingResponseDTO(
        int count,
        String mean,
        List<AdzunaJobsResponseDTO> results
) {
}
