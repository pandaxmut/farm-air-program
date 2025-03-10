<script setup lang="ts">
import { ref,onMounted,computed,watch} from "vue";
import {type Question, getQuestions,getSearchQuestions} from "@/api/questions";
import {useRouter,useRoute} from "vue-router";
import {type Article,getSearchArticles} from "@/api/articles";

const route = useRoute();  // 获取当前路由信息
const keyword = computed(() => route.query.keyword || '');  // 取 query.keyword
const router = useRouter()
//设置显示类型
const showType = ref('questions');

// 跳转到详细问题页面（示例）
const goToQuestion = (id: string) => {
  router.push({
    name: 'QuestionDetailView', // 使用路由名称
    params: { id: id } // 通过 params 传递动态路由参数
  });
};

const questionsList = ref([])

const articlesList = ref([])
const resource = ref([])

onMounted(async () => {
  searchQuestions()

})
// 监听 `keyword` 变化，自动重新搜索
watch(keyword, () => {
  questionsList.value = [];
  articlesList.value = [];
  resource.value = [];
  searchQuestions();
});
const page = ref(1)
const size = ref(5)

const searchQuestions = async () => {

  const data = await getSearchQuestions(keyword.value, page.value, size.value)
  console.log("data:", data);
  if (!data) {
    alert('没有搜索到相关问题');
    return;
  }

  showType.value = 'questions'
  for(let i = 0; i < data.length; i++){
    questionsList.value.push(data[i])
  }
  console.log("questionsList:", questionsList.value);
}

const searchArticles = async () => {
  const data = await getSearchArticles(keyword.value, page.value, size.value)
  console.log("data:", data);
  if (!data ) {
    alert('没有搜索到相关文章');
    return;
  }
  showType.value = 'articles'
  for(let i = 0; i < data.length; i++){
    articlesList.value.push(data[i])
  }
}
const selectType = async (type:string) => {
  //重置页码和每页数量
  page.value = 1
  size.value = 5
  questionsList.value = [];
  articlesList.value = [];
  resource.value = [];

  if (type === 'articles') {
    await searchArticles()
  } else if (type === 'questions') {
    await searchQuestions()
  }
  showType.value = type
}
//查看更多
const searchContent = async () => {
  page.value += 1;
  if (showType.value === 'articles') {
    await searchArticles()
  } else if (showType.value === 'questions') {
    await searchQuestions()
  }
}

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
  <div class="container">
    <el-row >
      <!-- 左侧内容区域 -->
      <el-col :span="24">
        <div class="header">
          <el-row   style=" margin-left: 70px; font-size: 16px; padding-top: 18px;padding-left: 10px">
            <el-col :span="2"  >
              <a @click="selectType('questions')" style=" display: flex;  align-items: center; font-size: 16px">
                <el-icon><CollectionTag /></el-icon>
                <span  style="padding-left: 5px">问答</span>
              </a>
            </el-col>
            <el-col :span="2" >
              <a @click="selectType('articles')" style=" display: flex; align-items: center; ">
                <el-icon><WindPower /></el-icon>
                <span  style="padding-left: 5px">文章</span>
              </a>
            </el-col>
            <el-col :span="3" >
              <a  href="#" style=" display: flex; align-items: center; ">
                <el-icon><ChatDotSquare /></el-icon>
                <span style="padding-left: 5px" >资源</span>
              </a>
            </el-col>
            <el-divider style="margin-top: 0px"></el-divider>
          </el-row>
        </div>
      </el-col>
      <el-col :span="16">

        <div v-if="showType === 'questions'" style="margin-left: 70px;margin-right: 70px">

          <el-row :gutter="20">
            <el-col :span="24" v-for="(item, index) in questionsList" :key="index">
              <el-card class="question-card">
                <el-row :gutter="20">
                  <el-col :span="3" class="card-header">
                    <el-tag size="large" type="success" style="margin-top: 15px; margin-bottom: 10px">回答 {{ item.commentCount }}</el-tag >
                    <el-tag size="large" type="warning">阅读 {{ item.readCount }}</el-tag>
                  </el-col>
                  <el-col :span="20" class="question-info">
                    <el-row>
                      <el-col :span="20">
                        <h2>
                          <a @click="goToQuestion(item.id)" v-html="item.highlightTitle || item.title"></a>

                        </h2>
                      </el-col>
                      <el-col :span="3" >
                        <el-tag size="large" style="float: right">{{item.tagName}}</el-tag>
                      </el-col>
                    </el-row>
                    <div class="blog-info" style="margin-top: 30px">
                      <ul class="flex-container">
                        <li style="padding-right: 6px; text-align: center; cursor: pointer;">
                <span id="blogId15" class="">
                  <span class="el-avatar el-avatar--medium el-avatar--circle">
                    <img :src="item.avatar" style="object-fit: fill;">
                  </span>
                </span>
                        </li>
                        <li class="author" style="margin-top: 9px;  cursor: pointer;">
                          <span class="pointer lv1">{{item.username}}</span>
                        </li>

                        <li class="view" style="margin-top: 8px; margin-left: 3px; ">
                          <img src="@/assets/images/查看.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>

                          <span>{{item.readCount}}</span>
                        </li>
                        <li class="like" style="margin-top: 8px; margin-left: 3px;">
                          <img src="@/assets/images/时间.svg" style="width: 1.5em; height: 1.5em; margin-top: 2px;"/>
                          <span> {{item.postTime}}</span>
                        </li>

                      </ul>
                    </div>

                  </el-col>
                </el-row>

              </el-card>
            </el-col>
            <el-col :span="24">
              <div v-if="questionsList" style="display: flex; justify-content: center;">
                <el-button type="primary" @click="searchContent" plain>查看更多</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-if="showType === 'articles'" style="margin-left: 70px;margin-right: 70px">
          <div v-for="(article, index) in articlesList" :key="index">
            <div class="article-container">
              <h2>
                <!--          v-html="article.highlightTitle"-->
                <a @click.prevent="goToArticleDetail(article.id)"  v-html="article.highlightTitle || article.title" ></a>
              </h2>
              <el-row class="tag-container" >
                <el-col :span="18" >
                  <el-text line-clamp="3" v-html="article.highlightContent || title" ></el-text>
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

