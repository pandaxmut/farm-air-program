// 请求拦截器，用于拦截请求 以及双token无感刷新
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ElNotification } from 'element-plus'
import {useUserStore} from "@/stores/userStore";

const request =  axios.create();
export default request;

function logout() {
    localStorage.removeItem('authorization')
    const userStore = useUserStore();
    console.log("HomeView :userStore", userStore.getUser)
    //清楚登录状态
    userStore.clearUser()
    //返回首页
    window.location.href = '/login'
}

// 添加请求拦截器
request.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    // 添加 token
    const token = localStorage.getItem('authorization');
    console.log('token', token);
    if (token !== null) {
        config.headers['authorization'] = token;
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    console.log('请求拦截器请求错误', error);
    return Promise.reject(error);
});

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    if (response.data.code === 402) {
        // 刷新 token
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken !== null) {
            axios.post(
                '/api/users/users/refreshToken',
                {},
                { headers: { 'authorization': refreshToken } }
            ).then(response => {
                if (response.data.code === 402){
                  //点击确认跳转到登录页
                  ElMessage.error('登录失效，请重新登录');
                  ElNotification({
                    title: 'Error',
                    message: '认证失效，请重新登录',
                    type: 'error',
                  })
                  //延迟一秒跳转
                    setTimeout(() => {
                      logout();
                    }, 1500);
                  return ;
                }
                //成功获取到access token
                console.log('刷新token成功')
                localStorage.removeItem('authorization');
                localStorage.setItem('authorization', 'Bearer '+response.data.data);
                window.location.reload();
                
            });
        }
    }

    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    console.log('响应拦截器请求错误', error);
    return Promise.reject(error);
});

