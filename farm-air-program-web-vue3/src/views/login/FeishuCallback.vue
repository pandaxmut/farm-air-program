<template>
  <div style="text-align: center; justify-items: center">
    <h1>飞书登录回调</h1>
    <p v-if="token">Token: {{ token }}</p>
    <p v-else>正在处理登录...</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();//获取路由参数
const router = useRouter();//路由跳转
const token = ref<string | null>(null);

onMounted(() => {
  const accessToken = route.query.accessToken as string | null;
  const refreshToken = route.query.refreshToken as string | null;
  if (accessToken) {
    token.value = accessToken;
    localStorage.setItem('authorization', `Bearer ${accessToken}`);
    localStorage.setItem('refreshToken', `Bearer ${refreshToken}`);
    router.push('/');
  } else {
    console.error('Token not found in query parameters');
  }
});
</script>