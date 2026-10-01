package com.example.TechInsightDashBoard.Controller;


import com.example.TechInsightDashBoard.External.Adzuna.AdzunaClient;
import com.example.TechInsightDashBoard.External.Adzuna.AdzunaProperties;
import com.example.TechInsightDashBoard.External.Adzuna.AdzunaSearchingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adzuna")
@RequiredArgsConstructor
public class AdzunaClientController {

    private static final Logger log = LoggerFactory.getLogger(AdzunaClientController.class);

    private final AdzunaClient adzunaClient;
    private final AdzunaProperties adzunaProperties;

    @GetMapping("/jobs/{country}/search/{page}")
    public ResponseEntity<AdzunaSearchingResponseDTO> searchForJobs(
            @PathVariable("country") String country,
            @PathVariable("page") int page,
            @RequestParam(name = "results_per_page", defaultValue = "10") int resultsPerPage,
            @RequestParam("what") String what,
            @RequestParam("where") String where,
            @RequestParam("location0") String location0,
            @RequestParam(name = "distance", defaultValue = "50") int distance) {

        AdzunaSearchingResponseDTO response;
        try {
            response = adzunaClient.searchJobs(
                    country, page, resultsPerPage,
                    adzunaProperties.appId(), adzunaProperties.appKey(),
                    what, where, location0, distance);
        } catch (WebClientResponseException exception) {
            log.error("Adzuna respondeu HTTP {}. Corpo da resposta: {}",
                    exception.getStatusCode(), exception.getResponseBodyAsString());
            throw exception;
        }

        return ResponseEntity.ok(response);
    }


}
