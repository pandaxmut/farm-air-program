// src/stores/store.ts
import { defineStore } from 'pinia';

export const useMainStore = defineStore('main', {
  state: () => ({
    msgCount: 0, // 初始消息数量
  }),
  actions: {
    updateMsgCount(count: number) {
      this.msgCount = count;
    },
  },
});