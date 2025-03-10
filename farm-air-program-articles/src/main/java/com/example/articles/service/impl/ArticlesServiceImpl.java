package com.example.articles.service.impl;

import cn.hutool.db.PageResult;
import com.example.articles.dto.ArticlesEsDTO;
import com.example.articles.entity.Articles;
import com.example.articles.mapper.ArticlesMapper;
import com.example.articles.service.IArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.articles.vo.ArticlesSearchRespVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


/**
 * <p>
 * 文章信息表 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles> implements IArticlesService {

    @Resource
    private RestHighLevelClient esClient;

    private static final String INDEX_NAME = "articles_index";

    public void saveOrUpdateArticleToEs(ArticlesEsDTO articleEsDTO) throws IOException {
        IndexRequest request = new IndexRequest(INDEX_NAME);
        request.id(articleEsDTO.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, Object> map = objectMapper.convertValue(articleEsDTO, new TypeReference<Map<String, Object>>() {});
        request.source(map);
        esClient.index(request, RequestOptions.DEFAULT);
    }

    public void deleteArticleFromEs(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, id);
        esClient.delete(request, RequestOptions.DEFAULT);
    }




//    public PageResult<ArticlesSearchRespVO> searchWithCache(String keyword, int page, int size) throws IOException {
//        String cacheKey = "search:articles:" + keyword + ":" + page + ":" + size;
////        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
////        if (cacheValue != null) {
////            return JSON.parseObject(cacheValue, PageResult.class);
////        }
//
//        PageResult<ArticlesSearchRespVO> result = searchService.searchArticles(keyword, page, size);
//
//        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(result), 5, TimeUnit.MINUTES);
//
//        return result;
//    }


    @Override
    public Articles getArticlesById(String id) {
        return this.getById(id);
    }
}
