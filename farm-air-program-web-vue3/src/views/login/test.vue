<script setup lang="ts">
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import {useUserStore} from "@/stores/userStore";
  const router = useRouter()

  const userStore = useUserStore();
  console.log("userStore: ",userStore.userId)
  console.log("userStore初始化")

  function login() {
    const user = {
      userId: '1',
      avatarUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      username: 'admin',
      role: 'admin',
      isLogin: true
    }
    userStore.setUser(user)
    console.log("userStore", userStore.getUser)
    router.push('/')
  }
</script>

<template>
  <el-button type="primary" @click="login">test</el-button>
</template>

<style scoped lang="scss">

</style>