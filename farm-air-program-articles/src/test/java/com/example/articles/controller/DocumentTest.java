package com.example.articles.controller;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.articles.FarmAirProgramArticlesApplication;
import com.example.articles.dao.ArticlesDAO;
import com.example.articles.entity.Articles;
import com.example.articles.service.IArticlesService;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = FarmAirProgramArticlesApplication.class)
public class DocumentTest {
    private RestHighLevelClient client;
    @Resource
    private IArticlesService articlesService;

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://localhost:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
    @Test
    void getArticleById() throws IOException {
        Articles article = articlesService.getById(1);
        System.out.println(article.toString());
    }

    /**
     * es索引库mapping字段名和sql字段名也一样，可以自动转化
     * 创建一条文档
     * @throws IOException
     */
    @Test
    void createArticlesTOES() throws IOException {
        //获取数据，转化数据
        Articles article = articlesService.getById(1);
        ArticlesDAO articlesDAO = BeanUtil.copyProperties(article, ArticlesDAO.class);
        //序列化
        String doc = JSONUtil.toJsonStr(articlesDAO);

        //准备创建请求
        IndexRequest request = new IndexRequest("articles").id(article.getId());
        //准备json文档
        request.source(doc, XContentType.JSON);
        //发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 根据id = 1, 获取es中的articles索引库中
     * 如果查不到数据则为空
     * @throws IOException
     */
    @Test
    void getArticleByESId() throws IOException {
        //1.创建请求
        GetRequest request = new GetRequest("articles").id("1");
        //2.发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        ArticlesDAO articlesDAO = JSONUtil.toBean(sourceAsString, ArticlesDAO.class);
        System.out.println("articlesDAO:\n"+articlesDAO.toString());
    }

    /**
     * 根据id,删除articles索引库中的文档
     */
    @Test
    void deleteArticleById() throws IOException {
        //1.创建请求
        DeleteRequest request = new DeleteRequest("articles").id("1");
        //2.发送请求
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }
    /**
     * 修改,如果不存在则不支持修改
     */
    @Test
    void updateArticlesByESId() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("articles", "1");
        updateRequest.doc("title","测试局部修改");
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
        getArticleByESId();
    }

    /**
     * 批量修改，从articles表中获取status为1的数据，并且按照分页方式，批量插入到es中
     */
    @Test
    void batchUpdateArticles() throws IOException {
        int pageSize = 20;
        int pageNo = 1;
        while (true){
            Page<Articles> page = new Page<>(pageNo, pageSize);
            Page<Articles> pages = articlesService.page(page, new LambdaQueryWrapper<Articles>().eq(Articles::getStatus, 1));
            //非空校验
            List<Articles> articles = pages.getRecords();

            if (CollUtil.isEmpty(articles)){
                break;
            }
            // 调试信息：打印当前页码、每页大小和总记录数
            System.out.println("Current Page: " + pageNo + ", Page Size: " + pageSize + ", Total Records: " + page.getTotal());
            //批量存储插入
            BulkRequest request = new BulkRequest("articles");
            for(Articles article : articles){
                //类型转化和序列化
                ArticlesDAO articlesDAO = BeanUtil.copyProperties(article, ArticlesDAO.class);
                String json = JSONUtil.toJsonStr(articlesDAO);
                request.add(new IndexRequest()
                        .id(articlesDAO.getId())
                        .source(json, XContentType.JSON));
            }
            //发送请求：
            client.bulk(request, RequestOptions.DEFAULT);
            pageNo++;
        }
    }

}
