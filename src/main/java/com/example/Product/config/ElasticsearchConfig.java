package com.example.Product.config;


import jakarta.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

    @PreDestroy
    public void closeClient() throws IOException {
        client().close();
    }
}

