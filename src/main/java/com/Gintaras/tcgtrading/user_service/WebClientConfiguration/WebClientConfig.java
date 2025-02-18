package com.Gintaras.tcgtrading.user_service.WebClientConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class WebClientConfig {


    @Value("${trade.service.url}")
    private String tradeServiceUrl;


    @Bean("trade")
    public WebClient tradeService(WebClient.Builder builder) {
        return builder.baseUrl(tradeServiceUrl).build();
    }
}
