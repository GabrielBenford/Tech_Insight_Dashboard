package com.example.TechInsightDashBoard.External.Adzuna;

import com.example.TechInsightDashBoard.DTO.AdzunaDTO.AdzunaSearchingResponseDTO;
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
            @RequestParam("what_and") String whatAnd,
            @RequestParam("what_exclude") String whatExclude,
            @RequestParam("where") String where,
            @RequestParam("max_days_old") int maxDaysOld
    );
}
