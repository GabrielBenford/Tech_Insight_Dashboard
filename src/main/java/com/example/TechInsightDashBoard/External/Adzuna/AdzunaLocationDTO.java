package com.example.TechInsightDashBoard.External.Adzuna;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AdzunaLocationDTO(@JsonProperty("display_name") String displayName, @JsonProperty("area") String[] area) {
}
