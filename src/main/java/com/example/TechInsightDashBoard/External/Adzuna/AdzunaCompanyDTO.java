package com.example.TechInsightDashBoard.External.Adzuna;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AdzunaCompanyDTO(@JsonProperty("display_name") String displayName, @JsonProperty("canonical_name") String cannonicalName, int count, @JsonProperty("average_salary") BigDecimal averageSalary) {
}
