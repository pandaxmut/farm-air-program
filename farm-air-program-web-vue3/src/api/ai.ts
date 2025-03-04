import axios from "axios";
import request from "@/api/utils/request";
import {ref} from 'vue'
import {fetchEventSource} from "@microsoft/fetch-event-source";

interface AIMessage{
    id: string,
    question: string,
    answer: string,
    postTime: date|null,
}

export async function getAIMessage(message: string){
    const res = await request.get(`/api/questions/ai/generate`,{
        params:{
            message: message
        }
    })
    return res.data;
}


const BaseUrl = "http://localhost:3004/api/questions/ai/generateStream/role";
// export const postStreamChat = (
//     role: string,
//     task: string,
//     question: string,
//
//     onMessage: (chunk: string) => void,
//     onError: (err: Error) => void,
//     onClose: () => void
// ) => {
//     const ctrl = new AbortController();
//
//     fetchEventSource(BaseUrl, {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json",
//             authorization: localStorage.getItem("authorization") || "",
//         },
//         body: JSON.stringify({
//             'role':role,
//             'task':task,
//             'question':question
//         }),
//         signal: ctrl.signal,
//         onmessage: (event) => {
//             try {
//                 // 直接传递原始数据（假设后端已清理格式）
//                 onMessage(event.data);
//             } catch (err) {
//                 onError(err as Error);
//             }
//         },
//         onerror: (err) => {
//             onError(err);
//             ctrl.abort();
//         },
//         onclose: () => {
//             onClose();
//             ctrl.abort();
//         },
//         onopen: async (response) => {
//             if (!response.ok) {
//                 throw new Error(`HTTP error! status: ${response.status}`);
//             }
//         },
//     });
//
//     return () => ctrl.abort();
// };

export const postStreamChat = (
    role: string,
    task: string,
    question: string,
    onMessage: (chunk: string) => void,
    onError: (err: Error) => void
): Promise<void> => {
    return new Promise((resolve, reject) => {
        const ctrl = new AbortController();

        fetchEventSource(BaseUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                authorization: localStorage.getItem("authorization") || "",
            },
            body: JSON.stringify({
                role: role,
                task: task,
                question: question
            }),
            signal: ctrl.signal,
            onmessage: (event) => {
                try {
                    // 直接传递原始数据（假设后端已清理格式）
                    onMessage(event.data);
                } catch (err) {
                    onError(err as Error);
                }
            },
            onerror: (err) => {
                onError(err);
                ctrl.abort();
                reject(err); // 出错时 reject
            },
            onclose: () => {
                ctrl.abort();
                resolve(); // 流式请求完成时 resolve
            },
            onopen: async (response) => {
                if (!response.ok) {
                    reject(new Error(`HTTP error! status: ${response.status}`));
                }
            },
        });
    });
};

export async function saveAIMessages(aiMessage: AIMessage){
    const res = await request.post(`/api/questions/aiMessage` ,{
        aiMessage: aiMessage
    })
    return res.data;
}