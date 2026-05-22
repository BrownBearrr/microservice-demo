package com.example.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced // spring sẽ tự động load balance giữa các instance của service khi gọi qua WebClient
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
