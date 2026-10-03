package com.example.TechInsightDashBoard.DTO.AdzunaDTO;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adzuna")
public record AdzunaProperties(String appId, String appKey) {
}
