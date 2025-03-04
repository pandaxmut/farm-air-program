<script  setup lang="ts">
import CommentBox from '../comment/CommentBox.vue';
import CommentList from '../comment/CommentList.vue';
import {type Article, getArticleById,likeArticle} from "@/api/articles";

import { ref, reactive, defineProps,computed, watch, onMounted } from 'vue';
import VueMarkdownEditor, { xss } from '@kangc/v-md-editor';
import { da } from 'element-plus/es/locales.mjs';

const article = ref<Article | null>(null);
const props = defineProps({
  id: {
    type: String,
    default: ''
  }
});
onMounted(async () => {
  const id = props.id;
  console.log("id:",id)
  if (id) {
    const data = await getArticleById(id);
    article.value = data.data;
    console.log("!!!!!!!!!article:",article.value)
  }

});


const text = ref(`
# hello world

:::tip
你好
:::

\`\`\`mermaid
graph LR
A --- B
B --> C[fa:fa-ban forbidden]
B --> D(fa:fa-spinner);
\`\`\`
`);

// 计算属性，将 Markdown 转换为 HTML 并进行 XSS 过滤
// const html = computed(() => xss.process(VueMarkdownEditor.vMdParser.themeConfig.markdownParser.render(props.article.content)));
// 3. 计算属性：将 article.content 渲染为 HTML，并进行 XSS 过滤
const html = computed(() => {
  // 检查 article 是否存在，并且 content 字段有效
  if (article.value && article.value.content) {
    console.log("A", article.value.content,);
    // 返回转义后的 HTML
    return xss.process(VueMarkdownEditor.vMdParser.themeConfig.markdownParser.render(article.value.content));
  }
  return ''; // 如果没有 article 或 content，返回空字符串
});


const voice = ref(false);


function openVoice() {
    voice.value = !voice.value; // 切换收藏状态
    }


async function doLike(art: Article) {
  // 这里的 `art` 就是解包后的 `article.value`
  console.log("Received article:", art);
  article.value.good = !art.good;

  if (article.value.good) {
    article.value.goodCount++; // 点赞数增加
  } else {
    article.value.goodCount--; // 点赞数减少
  }

  console.log("Updated article.good:", article.value.good);
  console.log("Updated article.goodCount:", article.value.goodCount);

  try {
    // 将更新后的 `art` 传递给后端
    const data = await likeArticle(article.value);
    console.log("Server response:", data.data);
    alert("点赞成功");
  } catch (error) {
    console.error("点赞失败:", error);
    alert("点赞失败，请重试");
  }
}




