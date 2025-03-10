package com.example.articles.service;

import com.example.articles.vo.ArticlesSearchRespVO;
import com.example.common.utils.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticlesSearchService {

    private final RestHighLevelClient esClient;



    public CommonResult<List<ArticlesSearchRespVO>> searchArticles(String keyword, int page, int size) throws IOException, IOException {
        SearchRequest request = new SearchRequest("articles_index");

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span class='em'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(new HighlightBuilder.Field("title").fragmentSize(200).numOfFragments(1));
        highlightBuilder.field(new HighlightBuilder.Field("content").fragmentSize(500).numOfFragments(1));
        highlightBuilder.field(new HighlightBuilder.Field("summary").fragmentSize(300).numOfFragments(1));


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(keyword, "title", "content", "summary"))
                .highlighter(highlightBuilder)
                .from((page - 1) * size)
                .size(size)
                .sort("postTime", SortOrder.DESC);

        request.source(sourceBuilder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        List<ArticlesSearchRespVO> resultList = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ArticlesSearchRespVO vo = objectMapper.readValue(hit.getSourceAsString(), ArticlesSearchRespVO.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.get("title") != null) {
                vo.setHighlightTitle(highlightFields.get("title").fragments()[0].string());
            }
            if (highlightFields.get("content") != null) {
                vo.setHighlightContent(highlightFields.get("content").fragments()[0].string());
            }
            if (highlightFields.get("summary") != null) {
                vo.setHighlightSummary(highlightFields.get("summary").fragments()[0].string());
            }

            resultList.add(vo);
        }


        return CommonResult.success(resultList);
    }
}
