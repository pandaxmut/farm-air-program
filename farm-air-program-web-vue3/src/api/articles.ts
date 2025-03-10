// 使用axios发送请求
import axios from "axios";
import request from "@/api/utils/request";



// articleReqVo实体类
export interface Article {
    id: string;
    //作者信息也得在里面
    user: {
        id: string;
        username: string;
        nickname: string;
        avatar: string;
    };
    title: string;
    categoryId: number | null;
    categoryName: string;
    tags: string[];
    userId: string;
    username: string;
    avatarUrl: string;
    cover: string | null;
    content: string;
    summary: string | null;
    postTime: Date | null;
    lastUpdateTime: Date | null;
    readCount: number;
    goodCount: number;
    collectionCount: number;
    commentCount: number;
    topType: number;
    status: number;
    good: boolean;
    collection: boolean;
    highlightTitle: string;
    highlightContent: string;
}


export interface ArticlesColumns{
    id: string;
    title: string;
    categoryId: number | null;
    categoryName: string;
    userId: string;
    username: string;
    avatarUrl: string;
    cover: string | null;
    summary: string | null;
    postTime: Date | null;
    lastUpdateTime: Date | null;
    readCount: number;
    collectionCount: number;
    status: number;
    articles: [];
}


// 定义一个函数来发送 POST 请求
// export async function getUser(id: number): Promise<User> {
//     try {
//       const response = await axios.get(`/api/users/${id}`);
//       console.log('User created successfully:', response.data);
//       return response.data;
//     } catch (error) {
//       console.error('Error creating user:', error);
//       throw error;
//     }
//   }



export async function getArticleById(id: String){
    try {
      const response = await request.get(`/api/articles/articles/${id}`, {

      });
      console.log('articles fetched successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching articles:', error);
      throw error;
    }
}

// 所有文章
export async function getArticles() {
  try {
    const response = await request.get(`/api/articles/articles`, {
      
    });
    console.log('articles fetched successfully:', response.data.data);
    return response.data; 
  } catch (error) {
    console.error('Error fetching user:', error);
    throw error;
  }
}



export async function createArticle(article:Article){
    try {
        const response = await request.post(`/api/articles/articles`, article);
        console.log('articles created successfully:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error creating articles:', error);
        throw error;
    }
}
export async function getAllCategory() {
    try {
      const response = await request.get(`/api/articles/articlesCategory/all`);
      console.log('articles fetched successfully:', response.data);
      return response.data;
    } catch (error) {
        console.error('Error fetching articles:', error)
    }
}

//like表
export async function likeArticle(article: Article) {
    try {
        console.log("article",article)
      const response = await request.post(`/api/articles/articlesLike`, article);
      console.log('articles fetched successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching articles:', error);
      throw error;
    }
  }

//普通搜索
export async function getSearchArticle(articleName:string, articleType:number){
    try{
        const response = await request.get(`/api/articles/articles/search`, {
            params: {
                articleName: articleName,
                status: articleType
            }
        });
        console.log('articles fetched successfully:', response.data);
        return response.data;
    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}
//es搜索 关键词
export async function getSearchArticles(keyword:string, page:number = 1, size:number = 5) {
    try{
        const response = await request.get(`/api/articles/articles/search/es`, {
            params: {
                keyword: keyword,
                page: page,
                size: size
            }
        });
        console.log('articles fetched successfully:', response.data.data);
        return response.data.data;
    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}


export async function createArticleColumns(articlesColumns:ArticleColumns){
    try{
        const response = await request.post(`/api/articles/articlesColumns`, articlesColumns);
        console.log('articles fetched successfully:', response.data);
        return response.data;

    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}
//查询一个用户创建的专栏列表

export async function getSearchColumns(columnsName:string, columnsType:number){
    try{
        const response = await request.get(`/api/articles/articlesColumns/search`, {
            params: {
                columnsName: columnsName,
                status: columnsType
            }
        });
        console.log('articles fetched successfully:', response.data);
        return response.data;
    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}

//查询一个分类的专栏列表
export async function getSearchColumnsByCategory(categoryId:number, status:number){
    try{
        const response = await request.get(`/api/articles/articlesColumns/searchByCategory`, {
            params: {
                categoryId: categoryId,
                status: status
            }
        });
        console.log('articles fetched successfully:', response.data);
        return response.data;
    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}
//查询一个专栏下的文章列表
export async function getSearchArticlesByColumns(columnsId:number, status:number){
    try{
        const response = await request.get(`/api/articles/articlesColumns/searchByColumns`, {
            params: {
                columnsId: columnsId,
                status: status
            }
        });
        console.log('articles fetched');
        return response.data;
    }catch (error){
        console.error('Error fetching articles:', error);
        throw error;
    }
}