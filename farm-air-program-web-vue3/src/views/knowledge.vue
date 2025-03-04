<script setup>
    import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import SearchComponent from '@/components/search/SearchComponent.vue';
const input1 = ref('')
const input2 = ref('')
const input3 = ref('')
const select = ref('')
        
</script>

<template>
    <!--搜索框-->
    <div class="content" style="margin-top: 60px;">
        <SearchComponent/>
    </div>
    
  </template>
  
  <style scoped>
  .content {
    background-color: #ffffff;
    box-shadow: 0px 0px 10px rgba(185, 222, 254, 0.904);
    padding: 20px;
    border-radius: 8px;
    
    
  }

  </style>