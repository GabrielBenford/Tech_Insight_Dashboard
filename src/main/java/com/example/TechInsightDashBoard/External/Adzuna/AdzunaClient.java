package com.example.TechInsightDashBoard.External.Adzuna;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface AdzunaClient {

    @GetExchange("/jobs/{country}/search/{page}")
    AdzunaSearchingResponseDTO searchJobs(
            @PathVariable("country") String country,
            @PathVariable("page") int page,
            @RequestParam("results_per_page") int resultsPerPage,
            @RequestParam("app_id") String appId,
            @RequestParam("app_key") String appKey,
            @RequestParam("what") String what,
            @RequestParam("where") String where,
            @RequestParam("location0") String location0,
            @RequestParam("distance") int distance);
}
