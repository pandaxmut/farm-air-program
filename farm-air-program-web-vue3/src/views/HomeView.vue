<script lang="ts" setup>
import { ref, computed,onMounted,watch } from 'vue';
import { useRoute } from "vue-router";
import { useMainStore } from '@/stores/store'; // 导入 Pinia store
import { el } from 'element-plus/es/locales.mjs';
import { useRouter} from "vue-router";
const router = useRouter();
const route = useRoute();
import { useUserStore} from "@/stores/userStore";
import MyselfPage from "@/views/MyselfPage.vue";
const userStore = useUserStore();



function login(){
  router.push('/login');
}

function logout() {
    localStorage.removeItem('authorization')
    const userStore = useUserStore();
    console.log("HomeView :userStore", userStore.getUser)
    //清楚登录状态
    userStore.clearUser()
    //返回首页
    window.location.href = '/'
}

// Default selected menu index
const selectedIndex = ref("3");
// 控制消息提醒红点是否显示
const hasNotification = ref(true); // 可以根据实际情况更新值

// Callback for menu selection
const selected = (index: string, indexPath: string[]) => {
  console.log("index", index, "indexPath", indexPath);
};

const mainStore = useMainStore(); // 使用 Pinia store

// 使用 computed 可以使变量数值实时更新
const showMsgCount = computed(() => {
  return mainStore.msgCount;
});

// 示例：模拟更新消息数量
setTimeout(() => {
  mainStore.updateMsgCount(5);
}, 3000);


const drawer = ref(false)

</script>


<template>
  <nav class="top-nav">
    
    <el-menu
      class="main-menu"
      mode="horizontal"
      :default-active="selectedIndex"
      @select="selected"
      :ellipsis="false"
    >
      <!-- Left-aligned menu items -->
      <div class="logo">
      <RouterLink to="/">
        <img src="@/components/icons/图标.svg" alt="Logo"/>
      </RouterLink>
    </div>
      <el-menu-item index="0">
        <RouterLink to="/">首页</RouterLink>
      </el-menu-item>
      <el-menu-item index="1">
        <RouterLink to="/subject">专栏</RouterLink>
      </el-menu-item>
      <el-menu-item index="2">
        <RouterLink to="/knowledge">知识库</RouterLink>
      </el-menu-item>
      <el-menu-item index="3">
        <RouterLink to="/question">问答区</RouterLink>
      </el-menu-item>
      
      <el-menu-item index="4">
        <RouterLink to="/mall">兑换商城</RouterLink>
      </el-menu-item>
      <el-menu-item index="5">
        <RouterLink to="/news">资讯</RouterLink>
      </el-menu-item>
      <el-menu-item index="6">
        <RouterLink to="/about">About</RouterLink>
      </el-menu-item>

      <!-- Right-aligned menu items -->
      <div class="right-menu" >

        <el-menu-item index="7" style="padding: 5px;">
        
          <el-dropdown placement="top-end"  >
            <el-button type="primary" plain>创作</el-button>
            <!-- 下拉菜单内容 -->
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  
                  <RouterLink to="/articles/create">
                    创作文章
                  </RouterLink>
                </el-dropdown-item>
                <el-dropdown-item>
                  <RouterLink to="/question/create">
                    发布问答
                  </RouterLink>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-menu-item>
        

        <el-menu-item index="8" style="padding: 10px;">
          <!-- 使用 el-dropdown 包裹消息图标 -->
          <el-dropdown placement="top-end" class="message-link" trigger="hover">
            <!-- 图标和红点的容器，设置为相对定位 -->
            <div class="icon-container">
              <!-- 图标 -->
              <img
                src="@/components/icons/提醒.svg"
                alt="Message"
                style="height: 25px;"
              />
              <!-- 消息提醒红点 -->
              <span v-if="hasNotification" class="notification-dot"></span>
            </div>

            <!-- 下拉菜单内容 -->
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>评论</el-dropdown-item>
                <el-dropdown-item>点赞</el-dropdown-item>
                <el-dropdown-item>收藏</el-dropdown-item>
                <el-dropdown-item>预约</el-dropdown-item>
                <el-dropdown-item>系统通知</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-menu-item>


        <el-menu-item v-if="!userStore.userId"  index="10" style="padding-left: 10px; padding-right: 0px;">
          <el-button type="primary" plain style="margin-right: 2px;" @click="login">登录</el-button>
        </el-menu-item>

        <el-sub-menu index="9">
          <template #title>我的工作台</template>
          <el-menu-item index="9-1">
            <a  @click="drawer = true">
              个人中心
            </a>
          </el-menu-item>
          <el-menu-item index="9-2">修改密码</el-menu-item>
          <el-menu-item index="9-3">系统通知</el-menu-item>
          <el-menu-item index="9-4"   @click="logout">
            登出
          </el-menu-item>



        </el-sub-menu>
        

      </div>
    </el-menu>

    <el-drawer v-model="drawer" title="个人中心" :with-header="false" size="40%">
      <MyselfPage />
    </el-drawer>


  </nav>
  <div class="router-view">
    <RouterView />
  </div>
  
</template>


<style scoped>
:deep(.el-drawer__body) {
  padding: 0;
}
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 50px;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.7); /* Transparent white background */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Shadow effect */
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px; /* Adjust logo height as needed */
  width: auto;
  margin-left:10px;
}

.main-menu {
  flex: 1;
  display: flex;
  align-items: center;

}

.right-menu {
  display: flex;
  margin-left: auto; /* Push right menu items to the far right */
}

.user-item {
  margin-left: 20px; /* Space between user item and other right menu items */
}



/* 图标和红点的容器 */
.icon-container {
  position: relative;
  display: inline-block;
}

/* 红点样式 */
.notification-dot {
  position: absolute;
  top: -5px;      /* 调整红点的垂直位置 */
  right: -5px;    /* 调整红点的水平位置 */
  width: 8px;     /* 红点的大小 */
  height: 8px;
  background-color: red; /* 红点颜色 */
  border-radius: 50%;    /* 圆形 */
}

/* 给 <RouterView /> 留出空间，避免被固定的导航栏遮挡 */
.router-view {
  margin-top: 55px; /* 60px 根据你的导航栏高度进行调整 */
  margin-left: 40px;
  margin-right: 40px;
}
</style>



