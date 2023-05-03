package com.example.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.port}")
    public String port;

    @Value("${elasticsearch.host}")
    public String host;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        // http port 와 통신할 주소
        ClientConfiguration configuration =
                ClientConfiguration.builder().connectedTo(host+":"+port).build();

        RestHighLevelClient client = RestClients.create(configuration).rest();

        return client;
    }
}
