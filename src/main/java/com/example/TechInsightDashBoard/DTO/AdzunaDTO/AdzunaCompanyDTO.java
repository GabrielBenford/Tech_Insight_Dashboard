package com.example.TechInsightDashBoard.DTO.AdzunaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AdzunaCompanyDTO(@JsonProperty("display_name") String displayName, @JsonProperty("canonical_name") String cannonicalName, Integer count, @JsonProperty("average_salary") BigDecimal averageSalary) {
}
