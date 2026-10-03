package com.example.TechInsightDashBoard.External.GithubRepositories;
import com.example.TechInsightDashBoard.External.GithubTopics.GithubTopicClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class GithubClientConfig {

   @Bean
   public HttpServiceProxyFactory httpServiceProxyFactory() {

       WebClient webClient = WebClient.builder()
               .baseUrl("https://api.github.com")
               .build();

       return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
               .build();
    }

    @Bean
    public GithubRepositoryClient githubClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(GithubRepositoryClient.class);
    }

    @Bean
    public GithubTopicClient githubTopicClient(HttpServiceProxyFactory httpServiceProxyFactory) {
          return httpServiceProxyFactory.createClient(GithubTopicClient.class);
     }
}