<!--                    <li class="like" style="margin-top: 8px; margin-left: 3px; " >-->
<!--                      <Star @click="toggleFavorite" :style="{ width: '1.5em', height: '1.5em', marginTop: '2px',  cursor: 'pointer', color: favorite ? 'red' : 'gray'  }" />-->
<!--                      <span>{{ article.collectionCount }}收藏</span>-->
<!--                    </li>-->
<!--                    <li class="like" style="margin-top: 8px; margin-left: 3px; ">-->
<!--                      <a v-if="article" @click="doLike(article,index) ">-->
<!--                        <img v-if="article?.good"  src= "@/assets/images/good.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>-->
<!--                        <img v-else src= "@/assets/images/nogood.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>-->
<!--                      </a>-->
<!--                      &lt;!&ndash;                <img v-if="article?.good"  src= "@/assets/images/good.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>&ndash;&gt;-->
<!--                      &lt;!&ndash;                <img v-else src= "@/assets/images/nogood.svg" style=" width: 1.5em; height: 1.5em; margin-Top: 2px; cursor: pointer; "/>&ndash;&gt;-->
<!--                      <span> {{ article.goodCount}}点赞</span>-->
<!--                    </li>-->
                  </ul>
                </div>
              </el-row>
            </div>

          </div>
        </div>
      </el-col>

      <!-- 右侧热门问题区域 -->
      <el-col :span="6">
        <div >
          <div class="header">
            <h3>热门问题</h3>
          </div>
          <el-divider></el-divider>

<!--          <el-list>-->
<!--            <el-list-item v-for="(item, index) in questions" :key="index" >-->
<!--              <div style="font-size: 16px">-->
<!--                <el-tag v-if="index===0" type="danger" size="small">{{index}}</el-tag>-->
<!--                <el-tag v-else-if="index===1" type="warning" size="small">{{index}}</el-tag>-->
<!--                <el-tag v-else-if="index===2" type="success" size="small">{{index}}</el-tag>-->
<!--                <el-tag v-else type="info" size="small">{{index}}</el-tag>-->

<!--                <a type="text" size="small" style="color:#555;margin-left:5px">{{ item.title }}</a>-->
<!--                <el-divider  style="margin-top: 0px;margin-bottom: 2px"/>-->
<!--              </div>-->

<!--            </el-list-item>-->
<!--          </el-list>-->
        </div>
      </el-col>
    </el-row>
  </div>
</template>


<style scoped>

::v-deep .em {
  color: #fb7a75;
  font-weight: bold;
  font-style: italic; /* 字体倾斜 */
}
.article-container {
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


.container {
  margin: 20px;
}

.header {
  font-size: 24px;
  font-weight: bold;
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

.question-card {
  margin-bottom: 10px;
}

.card-header {
  display: flex;
  flex-direction: column;
  justify-content: left;
  //justify-content: space-between;

}


</style>>