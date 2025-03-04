import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import FeishuCallback from '@/views/login/FeishuCallback.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView,
    // },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('@/views/AboutView.vue'),
    // },

    {
      path: '/',
      component: HomeView,
      children: [
        //首页
        {path: '', component: () => import('@/views/index.vue') },
        //登录注册
        { path: 'login',component: () => import('@/views/login/LoginView.vue') },
        //专栏
        { path: 'subject',
          children: [
            //专栏
            {
              path: '',
              name: 'SubjectView',
              component: () => import('@/views/subject.vue'),
              props: true  // 允许将路由参数作为 props 传递给组件
            },
            {
              path: ':id',
              name: 'SubjectCountPage',
              component: () => import('@/views/SubjectCountPage.vue'),
              props: true  // 允许将路由参数作为 props 传递给组件
            },

          ]
        },
        //知识库
        { path: 'knowledge',component: () => import('@/views/knowledge.vue') },
        //问答区
        {path : 'question',

          children: [
            //问答区
            {
              path: '',
              name: 'QuestionView',
              component: () => import('@/views/questions/question.vue'),
              props: true  // 允许将路由参数作为 props 传递给组件
            },
            //创建问题
            { path: 'create',
              component: () => import('@/views/questions/CreateQuestionsView.vue')
            },
            //问题详情
            { path: ':id',
              name: 'QuestionDetailView',
              component: () => import('@/views/questions/QuestionDetailView.vue'),
              props: true
            }
          ]

        },
        //关于系统
        { path: 'about',component: () => import('@/views/AboutView.vue') },
        //mall
        { path: 'mall',component: () => import('@/views/mall.vue') },
        //news
        { path: 'news',component: () => import('@/views/news.vue') },
        // articles
        { path: 'articles', 
          children: [
            //文章
            {
              path: ':id',
              name: 'ArticlesView',
              component: () => import('@/views/articles/ArticlesView.vue'),
              props: true  // 允许将路由参数作为 props 传递给组件
            },
            
            //创建文章
            { path: 'create',
              component: () => import('@/views/articles/CreateArticlesView.vue')
            },

            
          ]
        },
        //
        
      ]
      
    },
      //第三方登录的回调地址 http://localhost:3004/callback/feishu?token="+token
    {
      path: '/callback/feishu',
      name: 'FeishuCallback',
      component: FeishuCallback, // 添加 FeishuCallback 组件
    },
  ],
})

export default router
