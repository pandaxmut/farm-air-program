<template>
    <div class="mt-5 container">
      <el-input
        v-model="input3"
        style="max-width: 600px;"
        placeholder="Please input"
        class="input-with-select"
        size="large"
      >
        <template #prepend>
          <el-select v-model="select" placeholder="行情" style="width: 115px;" size="large" >
            <el-option label="畜牧业" value="1" />
            <el-option label="农业" value="2" />
            <el-option label="行情" value="3" />
            <el-option label="全文" value="4" />
          </el-select>
        </template>
        <template #append>
          <el-button type="primary">搜索</el-button>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 5px;">
      <!-- 热搜词 -->
       <el-row>
        <el-col :span="7"></el-col>
        热搜词:
        <el-col :span="2"  v-for="item in hotSearch">
          <span style="color: #606266;">{{ item }}</span>
        </el-col>
      </el-row>
       
      
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import 'element-plus/dist/index.css' // 确保引入 Element Plus 的样式
  import { ElInput, ElSelect, ElOption, ElButton } from 'element-plus'
  
  const input3 = ref('')
  const select = ref('')

  const options = [
    { value: '选项1', label: '黄金糕' },
    { value: '选项2', label: '双皮奶' },
    { value: '选项3', label: '蚵仔煎' },
    { value: '选项4', label: '龙须面' },
    { value: '选项5', label: '北京烤鸭' }
  ]

  const hotSearch = ref([
    '猪肉aaaaa','牛肉','羊肉','土豆'
  ])
  </script>
  
  <style lang="scss" scoped>
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  @media (min-width: 1024px) {
    .container {
      align-items: flex-start;
    }
  }
  </style>