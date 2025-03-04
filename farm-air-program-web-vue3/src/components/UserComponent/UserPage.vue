<template>
    <!-- 用户看板 -->
    <el-card class="box-card cardStyle is-always-shadow">
          <div>
            <el-row class="userInfo">
              <el-col :span="5" class="userAvatar">
                <el-avatar :src="userInfo.avatarUrl" style="height: 60px; width: 60px; line-height: 60px; cursor: pointer;" />
              </el-col>
              <el-col  :span="18" style="padding-left: 5px;">
                  <div><span style="font-size: 20px;">{{ userInfo.username }}</span></div>
                  <div>
                    <el-text line-clamp="1" >
                      {{ userInfo.description }}
                    </el-text>
                  </div>
              </el-col>
            </el-row>
          </div>
          <div>
            <el-row  >
              <el-col :span="4"  style="padding: 5px; place-items: center;">
                <div class="countStyle">1</div>
                <div >文章</div>
              </el-col>
              <el-col :span="4" style="padding: 5px; place-items: center; ">
                <div class="countStyle">0</div>
                <div>评论</div>
              </el-col>
              <el-col :span="4" style="padding: 5px; place-items: center;">
                <div class="countStyle">3</div>
                <div>收藏</div>
              </el-col>
              <el-col :span="4" style="padding: 5px; place-items: center;">
                <div class="countStyle">2</div>
                <div>预约</div>
              </el-col>
              <el-col :span="4" style="padding: 5px; place-items: center;">
                <div class="countStyle">{{userInfo.totalIntegral}}</div>
                <div>积分</div>
              </el-col>
              <el-col :span="4" style="padding: 5px; place-items: center;">
                <div class="countStyle">1</div>
                <div>关注</div>
              </el-col>
            </el-row>
          </div>
        <div >
          <el-row class="kk">
            <el-col :span="12"> 
              <div class="title">我的关注</div>
              <div>ok</div>
            </el-col>
            <el-col :span="12"> 
              <div class="title">我的收藏</div>
              <div>ok</div>
            </el-col>
          </el-row>
        </div>
       
    </el-card>
    <!-- 任务广场 -->
     <el-row>
      <el-col :span="24"> 
        <TasksComponent/>
      </el-col>
     </el-row>
  </template>
  
  <script setup lang="ts">
  import TasksComponent from './TasksComponent.vue';

  import { reactive  ,onMounted, ref} from 'vue';
  import { getUser } from '@/api/users';
  import type {User} from "@/api/users";
  import {useUserStore} from "@/stores/userStore";

  const userStore = useUserStore();
  console.log("userStore: ",userStore.userId)
  console.log("userStore初始化")

  
  // 定义响应式数据
  
  const userInfo = reactive<User>({
    userId: '',
    username: 'Lee',
    feishuId: '',
    description: '我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动',
    avatarUrl: new URL('@/assets/images/tag4.png', import.meta.url).href,
    age: 0,
    sex: 0,
    email: '',
    phone: '',
    role: 0,
    status: 0,
    totalIntegral: 0,
    currentIntegral: 0,
    createTime: '',
    updateTime: '',
  });
  
  // 获取用户信息的函数
  async function getUserInfo() {
    try {
      
      const {data} = await getUser(); // 使用 getUser 函数
      console.log("data:",data)
      Object.assign(userInfo,data)
      if (userInfo.avatarUrl == null){
        userInfo.avatarUrl = new URL('@/assets/images/tag4.png', import.meta.url).href
      }
      console.log("userInfo:",userInfo.avatarUrl)
      //设置共享数据
      userStore.setUser(userInfo)
      console.log("UserPage : userStore",userStore.getUser)
    
    } catch (err) {
      console.error('Error fetching user info:', err);
    }
  }

  // 在组件挂载时调用 getUserInfo 函数
  onMounted(() => {
    getUserInfo();
  });
    

  
  </script>
  
  <style lang="scss" scoped>
  .container {
    background-color: #ffffff;
    box-shadow: 0px 0px 10px rgba(185, 222, 254, 0.904);
    padding: 20px;
    margin-bottom: 20px;
    
  }
  .countStyle{
    color:black;
    font-weight: 700;
  }
  .kk{
    display: flex;
    place-items: center;
    text-align: center;
    margin-left: -12px;
    margin-right: -12px;
    margin-top: 10px;
    padding: 10px;
    min-height: 45px;
    background: rgb(239, 239, 239);
  }
  </style>