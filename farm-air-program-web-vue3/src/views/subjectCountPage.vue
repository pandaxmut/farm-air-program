<script setup lang="ts">
import {onMounted, ref,defineProps,watch } from 'vue'
import {type ArticlesColumns, getSearchArticlesByColumns} from "@/api/articles";
import {useRouter} from "vue-router";
const router = useRouter();
const props = defineProps({
  id: {
    type: String,
    required: true
  }
})
const articlesColumns = ref<ArticlesColumns | null>(null); // 初始化为空对象const index = ref(2)
const index = ref('2');
const reloadKey = ref(0); // 用于强制重新渲染的 key
onMounted(async () => {
  //获取某一篇专栏的所有文章
  const {data} =  await getSearchArticlesByColumns(props.id,1);
  console.log(data);
  articlesColumns.value = data;
  console.log(articlesColumns.value.summary);

})
// 监听 articlesColumns 的变化
watch(
    articlesColumns,
    (newValue) => {
      console.log('articlesColumns 发生变化:', newValue);
      reloadKey.value++; // 强制重新渲染
    },
    { deep: true } // 深度监听
);




console.log('当前专栏 ID:', index);

const handle = (id: string) => {
  console.log('点击了链接', id);
  // 在这里可以添加你的处理逻辑
  index.value = id;

};

function goToArticleDetail(id:number) {
  router.push({
    name: 'ArticlesView',
    params: {
      id: id,
    },
  });
}

</script>

<template>
  <div  :key="reloadKey" class="container">
    <div class="header"  v-if="articlesColumns">
      <h1>{{articlesColumns.title}}</h1>
      <p>{{articlesColumns.summary}}</p>
      <p>{{articlesColumns.articles.length}} 章节</p>
      <button class="subscribe">订阅</button>
    </div>
    <div v-else>
      <p>加载中...</p>
    </div>
    <div class="content">
      <div  style=" font-size: 16px; padding-top: 10px;">


          <a @click="handle('1')">
            <el-icon><Document /></el-icon>
            <span style="padding-left: 5px" >专栏介绍</span>
          </a>

          <a @click="handle('2')">
            <el-icon><CollectionTag /></el-icon>
            <span  style="padding-left: 5px">专栏目录</span>
          </a>

      </div>
      <el-divider style="margin:5px" />
      <div style= "display: flex;   flex-direction: column; width: 100%;margin-top: 20px">
        <!-- 动态内容 -->
        <div v-if="index === '1'">
          <h1>专栏介绍</h1>
          <p >{{articlesColumns.summary}}</p>


        </div>
        <div v-else-if="index ==='2'">

          <div v-for="(item,index) in articlesColumns?.articles" :key="index"  >
            <div>
              <a @click.prevent="goToArticleDetail(item.id)" class="move">
                ({{ index+1 }}){{item.title}}
              </a>
            </div>
            <el-divider style="margin-top: 0px"/>
          </div>
        </div>
        <div v-else>
          <h1>未知页面</h1>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 70%;
    padding: 20px;
    min-height: 500px;
    background: #ffffff;
    margin-top: 20px;
  }
}


.header {
  background-color: #000;
  color: #fff;
  width: 100%;
  text-align: center;
  padding: 20px;
}


.subscribe {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
}
.move {
  font-weight: bold;  /* 设置加粗 */
  display: inline-block;  /* 使元素支持 transform */
  transition: transform 0.3s ease, color 0.3s ease;  /* 添加平滑过渡效果 */
}

.move:hover {
  transform: translateX(10px);  /* 悬停时向右移动 10px */
  color: #4CAF50;  /* 可以给它加个颜色变化效果（可选） */
}


</style>
