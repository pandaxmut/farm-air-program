<template>

  <!-- 分为三块:分类菜单、轮播图、我的信息 -->
  <div class="content"  style="margin-top:10px;">
    <el-row >
        
      <el-col :span="18" class="categorymenu">
        <!-- <div>轮播图</div> -->
        <div class="block text-center">
          <!-- <span class="demonstration">
            Switch when indicator is hovered (default)
          </span> -->
          <el-carousel  height="460px" :interval="2000">
            <el-carousel-item v-for="tag in tags" >
              <img :src="tag.url" width="100%" alt="carousel image" class="carousel-image" />
              
            </el-carousel-item>
          </el-carousel>
        </div>

        
      </el-col>
      <el-col :span="6" style="padding-left:20px; padding-right:20px; ">
        <el-row style="padding-bottom:20px;">
          <img  src="@/assets/images/tag1.png" alt="推荐"  height="220" width="100%">
        </el-row>
        <el-row>
          <img  src="@/assets/images/tag2.png" alt="分类"  height="220" width="100%">
        </el-row>

      </el-col>
    </el-row>
  </div>
  
 
   <!-- 热点文章 -->
   <div class="custom-style" style="margin-top:20px; margin-bottom: 15px;" >
        <el-segmented v-model="value" :options="options" block="true" />
   </div>
   

  <div>
    <el-row>
      <el-col :span="18">
       <ArticleListComponent></ArticleListComponent>
       
      </el-col>
      <el-col :span="6">
        <UserPage/>
      </el-col>
    </el-row>
  </div>
  


  


    
</template>

<script setup lang="ts">
// 可以在这里添加任何你需要的逻辑
import SearchComponent from '@/components/search/SearchComponent.vue';
import ArticleListComponent from '@/components/articles/ArticleListComponent.vue';
import UserPage from '@/components/UserComponent/UserPage.vue';

import { el } from 'element-plus/es/locales.mjs';
import { ref, onMounted } from 'vue';

// 插入轮播图
const tags = ref([
  { name: 'tag1', url: new URL(`@/assets/images/tag1.png`, import.meta.url).href },
  { name: 'tag2', url: new URL(`@/assets/images/tag2.png`, import.meta.url).href },
  { name: 'tag3', url:new URL(`@/assets/images/tag3.png`, import.meta.url).href },
  { name: 'tag4', url: new URL(`@/assets/images/tag4.png`, import.meta.url).href },
  { name: 'tag5', url: new URL(`@/assets/images/tag5.png`, import.meta.url).href}
]);

const value = ref('最热话题')

const options = ['最热话题', '养殖圈', '种植圈', '专家答疑','使用指南']

</script>

<style  lang="scss" scoped>

@media (min-width: 1024px) {
  body {
    display: block;
    // place-items: center;
  }
  .content {
    background-color: #ffffff;
    box-shadow: 0px 0px 10px rgba(185, 222, 254, 0.904);
    padding: 20px;
    border-radius: 8px;
    .categorymenu {
      // background-color: rgb(252, 242, 230);
      
      padding: 0;
      h3 {
        display: flex;
        place-content: center;
      }
      .row {
        display: flex;
        justify-content: center; /* 水平居中 */
        // gap: 10px; /* 列之间的间距 */
      }

      .col {
        flex: 1; /* 使列等宽 */
        text-align: center; /* 列内部文本居中 */
        // padding: 10px;
        border-radius: 4px;
      }
    }
  }

  .demonstration {
  color: var(--el-text-color-secondary);
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

}

.custom-style .el-segmented {
  --el-segmented-item-selected-color: var(--el-text-color-primary);
  --el-segmented-item-selected-bg-color: #ffd100;
  --el-border-radius-base: 16px;
}
</style>