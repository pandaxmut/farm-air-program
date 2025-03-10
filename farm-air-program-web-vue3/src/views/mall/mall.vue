<template>
  <div class="mall-container">

    <!-- 商城头部 -->
    <div class="mall-header">
      <img src="https://www.moguit.cn/static/img/mall.525a645.png
" alt="商城图标" class="mall-icon">
      <div>
        <h1 class="mall-title">社区商城</h1>
        <p class="mall-subtitle">便捷商城，好物云集</p>
      </div>
    </div>

    <!-- 分类和排序 -->
    <div class="mall-filters">
      <div class="category-filters">
        <button v-for="(category, index) in categories" :key="index"
                :class="['filter-btn', activeCategory === category ? 'active' : '']"
                @click="setCategory(category)">
          {{ category }}
        </button>
      </div>
      <!-- 搜索框 -->
      <div class="search-box" >
        <input type="text"  style="width: 300px" placeholder="搜索感兴趣的实战课程内容" class="search-input">
        <button class="search-btn">🔍</button>
      </div>
    </div>
    <div class="sort-filters" style="padding: 10px" >
      <button v-for="(sort, index) in sorts" :key="index"
              :class="['filter-btn', activeSort === sort ? 'active' : '']"
              @click="setSort(sort)">
        {{ sort }}
      </button>
    </div>


    <!-- 商品展示 -->
    <div class="product-list">
      <div class="product-card" style="width: 300px">
        <img src="../../assets/images/tag3.png" alt="商品图片" class="product-img">
        <div class="product-info">
          <h3 class="product-title">蘑菇社区源码</h3>
          <p class="product-desc">使用蘑菇社区源码，搭建专属知识库</p>
          <div class="product-price">
            <span class="price-new">￥299</span>
            <span class="price-old">￥399</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const categories = ["全部", "源码", "虚拟道具"];
const sorts = ["推荐", "最新", "精选", "评分"];
const activeCategory = ref("全部");
const activeSort = ref("推荐");

const setCategory = (category) => {
  activeCategory.value = category;
};

const setSort = (sort) => {
  activeSort.value = sort;
};
</script>

<style scoped>
/* 页面整体样式 */
.mall-container {
  //background-color: #f4f4f4;
  min-height: 100vh;
  padding: 20px;
  font-family: Arial, sans-serif;
}

/* 商城头部 */
.mall-header {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.mall-icon {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.mall-title {
  font-size: 18px;
  font-weight: bold;
}

.mall-subtitle {
  font-size: 12px;
  color: gray;
}

/* 分类 & 排序 */
.mall-filters {
  display: flex;
  justify-content: space-between;
  background-color: white;
  padding: 10px;
  margin-top: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.category-filters, .sort-filters {
  display: flex;
  gap: 10px;

}

.filter-btn {
  padding: 6px 12px;
  border-radius: 15px;
  font-size: 14px;
  border: none;
  cursor: pointer;
  background-color: #e0e0e0;
}

.filter-btn.active {
  background-color: black;
  color: white;
}

/* 搜索框 */
.search-box {
  display: flex;
  margin-top: 0px;
  background-color: white;
  padding: 0px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.search-input {
  flex-grow: 1;
  border: none;
  outline: none;
  padding: 5px;
}

.search-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
}

/* 商品展示 */
.product-list {
  margin-top: 15px;
}

.product-card {
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.product-img {
  width: 100%;
  border-radius: 8px;
}

.product-info {
  margin-top: 10px;
}

.product-title {
  font-size: 16px;
  font-weight: bold;
}

.product-desc {
  font-size: 12px;
  color: gray;
}

.product-price {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.price-new {
  font-size: 18px;
  color: red;
  font-weight: bold;
}

.price-old {
  font-size: 14px;
  color: gray;
  text-decoration: line-through;
  margin-left: 10px;
}
</style>
