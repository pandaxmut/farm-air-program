<template>
  <div class="container">
    
    <h2>
      <a href="#" style="text-decoration-color: black;">支付宝网站支付DEMO</a>
    </h2>
    <el-row class="tag-container" >
      <el-col :span="18" >
        <el-text line-clamp="3" >
          Lorem ipsum dolor sit, amet consectetur adipisicing elit. Officiis, rerum quo facere nemo accusamus excepturi qui delectus nisi necessitatibus porro officia perspiciatis cumque minima, voluptate quae dolorem a, tempore ex!
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Laborum assumenda laudantium doloremque ratione libero sapiente nobis repudiandae ut cumque culpa in error rerum repellat cum minima sint, excepturi consequatur. Nihil!
        </el-text>
      </el-col>
      <!-- 文章图片 -->
      <el-col :span="6" style="display: flex; justify-content: center;">
        <img src="@/assets/images/tag1.png" style="height: 90px; ">
      </el-col>
      <div class="blog-info">
          <ul class="flex-container">
            <li style="padding-right: 6px; text-align: center; cursor: pointer;">
              <span id="blogId15" class="">
                <span class="el-avatar el-avatar--medium el-avatar--circle">
                  <img src="https://picture.moguit.cn//blog/admin/webp/2022/5/1/1651414702363.webp" style="object-fit: fill;">
                </span>
              </span>
            </li>
            <li class="author" style="margin-top: 9px;  cursor: pointer;">
              <span class="pointer lv1">panda</span>
            </li>
            <li class="lmname" style="margin-top: 8px;">
              <span class="el-tag el-tag--primary el-tag--mini el-tag--light is-hit" style="cursor: pointer;">
                生活随笔
              </span>
              <span class="el-tag el-tag--success el-tag--mini el-tag--light is-hit" style="margin-left: 1px; cursor: pointer;">
                学习笔记
              </span>
            </li>
            <li class="view" style="margin-top: 8px; margin-left: 3px; ">
              <img src="@/assets/images/查看.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>

              <span>151</span>
            </li>
            <li class="like" style="margin-top: 8px; margin-left: 3px;">
              <img src="@/assets/images/时间.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>
              <span> 5月前</span>
            </li>
            <li class="like" style="margin-top: 8px; margin-left: 3px; " >
              <Star @click="toggleFavorite" :style="{ width: '1.5em', height: '1.5em', marginTop: '2px',  cursor: 'pointer', color: favorite ? 'red' : 'gray'  }" />
              <span>{{ favoriteCount }}收藏</span>
            </li>
            <li class="like" style="margin-top: 8px; margin-left: 3px; ">
              <img :src="zan ? zanTypes[1] : zanTypes[0]" @click="toggleZan" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>
            
              <span> {{ zanCount}}点赞</span>
            </li>
          </ul>
        </div>
    </el-row>
  </div>

    <div v-for="(article, index) in articles">
      <div class="container">
        <h2>
<!--          v-html="article.highlightTitle"-->
          <a @click.prevent="goToArticleDetail(article.id)"   >
            {{article.title}}
          </a>
      </h2>
      <el-row class="tag-container" >
        <el-col :span="18" >
          <el-text line-clamp="3" >
            {{ article.content}}
          </el-text>
        </el-col>
        <!-- 文章图片 -->
        <el-col :span="6" style="display: flex; justify-content: center;">
          <img :src=article.cover style="height: 90px; ">
        </el-col>
        <div class="blog-info">
            <ul class="flex-container">
              <li style="padding-right: 6px; text-align: center; cursor: pointer;">
                <span id="blogId15" class="">
                  <span class="el-avatar el-avatar--medium el-avatar--circle">
                    <img :src=article.avatarUrl style="object-fit: fill;">
                  </span>
                </span>
              </li>
              <li class="author" style="margin-top: 9px;  cursor: pointer;">
                <span class="pointer lv1">{{ article.username }}</span>
              </li>

              <li class="lmname" style="margin-top: 8px;">
                <span  v-for="(tag,index) in article.tags"  class="el-tag  el-tag--primary el-tag--mini el-tag--light is-hit" style="cursor: pointer; margin-right: 2px">
                  {{ tag }}
                </span>
