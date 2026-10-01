package com.example.TechInsightDashBoard.External.Adzuna;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AdzunaJobsResponseDTO(String id, String title, String description, String created, @JsonProperty("redirect_url") String redirectUrl,
                                    AdzunaCompanyDTO company, AdzunaLocationDTO location, @JsonProperty("salary_min") BigDecimal salaryMin, @JsonProperty("salary_max") BigDecimal salaryMax,
                                    @JsonProperty("contract_time") String contractTime, @JsonProperty("contract_type") String contractType) {
}
