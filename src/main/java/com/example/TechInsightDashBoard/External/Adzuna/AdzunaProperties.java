package com.example.TechInsightDashBoard.External.Adzuna;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adzuna")
public record AdzunaProperties(String appId, String appKey) {
}
