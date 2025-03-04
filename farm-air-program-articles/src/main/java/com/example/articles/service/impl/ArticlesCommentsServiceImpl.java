package com.example.articles.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesComments;
import com.example.articles.mapper.ArticlesCategoryMapper;
import com.example.articles.mapper.ArticlesCommentsMapper;
import com.example.articles.service.IArticlesCategoryService;
import com.example.articles.service.IArticlesCommentsService;
import com.example.articles.vo.ArticlesCommentsReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Service
public class ArticlesCommentsServiceImpl extends ServiceImpl<ArticlesCommentsMapper, ArticlesComments> implements IArticlesCommentsService {

    @Resource
    private ArticlesCommentsMapper articlesCommentsMapper;

    @Override
//    public List<ArticlesCommentsReqVO> getArticlesByArticleId(String articleId) {
//
//        //测试
//        System.out.println("测试");
//        ArticlesComments one = articlesCommentsMapper.getOne("1");
//        System.out.println(one);
//
//        List<ArticlesCommentsReqVO> list = articlesCommentsMapper.getArticlesByArticleId(articleId);
//        //筛选出父评论，并且将子评论放在父评论下面
//        // 先筛选出父评论
//        List<ArticlesCommentsReqVO> parentComments = list.stream()
//                .filter(articlesCommentsReqVO -> articlesCommentsReqVO.getParentId() == null)
//                .collect(Collectors.toList());
//
//// 将子评论按父评论的 ID 分组，并赋值给父评论的 `children` 属性
//        List<ArticlesCommentsReqVO> finalList = list;
//        parentComments.forEach(parentComment -> {
//            List<ArticlesCommentsReqVO> children = finalList.stream()
//                    .filter(child -> child.getParentId() != null && child.getParentId().equals(parentComment.getId()))
//                    .collect(Collectors.toList()); // 找到与当前父评论对应的子评论
//
//            parentComment.setChildren(children); // 设置子评论
//        });
//
//// 如果需要，你可以从原始的 `list` 中移除子评论（不含父评论）
//        list = parentComments; // 这里，`list` 将只包含父评论，且每个父评论包含其子评论
//
//        System.out.println( " List<ArticlesCommentsReqVO> list :"+list);
////        for (ArticlesCommentsReqVO articlesCommentsReqVO : list) {
////            if (articlesCommentsReqVO.getParentId() == null) {
////                for (ArticlesCommentsReqVO articlesCommentsReqVO1 : list) {
////                    if (articlesCommentsReqVO1.getParentId() != null && articlesCommentsReqVO1.getParentId().equals(articlesCommentsReqVO.getId())) {
////                        articlesCommentsReqVO.getChildren().add(articlesCommentsReqVO1);
////                    }
////                }
////            }
////        }
//
//        return list;
//    }

    public List<ArticlesCommentsReqVO> getArticlesByArticleId(String articleId) {
        // 获取所有评论
        List<ArticlesCommentsReqVO> allComments = articlesCommentsMapper.getArticlesByArticleId(articleId);

        // 筛选出一级评论（父评论）
        List<ArticlesCommentsReqVO> parentComments = allComments.stream()
                .filter(comment -> comment.getParentId() == null)
                .collect(Collectors.toList());

        // 筛选出所有子评论（二级及之后的评论）
        List<ArticlesCommentsReqVO> childComments = allComments.stream()
                .filter(comment -> comment.getParentId() != null)
                .collect(Collectors.toList());

        // 将所有子评论直接归类到对应的一级评论下
        for (ArticlesCommentsReqVO parentComment : parentComments) {
            List<ArticlesCommentsReqVO> children = new ArrayList<>();
            for (ArticlesCommentsReqVO childComment : childComments) {
                // 如果子评论的父 ID 等于当前一级评论的 ID，则将其归类到当前一级评论下
                if (childComment.getParentId().equals(parentComment.getId())) {
                    children.add(childComment);
                }
            }
            parentComment.setChildren(children); // 将所有子评论直接归类到一级评论下
        }

        // 返回结果：一级评论列表，每个一级评论包含所有直接子评论（无论几级）
        return parentComments;
    }
}
