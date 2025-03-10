// store.js
import { defineStore } from 'pinia';

export const useSearchStore = defineStore('search', {
    state: () => ({
        keyword: ''
    }),
    actions: {
        setKeyword(val) {
            this.keyword = val;
        }
    },
    persist: true  // ✅ 让 Pinia 数据持久化
});
