package com.example.articles.controller;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EsRestTest {

    private RestHighLevelClient client;
    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://localhost:9200"))
        );
    }
    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

    @Test
    void test() throws IOException {
        System.out.println(client);
    }

    //判断用户索引库是否存在
    @Test
    void getUser() throws IOException {
        //TODO
        //1.加入用户索引库参数
        GetIndexRequest request = new GetIndexRequest("users");
        //2.发送请求
        boolean exists = client.indices().exists(request,RequestOptions.DEFAULT);
        System.out.println("exists = " + exists);
    }
    //删除索引库
    @Test
    void deleteUser() throws IOException {
        //TODO
        //1.加入用户索引库参数
        DeleteIndexRequest request = new DeleteIndexRequest("users");
        //2.发送请求
        client.indices().delete(request,RequestOptions.DEFAULT);
    }

}
