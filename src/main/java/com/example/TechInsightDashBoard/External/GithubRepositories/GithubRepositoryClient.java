package com.example.TechInsightDashBoard.External.GithubRepositories;
import com.example.TechInsightDashBoard.DTO.GithubRepositoryDTO.GithubSearchResponseDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface GithubRepositoryClient {

     @GetExchange("/search/repositories")
     GithubSearchResponseDTO getRepositories(@RequestHeader(value = "X-GitHub-Api-Version", defaultValue = "2026-03-10") String apiVersion,
                                             @RequestParam("q") String query,
                                             @RequestParam(name = "sort", defaultValue = "forks") String sort,
                                             @RequestParam(name = "order", defaultValue = "desc") String order,
                                             @RequestParam(name = "per_page", defaultValue = "10") int perPage,
                                             @RequestParam(name = "page", defaultValue = "1") int page);

}