<!--                <span class="el-tag el-tag&#45;&#45;success el-tag&#45;&#45;mini el-tag&#45;&#45;light is-hit" style="margin-left: 1px; cursor: pointer;">-->
<!--                  学习笔记-->
<!--                </span>-->
              </li>
              <li class="view" style="margin-top: 8px; margin-left: 3px; ">
                <img src="@/assets/images/查看.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>

                <span>{{ article.readCount }}</span>
              </li>
              <li class="like" style="margin-top: 8px; margin-left: 3px;">
                <img src="@/assets/images/时间.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>
                <span> {{ article.postTime }}</span>
              </li>

              <li class="like" style="margin-top: 8px; margin-left: 3px; " >
                <Star @click="toggleFavorite" :style="{ width: '1.5em', height: '1.5em', marginTop: '2px',  cursor: 'pointer', color: favorite ? 'red' : 'gray'  }" />
                <span>{{ article.collectionCount }}收藏</span>
              </li>
              <li class="like" style="margin-top: 8px; margin-left: 3px; ">
                <a v-if="article" @click="doLike(article,index) ">
                  <img v-if="article?.good"  src= "@/assets/images/good.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>
                  <img v-else src= "@/assets/images/nogood.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>
                </a>
<!--                <img v-if="article?.good"  src= "@/assets/images/good.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>-->
<!--                <img v-else src= "@/assets/images/nogood.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>-->
                <span> {{ article.goodCount}}点赞</span>
              </li>
            </ul>
          </div>
      </el-row>
      </div>
      
    </div>
    
</template>

<script setup lang="ts">
  import { ref, onMounted } from "vue";
  import {useRouter} from "vue-router";
  import {getArticles, likeArticle} from "@/api/articles";
  import type { Article } from "@/api/articles";
  import { ar } from "element-plus/es/locales.mjs";

  const router = useRouter();

  const favoriteCount = ref(1);
  const favorite = ref(false);

  function toggleFavorite() {
      favorite.value = !favorite.value; // 切换收藏状态
      if (favorite.value) {
        favoriteCount.value++; // 收藏数增加
      }
      else {
        favoriteCount.value--; // 收藏数减少
      }
    }
  const zanCount = ref(1);
  const zan = ref(false);
  const zanTypes = [
    new URL(`@/assets/images/nogood.svg`, import.meta.url).href,
    new URL(`@/assets/images/good.svg`, import.meta.url).href
  ]


  const articles = ref<Article[]>([]);
  //获取文章列表信息
  onMounted(async () => {
    const { data } = await getArticles();
    console.log("data:", data);
    articles.value = data;
    console.log("赋值之后articles",articles.value)
    
  });

  function goToArticleDetail(id:number) {
    router.push({
      name: 'ArticlesView',
      params: {
        id: id,
      },
    });
  }

  async function doLike(art: Article, index: number) {
    // 确保 index 对应的文章存在
    console.log(articles.value[2])
    if (!articles.value[index]) {
      console.error("Article not found at index:", index);
      return;
    }

    // 更新点赞状态
    articles.value[index].good = !art.good;

    // 根据点赞状态更新 goodCount
    if (articles.value[index].good) {
      articles.value[index].goodCount++; // 点赞数增加
    } else {
      articles.value[index].goodCount--; // 点赞数减少
    }

    console.log("Updated article.good:", articles.value[index].good);
    console.log("Updated article.goodCount:", articles.value[index].goodCount);

    try {
      // 将更新后的 `art` 传递给后端
      const data = await likeArticle(articles.value[index]);
      console.log("Server response:", data.data);
      alert("点赞成功");
    } catch (error) {
      console.error("点赞失败:", error);
      alert("点赞失败，请重试");
    }
  }


  function toggleZan(index:number,articleId:string) {
    zan.value = !zan.value; // 切换点赞状态
    if (zan.value) {
      articles.value[index].goodCount++; // 点赞数增加
    } else {
      articles.value[index].goodCount--; // 点赞数减少
    }
  }

  </script>

<style lang="scss" scoped>

.container {
  background-color: #ffffff;
  box-shadow: 0px 0px 10px rgba(185, 222, 254, 0.904);
  padding: 20px;
  margin-right:10px;
  margin-bottom: 20px;
  

  h2 {
    margin-bottom: 5px;
    color: black;

  }
  
  .blog-info{
    margin-top: 15px;
    text-align: center;
    .flex-container {
      padding-left: 5px;
      display: flex;
      align-items: center; /* 垂直居中对齐 */
      gap: 10px; /* 项目之间的间距 */
    }

    .flex-container li {
      list-style-type: none; /* 去掉默认的列表样式 */
    }

    .author, .lmname, .view, .like {
      display: flex;
      align-items: center; /* 垂直居中对齐 */
    }

    .iconfont {
      font-family: 'iconfont'; /* 确保图标字体正确 */
    }
  }

}
.gun{
  :hover {
    transform: translateY(3px);
  }
}
::v-deep .em {
  color: #fb7a75;
  font-size: 20px;
  font-weight: bold;
}
</style>
