package com.example.TechInsightDashBoard.Controller;


import com.example.TechInsightDashBoard.DTO.GithubTopicsDTO.GithubSearchForTopicsDTO;
import com.example.TechInsightDashBoard.External.GithubRepositories.GithubRepositoryClient;
import com.example.TechInsightDashBoard.DTO.GithubRepositoryDTO.GithubSearchResponseDTO;
import com.example.TechInsightDashBoard.External.GithubTopics.GithubTopicClient;
import com.example.TechInsightDashBoard.Service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubRepositoryClient githubClient;
    private final GithubTopicClient githubTopicClient;
    private final SearchHistoryService searchHistoryService;


    @GetMapping("/search/repositories")
    public ResponseEntity<GithubSearchResponseDTO> getUserRepositories(String q, String sort, String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long technologyId) {

        var githubResponse = githubClient.getRepositories(null, q, sort, order, perPage, page);
        searchHistoryService.recordIfLinked(userId, technologyId, "GITHUB_REPOSITORIES", q,
                githubResponse.totalCount());

        return ResponseEntity.ok(githubResponse);
    }


    @GetMapping("/search/topics")
    public ResponseEntity<GithubSearchForTopicsDTO> getTopics(String q,
                                                              @RequestParam(name = "per_page", defaultValue = "10") int perPage,
                                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                                              @RequestParam(required = false) Long userId,
                                                              @RequestParam(required = false) Long technologyId) {


        var githubResponse = githubTopicClient.getTopics(null, q, perPage, page);
        searchHistoryService.recordIfLinked(userId, technologyId, "GITHUB_TOPICS", q,
                githubResponse.totalCount());

        return ResponseEntity.ok(githubResponse);
    }

}
