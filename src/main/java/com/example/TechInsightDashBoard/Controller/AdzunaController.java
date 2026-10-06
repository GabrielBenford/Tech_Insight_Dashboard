package com.example.TechInsightDashBoard.Controller;


import com.example.TechInsightDashBoard.External.Adzuna.AdzunaClient;
import com.example.TechInsightDashBoard.DTO.AdzunaDTO.AdzunaProperties;
import com.example.TechInsightDashBoard.DTO.AdzunaDTO.AdzunaSearchingResponseDTO;
import com.example.TechInsightDashBoard.Service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adzuna")
@RequiredArgsConstructor
public class AdzunaController {

    private static final Logger log = LoggerFactory.getLogger(AdzunaController.class);

    private final AdzunaClient adzunaClient;
    private final AdzunaProperties adzunaProperties;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/jobs/{country}/search/{page}")
    public ResponseEntity<AdzunaSearchingResponseDTO> searchForJobs(
            @PathVariable("country") String country,
            @PathVariable("page") int page,
            @RequestParam(name = "results_per_page", defaultValue = "10") int resultsPerPage,
            @RequestParam("what") String what,
            @RequestParam("what_and") String whatAnd,
            @RequestParam("what_exclude") String whatExclude,
            @RequestParam("where") String where,
            @RequestParam(name = "max_days_old", defaultValue = "30") int maxDaysOld,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long technologyId) {

        AdzunaSearchingResponseDTO response= adzunaClient.searchJobs(
                    country, page, resultsPerPage,
                    adzunaProperties.appId(), adzunaProperties.appKey(),
                    what, whatAnd, whatExclude, where, maxDaysOld);

        searchHistoryService.recordIfLinked(userId, technologyId, "ADZUNA_JOBS",
                "what=" + what + "; what_and=" + whatAnd + "; what_exclude=" + whatExclude
                        + "; where=" + where,
                response.count() != null ? response.count() : response.results().size());
        return ResponseEntity.ok(response);
    }


}
