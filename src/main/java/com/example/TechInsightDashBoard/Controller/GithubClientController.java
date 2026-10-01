package com.example.TechInsightDashBoard.Controller;


import com.example.TechInsightDashBoard.DTO.GithubRepositoryResponseDTO;
import com.example.TechInsightDashBoard.External.GithubClient;
import com.example.TechInsightDashBoard.External.GithubSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubClientController {

    private final GithubClient githubClient;

    @GetMapping("/search/repositories")
    public ResponseEntity<GithubSearchResponseDTO> getUserRepositories(String q, String sort, String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage) {

        var githubResponse = githubClient.getRepositories(null, q, sort, order, perPage, page);

        return ResponseEntity.ok(githubResponse);
    }

}