</script>
<template>
    
  <div class="container ">
    
    <el-row :gutter="20">
        <!-- TODO: 标签栏 ：点赞，收藏、评论、语音播报 -->
        <el-col :span="1" :offset='1'>
            <div class="button-item">

                <el-badge :value=article?.goodCount class="item">
                    <el-button v-if="article"  @click="doLike(article)" class="is-circle" size="large" circle >
                        <img v-if="article?.good" src= "@/assets/images/good.svg" alt="">
                        <img v-else src= "@/assets/images/nogood.svg" alt="">
                    </el-button>
                </el-badge>
                
                <el-tooltip content="收藏" placement="top" effect="light">
                    <el-badge :value=article?.collectionCount class="item">
                        <el-button  class="is-circle" size="large" circle>
                            <Star  :style="{ width: '2em', height: '2em', marginTop: '2px',  cursor: 'pointer', color: article?.collection ? 'red' : 'gray'  }" />
                            <span></span>
                        </el-button>
                    </el-badge>
                </el-tooltip>

                <el-tooltip content="语音播报" placement="top" effect="light">
                    <el-button  type="default" class=" is-circle " size="large" circle >
                        <Headset @click="openVoice" :style="{ width: '2em', height: '2em', marginTop: '2px',  cursor: 'pointer', color: voice ? 'blue' : 'gray'  }" />
                    </el-button>
                </el-tooltip>



            </div>
        </el-col>
        
        <!-- 文章内容 -->
      <el-col class="article-body" :span="18">
        <div class="body-content" style="padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
          <!-- Title -->
          <div class="body-title" style="text-align: center; margin-bottom: 20px;">
            <h1 style="font-size: 32px; font-family: 'Georgia', serif; font-weight: bold; color: #333;">
              {{ article?.title }}
            </h1>
          </div>

          <el-row style="display: flex; align-items: center; font-size: 14px; color: #666;">
            <!-- Avatar and User Name -->
            <el-col :span="4" style="display: flex; align-items: center;">
              <el-avatar :src="article?.avatarUrl" style="height: 40px; width: 40px; border-radius: 50%; margin-right: 10px;" />
              <span>{{ article?.username }}</span>
            </el-col>

            <!-- Tags Section -->
            <el-col :span="6" style="display: flex; flex-wrap: wrap; align-items: center;">
        <span v-for="(tag,index) in article?.tags" class="el-tag el-tag--primary el-tag--mini"
              style="margin-right: 10px; border-radius: 12px; padding: 4px 8px;">
            {{ tag }}
        </span>
            </el-col>

            <!-- View Count -->
            <el-col :span="3" style="display: flex; align-items: center;">
              <img src="@/assets/images/查看.svg" style="width: 1.5em; height: 1.5em; margin-right: 8px;" />
              <span>{{ article?.readCount }}</span>
            </el-col>

            <!-- Post Time -->
            <el-col :span="5" style="display: flex; align-items: center;">
              <img src="@/assets/images/时间.svg" style="width: 1.5em; height: 1.5em; margin-right: 8px;" />
              <span>{{ article?.postTime }}</span>
            </el-col>
          </el-row>

          <!-- Article Content -->
          <div v-if="html" style="margin-top: 20px; padding-bottom: 20px;">
            <v-md-preview-html :html="html" preview-class="vuepress-markdown-body"></v-md-preview-html>
          </div>

          <!-- Related Articles Section -->
          <div style="margin-top: 40px;">
            <h2 style="font-family: 'KaiTi', cursive; font-size: 20px;">相关文章</h2>
            <div style="border-top: 1px solid #ddd; margin: 20px 0;">
              <!-- Related article content -->
            </div>
          </div>

          <!-- Comments Section -->
          <div style="margin-top: 40px;">
            <h2 style="font-family: 'KaiTi', cursive; font-size: 20px;">文章评论</h2>
            <div class="comments-body" style="border-top: 1px solid #ddd; margin: 20px 0;">
              <CommentBox v-if="props.id" :id="props.id"/>
            </div>
            <div class="comments-table">
              <CommentList v-if="props.id" :id="props.id"/>
            </div>
          </div>
        </div>
      </el-col>

    </el-row>
  </div>
  
</template>

<style lang="scss" scoped>
    .container{
        display: block;
        padding-top: 20px;
        // margin-top:90px;

        .button-item{     
            position: fixed;  // 固定位置
            display: flex;    // 使用 flex 布局
            flex-direction: column;  // 垂直排列
            padding-top:30px;  
            .is-circle{
                margin-bottom: 15px;
                border-radius: 50%;
                width:50px;
                height:50px;
            }
        }
        .body-content{
          background-color: white;
          background-image:
              linear-gradient(to right, #d7d7d7 1px, transparent 1px), /* 纵向线条 */
              linear-gradient(to bottom, #e6e6e6 1px, transparent 1px); /* 横向线条 */
          background-size: 30px 30px; /* 设置网格的间距，类似纸上的方格 */
          background-position: 0 0;

          border-radius: 10px;
          min-height: 600px;
          margin-left: 25px;
          .other-articles{
            min-height: 100px;
            margin-bottom: 20px;
          }
          .comments{
            min-height: 150px;
            margin-bottom: 20px;
            background-color: antiquewhite;
            .comments-body{
              // background-color: aquamarine;
              min-height: 150px;
            }
          }

      }

    }


</style>
