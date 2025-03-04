<template>
  <div class="home-container">
    <header class="top-nav">
      <nav class="navbar">
        <div class="logo">Logo</div>
        <ul class="nav-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/subject">专栏</router-link></li>
          <li><router-link to="/knowledge">知识库</router-link></li>
          <li><router-link to="/question">问答区</router-link></li>
          <li><router-link to="/about">关于系统</router-link></li>
          <li><router-link to="/mall">商城</router-link></li>
          <li><router-link to="/news">新闻</router-link></li>
        </ul>
      </nav>
    </header>

    <main class="main">
      <router-view></router-view> <!-- 子组件将在这里被渲染 -->
    </main>
  </div>
</template>

<script setup lang="ts">
// 可以在这里添加任何你需要的逻辑
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0;
  padding: 20px;
}

.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.7); /* 半透明白色背景 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  display: flex;
  align-items: center;
  padding: 10px 20px; /* 内边距 */
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 20px;
}

.nav-links li a {
  color: #333;
  text-decoration: none;
  font-size: 16px;
}

.main {
  display: block;
  padding: 20px;
  margin-top: 80px; /* 确保主体部分紧贴导航栏下方 */
}
</style>