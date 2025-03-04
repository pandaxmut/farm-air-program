import axios from "axios";
import request from "@/api/utils/request";

export interface QuestionComments {
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
    questionId: string;

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
    children: QuestionComments[];
}

export async function insertArticleComments(articleComments: QuestionComments) {
    console.log("questionComments", articleComments);
    const res = await request.post(`/api/questions/questionsComments`, articleComments)
    return res.data;
}

export async function getArticleComments(questionId: string) {
    const res = await request.get(`/api/questions/questionsComments/${questionId}`)
    return res.data;
}
