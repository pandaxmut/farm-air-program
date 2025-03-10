// 使用axios发送请求
import axios from "axios";
import request from "@/api/utils/request";

interface Question {
    id: string;
    title: string;
    content: string;
    aiAnswer: string;
    tagId: number | null;
    tagName: string;
    userId: string;
    username: string;
    avatar: string;
    postTime: Date | null;
    readCount: number;
    commentCount: number;
    topType: number;
    status: number;
    highlightTitle: string;
    highlightContent: string;
    highlightAiAnswer: string;

}

//新增问答
export async function createQuestion(question: Question) {
    try{
        const response = request.post(`/api/questions/questions`, question)
        console.log('question created successfully:', response.data);
        return response.data;
    }catch (error){
        console.error('Error creating question:', error);
        throw error;
    }
}

//所有问题
export async function getQuestions() {
    try{
        const response = await request.get(`/api/questions/questions/all`, {

        })
        console.log('question fetched successfully:', response.data.data);
        return response.data.data;
    }catch (error){
        console.error('Error fetching question:', error);
        throw error;
    }
}
//es搜索
export async function getSearchQuestions(keyword:string, page:number = 1, size:number = 5) {
    try{
        const response = await request.get(`/api/questions/questions/search`, {
            params: {
                keyword: keyword,
                page: page,
                size: size
            }
        })
        console.log('question fetched successfully:', response.data.data);
        return response.data.data;
    }catch (error){
        console.error('Error fetching question:', error);
        throw error;
    }
}



//某一篇文章
export async function getQuestionById(id: String) {
    try{
        const response = await request.get(`/api/questions/questions/${id}`, {

        })
        console.log('question fetched successfully:', response.data.data);
        return response.data.data;
    }catch (error){
        console.error('Error fetching question:', error);
        throw error;
    }
}

//获取问答tags
export async function getQuestionTags() {
    try{
        const response = request.get(`/api/questions/questionsTags/all`)
        console.log('question tags fetched successfully:', response);
        return response
    }catch (error){
        console.error('Error fetching question tags:', error);
        throw error;
    }
}

//更新问答信息
export async function saveQuestionAIAnswer(question: Question) {
    try{
        const response = request.patch(`/api/questions/questions`, question)
        console.log('question updated successfully:', response.data);
        return response.data;
    }catch (error){
        console.error('Error')
        throw error;
    }


}