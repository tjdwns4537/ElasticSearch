package com.example.elasticsearch.article.domain;

import com.example.elasticsearch.helper.Indices;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyIndexCreator {

    private final RestHighLevelClient client;

    @Autowired
    public MyIndexCreator(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex() throws Exception {
        CreateIndexRequest request = new CreateIndexRequest(Indices.ARTICLE_INDEX);
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("_doc");
            {
                builder.startObject("properties");
                {
                    builder.startObject("my_field");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "sentiment");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping("_doc", builder);
        client.indices().create(request, RequestOptions.DEFAULT);
    }
}
