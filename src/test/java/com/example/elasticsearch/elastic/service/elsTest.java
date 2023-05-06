package com.example.elasticsearch.elastic.service;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class elsTest {

    @Test
    public void testCreateIndex() throws IOException {
        String indexName = "my_index";

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        try {
            DeleteIndexRequest request = new DeleteIndexRequest("my_index");
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                // The index doesn't exist, so we don't need to delete it
            } else {
                throw e;
            }
        }

        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .settings(Settings.builder().put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 0));

        XContentBuilder builder = jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("title")
                .field("type", "text")
                .endObject()
                .startObject("description")
                .field("type", "text")
                .endObject()
                .endObject()
                .endObject();

        request.mapping(builder);

        client.indices().create(request, RequestOptions.DEFAULT);

        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);

        boolean indexExists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        assertTrue(indexExists, "Index should exist");

        System.out.println(indexExists);

        client.close();
    }
}
