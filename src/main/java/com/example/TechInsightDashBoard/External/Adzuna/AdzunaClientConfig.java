package com.example.TechInsightDashBoard.External.Adzuna;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AdzunaClientConfig {

    @Bean
    public HttpServiceProxyFactory adzunaHttpServiceProxyFactory() {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.adzuna.com/v1/api")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
    }
    @Bean
    public AdzunaClient adzunaJobsService() {
        return adzunaHttpServiceProxyFactory().createClient(AdzunaClient.class);
    }
}
