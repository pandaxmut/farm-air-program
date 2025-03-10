package com.example.questions.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.example.common.exception.enums.ResponseEnum;
import com.example.common.utils.CommonResult;
import com.example.questions.dto.QuestionsEsDTO;
import com.example.questions.entity.Questions;
import com.example.questions.mapper.QuestionsMapper;
import com.example.questions.service.IQuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.questions.vo.QuestionsSearchRespVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
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
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
@Slf4j
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements IQuestionsService {

    @Autowired
    private QuestionsMapper questionsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RestHighLevelClient esClient;

    private static final String INDEX_NAME = "questions_index";

//    @Transactional // 低配版，无es
//    @Override
//    public CommonResult<Boolean> createQuestion(Questions questions) {
//        boolean flag = checkIdempotent(questions);
//        if ( Boolean.FALSE.equals(flag)) {
//            return CommonResult.error(ResponseEnum.MULTIPLE);
//        }
//        //1.写数据库
//        questionsMapper.insert(questions);
//        //2.新增redis缓存
//        String cacheKey = "question:list:"+questions.getId();
//        redisTemplate.opsForValue().setIfAbsent(cacheKey,questions,5, TimeUnit.MINUTES);
//        //3.写redis缓存 TODO:mq异步延时刷新缓存
//        return CommonResult.success(true);
//    }

    //修改update
    @Transactional
    @Override
    public CommonResult<Boolean> updateQuestion(Questions questions) {
        boolean flag = checkIdempotent(questions);
        if ( Boolean.FALSE.equals(flag)) {
            return CommonResult.error(ResponseEnum.MULTIPLE);
        }
        //1.写数据库
        questionsMapper.updateById(questions);
        //2.新增redis缓存
        String cacheKey = "question:list:"+questions.getId();
        redisTemplate.opsForValue().set(cacheKey,questions,5, TimeUnit.MINUTES);
        //3.写redis缓存 TODO:mq异步延时刷新缓存
        return CommonResult.success(true);
    }

    //查找单个文章详情
    @Override
    public CommonResult<Questions> getQuestionByIdFromRedis(String id) {
        //1.从redis缓存中获取
        String cacheKey = "question:list:"+id;
        Questions questions = (Questions) redisTemplate.opsForValue().get(cacheKey);
        if (questions != null) {
            return CommonResult.success(questions);
        }
        //2.从数据库中获取 这个可能造成雪崩，所以我们先从数据库中获取为空也保存到redis中，第二只能有一个线程查询，我们应该使用redisson
        questions = questionsMapper.selectById(id);
        //3.写入redis缓存 (不管是否为空值，我们都要保存到redis中，防止缓存击穿)
        if (questions == null) {
           redisTemplate.opsForValue().setIfAbsent(cacheKey,null,5,TimeUnit.MINUTES);
        }
        redisTemplate.opsForValue().setIfAbsent(cacheKey,questions,5, TimeUnit.MINUTES);
        return CommonResult.success(questions);
    }

    //查找所有问答详情
    @Override
    public CommonResult< List<Questions>> getQuestions() {
        String cacheKey = "question:list:";
        String lockKey = "lock:question:";

        //redis 缓存中获取
        List<Questions> questions = (List<Questions>)redisTemplate.opsForValue().get(cacheKey);
        if (questions != null || questions.size()!=0){
            return CommonResult.success(questions);
        }
        //数据库中获取 使用分布式锁
        RLock lock = redissonClient.getLock(lockKey);
        try{
            if(lock.tryLock(3,10, TimeUnit.SECONDS)){
                //再次检查缓存
                questions = (List<Questions>)redisTemplate.opsForValue().get(cacheKey);
                if (questions != null || questions.size()!=0){
                    //缓存不为空，直接返回
                    return CommonResult.success(questions);
                }
                if (questions == null && redisTemplate.hasKey(cacheKey)){
                    //缓存为空说明数据库中没有数据
                    return CommonResult.success(questions);
                }
                questions = questionsMapper.selectList(null);
                if (questions != null){
                    RBatch batch = redissonClient.createBatch();
                    Random random = new Random();

                    for (Questions item : questions) {
                        String key = cacheKey+item.getId();
                        Questions value = item;
                        long expireTime = 300 + random.nextInt(300);

                        batch.getBucket(key).trySetAsync(value, expireTime, TimeUnit.SECONDS);
                    }

                    BatchResult<?> batchResult = batch.execute();
                    System.out.println("批量设置结果：" + batchResult.getResponses());
                }else{
                    // 空值缓存，防止穿透
                    redisTemplate.opsForValue().setIfAbsent(cacheKey,null,5,TimeUnit.MINUTES);
                }
            }else{
                //没拿到锁，直接返回失败
                return CommonResult.error(50002,"更新问答缓存中，稍后再试！");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CommonResult.error(50003,"系统异常");
        }finally{
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return CommonResult.success(questions);


    }

    @Override
    @Transactional
    public void updateAIAnswer(Questions questions) {
        //前端有做校验，这里不需要防抖
        //1.保存到数据库
        questionsMapper.updateById(questions);

        //rabbitMq 实现最终一致性
        //2.保存到redis
        String cacheKey = "question:list:"+questions.getId();
        redisTemplate.opsForValue().set(cacheKey,questions,5, TimeUnit.MINUTES);

        System.out.println("更新问答缓存成功！");
        //3.保存到es
        try {
            saveOrUpdateArticleToEs(questions);
            System.out.println("es 问答服务（aiAnswer）字段数据保存成功！");
        } catch (IOException e) {
            System.out.println("es 问答服务（aiAnswer）字段数据保存失败！");
            throw new RuntimeException(e);
        }
    }


    //幂等性判断
    private boolean checkIdempotent(Questions questions) {
        //0.幂等性判断：
        String ifKey = "idempotent:%s:%s";
        String userId = questions.getUserId();
        String titleAndContent = questions.getTitle()+questions.getContent();
        String titleAndContentMd5 = DigestUtil.md5Hex(titleAndContent);
        String formatKey = String.format(ifKey, userId, titleAndContentMd5);
        System.out.println("formatKey = " + formatKey);
        //0.1 查、存 判断是否重复插入（五分钟内不得重复插入），如果重复插入，则直接返回
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(formatKey, "", 5, TimeUnit.MINUTES));
    }


    //新增 redis mysql es
    @Transactional
    @Override
    public CommonResult<Boolean> createQuestion(Questions questions) {

        boolean flag = checkIdempotent(questions);
        if ( Boolean.FALSE.equals(flag)) {
            return CommonResult.error(ResponseEnum.MULTIPLE);
        }
        questions.setPostTime(LocalDateTime.now());
        questions.setReadCount(0);
        questions.setCommentCount(0);
        questions.setStatus("1");
        //保存到数据库
        questionsMapper.insert(questions);
        //保存到redis
        String cacheKey = "question:list:"+questions.getId();
        redisTemplate.opsForValue().set(cacheKey,questions,5, TimeUnit.MINUTES);
        //保存到es
        try {
            this.saveOrUpdateArticleToEs(questions);
            log.info("es数据保存成功！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return CommonResult.success(true);
    }

    //es新增
    public void saveOrUpdateArticleToEs(Questions questions) throws IOException {
        IndexRequest request = new IndexRequest(INDEX_NAME);
        request.id(questions.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        // 将Java对象转换为JSON字符串 ,时间序列化
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Map<String, Object> map = objectMapper.convertValue(questions, new TypeReference<Map<String, Object>>() {});
        request.source(map);
        esClient.index(request, RequestOptions.DEFAULT);
    }

    //es查找
    public CommonResult<List<QuestionsSearchRespVO>> getQuestionByKeywordFromRedis(String keyword, int page, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span class='em'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(new HighlightBuilder.Field("title").fragmentSize(200).numOfFragments(1));
        highlightBuilder.field(new HighlightBuilder.Field("content").fragmentSize(500).numOfFragments(1));
        highlightBuilder.field(new HighlightBuilder.Field("aiAnswer").fragmentSize(300).numOfFragments(1));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(keyword, "title", "content", "aiAnswer"))
                .highlighter(highlightBuilder)
                .from((page - 1) * size)
                .size(size)
                .sort("postTime", SortOrder.DESC);

        searchRequest.source(sourceBuilder);
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //获取result
        List<QuestionsSearchRespVO> resultList = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            QuestionsSearchRespVO vo = objectMapper.readValue(hit.getSourceAsString(), QuestionsSearchRespVO.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.get("title") != null) {
                vo.setHighlightTitle(highlightFields.get("title").fragments()[0].string());
            }
            if (highlightFields.get("content") != null) {
                vo.setHighlightContent(highlightFields.get("content").fragments()[0].string());
            }
            if (highlightFields.get("aiAnswer") != null){
                vo.setHighlightAiAnswer(highlightFields.get("aiAnswer").fragments()[0].string());
            }
            resultList.add(vo);
            log.info("vo = " + vo);
        }

        //TODO 如果这里没有的话，可能需要查找redis、mysql
        return CommonResult.success(resultList);

    }

}
