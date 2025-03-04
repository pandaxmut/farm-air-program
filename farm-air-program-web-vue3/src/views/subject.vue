<script setup>
import { ref, reactive, onMounted, computed, watchEffect } from 'vue';
import { getAllCategory, getSearchColumnsByCategory } from '@/api/articles';
import {useRouter} from "vue-router";
const router = useRouter()
const categories = ref({
  1: '种植业',
  2: '畜牧业',
  3: '渔业',
  4: '农业技术'
});
const firstRef = ref(0);
const firstEntry = computed(() => {
  const entries = Object.entries(categories.value);
  return entries.length > 0 ? entries[firstRef.value] : null;
});
// 解构第一个键和值
const firstkey = computed(() => firstEntry.value ? firstEntry.value[0] : null);
const firstValue = computed(() => firstEntry.value ? firstEntry.value[1] : null);

const articlesColumns = ref([]);
// 当前显示的专栏
const currentCategory = ref(categories.value);

onMounted(async () => {
  await getAllCategory().then((res) => {
    console.log(res);
    categories.value = res.data;
  });
  // 获取第一个分类的专栏信息
  await getSearchColumnsByCategory(firstkey.value, 1).then((res) => {
    console.log(res);
    articlesColumns.value = res.data;
    console.log(articlesColumns.value);
  });
});

const changeCategory = async (key) => {
  console.log('当前选中的专栏：', key-1)
  // 更新当前显示的专栏
  currentCategory.value = categories.value[key-1];
  firstRef.value = key-1;

  // 手动更新 firstValue
  const entries = Object.entries(categories.value);
  firstValue.value = entries.length > 0 ? entries[key-1][1] : null;

  // 根据新的专栏信息获取专栏数据
  await getSearchColumnsByCategory(key, 1).then((res) => {
    console.log(res);
    articlesColumns.value = res.data;
    console.log(articlesColumns.value);
  });
};

//推荐、最新、热门
const selectType = (type) => {
  console.log('当前选中的专栏：', type)
  // 更新当前显示的专栏
  if(type == 1){
    articlesColumns.value = articlesColumns.value.sort((a, b) => b.collectionCount - a.collectionCount);
  }else if (type == 2){
    articlesColumns.value = articlesColumns.value.sort((a, b) => new Date(b.postTime) - new Date(a.postTime))
  }else{
    articlesColumns.value = articlesColumns.value.sort((a, b) => a.readCount - b.readCount);

  }
};

const goSubject = (item) => {
  console.log(item);
  // 跳转到专栏页面
  // 使用路由导航
  router.push({
    name: 'SubjectCountPage', // 使用路由名称
    params: { id: item.id } // 通过 params 传递动态路由参数
  });

  //或者
  // router.push({
  //   path: `/subject/${item.id}` // 直接在路径中传递动态路由参数
  // });

  // window.location.href = `/subject?id=${item.id}`;
};
</script>


<template>
  <el-container style="margin-top: 80px">
    <!-- 左侧菜单 -->
    <el-aside width="200px" style="background-color: #f5f5f5; border-radius: 5px;margin-top: 20px;margin-left: 120px">
      <el-menu :default-active="activeCategory" @select="changeCategory">
        <el-menu-item v-for="(value, key) in categories" :key="key" :index="key.toString()">
          {{ value }}
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容 -->
    <el-main  style="margin-left: 50px;margin-right: 50px">
      <el-row :gutter="20" >
        <el-col :span="24">
          <el-card style="height: 100px;display: flex;align-items: center;justify-content: center">
            <h2>{{ firstValue }}</h2>

          </el-card>
        </el-col>
      </el-row>

      <div class="columns" style="margin-top:20px;">
        <el-row   :gutter="20"  style=" font-size: 16px; padding-top: 18px;padding-left: 10px">
          <el-col :span="2" style=" display: flex; align-items: center; ">
            <a @click="selectType(1) ">
              <el-icon><Document /></el-icon>
              <span style="padding-left: 5px" >推荐</span>
            </a>

          </el-col>
          <el-col :span="2"  style=" display: flex; align-items: center; ">
            <a @click="selectType(2)">
              <el-icon><CollectionTag /></el-icon>
              <span  style="padding-left: 5px">最新</span>
            </a>

          </el-col>
          <el-col :span="2" style=" display: flex; align-items: center; ">
            <a @click="selectType(3)">
              <el-icon><WindPower /></el-icon>
              <span  style="padding-left: 5px">最热</span>
            </a>

          </el-col>
          <el-divider style="margin:5px" />
        </el-row>

        <el-row  :gutter="20" style="padding-top:20px;">

          <el-col  :span="12" v-for="(item, index) in articlesColumns" :key="index" style="padding-left: 30px;padding-right: 30px">
            <div class="columnsCard" style="" @click="goSubject(item)">
              <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 5px">

                <a style="margin-bottom: 0px; font-size: 16px; font-weight: bold; font-family: Microsoft YaHei, Arial, Helvetica, sans-serif;">
                  {{ item.title }}

                </a>

                <div style="display: flex; align-items: center; justify-content: space-between;">
                  <el-icon :size="20"><View style="color: #ffb6b6;"  /></el-icon>{{ item.readCount }}
                  <el-icon :size="20"><Star  style="color: #fde162" /></el-icon>{{ item.collectionCount }}
                  <el-tag type="warning" style="margin-right: 10px;">精选</el-tag>
                  <el-tag type="primary" style="margin-right: 10px;">{{ item.categoryName }}</el-tag>
                </div>
              </div>
              <div style="display: flex; justify-content: center;justify-items: center;margin-left: 2px;margin-right: 2px">
                <el-image style="width: 100%; height: 160px;" :src="item.cover" />
              </div>


              <div style="margin-left: 2px;padding-top: 5px;padding-bottom: 5px;">
                <a >{{ item.username }}. 38节. {{item.summary}}</a>

              </div>

            </div>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.columns {
  margin-top: 20px;
  border-radius: 5px;
  background: #ffffff;

  .columnsCard {
    background: rgba(255, 237, 237, 0.6);
    margin-bottom: 20px;
    margin-left: 50px;
    margin-right: 50px;
    padding-right: 2px;
    padding-left: 2px;

    border-radius: 5px;
  }
}

.el-menu {
  border-right: none;
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

</style>