import axios from "axios";
import request from "@/api/utils/request";

export interface ArticlesComments {
    /**
     * 评论id
     */
    id: string;

    /**
     * 父评论id
     */
    parentId: string;

    /**
     * 文章id
     */
    articleId: string;

    /**
     * 评论内容
     */
    commentText: string;

    /**
     * 创建时间
     */
    createTime: string; // 使用 ISO 格式的字符串来表示日期

    /**
     * 发送者
     */
    userId: string;

    /**
     * 评论者昵称
     */
    username: string;

    /**
     * 评论者头像
     */
    avatarUrl: string;

    /**
     * 接收者
     */
    receiverId: string;

    /**
     * 接收者昵称
     */
    receiverUsername: string;

    /**
     * 接收者头像
     */
    receiverAvatarUrl: string;
    /**
     * 子消息列表
     */
    children: ArticlesComments[];
}

export async function insertArticleComments(articleComments: ArticlesComments) {
    console.log("articleComments", articleComments);
    const res = await request.post(`/api/articles/articlesComments`, articleComments)
    return res.data;
}

export async function getArticleComments(articleId: string) {
    const res = await request.get(`/api/articles/articlesComments/${articleId}`)
    return res.data;
}
