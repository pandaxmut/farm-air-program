import { defineStore } from "pinia";
//存储登录状态
import type { User } from "@/api/users";
import { ref } from "vue";

const defaultUser: User = {
  userId: '',
  username: '',
  feishuId: '',
  description: '',
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
};

export const useUserStore = defineStore("userStore", {
    state: (): User => ({
      ...defaultUser,
      username: 'Lee',
      description: '我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动',
    }),

    getters: {
      getUser: (state) => state,
    },
    actions: {
      setUser(user: any) {
          this.$state = user

      },
      clearUser() {
        //清空
        this.$state = {...defaultUser}
      }
    },
    persist: {
      enabled: true,
      strategies: [
        {
          key: "userStore",
          storage: localStorage,
        },
      ],
    },
});