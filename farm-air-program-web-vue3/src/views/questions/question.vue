<template>
  <div class="container">
    <el-row gutter="20">
      <!-- 左侧内容区域 -->
      <el-col :span="24">
        <div class="header">
          <el-row  :gutter="20"  style=" margin-left: 70px; font-size: 16px; padding-top: 18px;padding-left: 10px">
            <el-col :span="2"  >
              <a @click="selectType(1)" style=" display: flex;  align-items: center; font-size: 16px">
                <el-icon><CollectionTag /></el-icon>
                <span  style="padding-left: 5px">最新</span>
              </a>
            </el-col>
            <el-col :span="2" >
              <a @click="selectType(2)" style=" display: flex; align-items: center; ">
                <el-icon><WindPower /></el-icon>
                <span  style="padding-left: 5px">最热</span>
              </a>
            </el-col>
            <el-col :span="3" >
              <a @click="selectType(3) " style=" display: flex; align-items: center; ">
                <el-icon><ChatDotSquare /></el-icon>
                <span style="padding-left: 5px" >待回答</span>
              </a>
            </el-col>
            <el-divider style="margin-top: 0px"></el-divider>
          </el-row>
        </div>
      </el-col>
      <el-col :span="16">
        <div style="margin-left: 70px;margin-right: 70px">
          <el-row :gutter="20">
            <el-col :span="24" v-for="(item, index) in newQuestions" :key="index">
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
                        <a @click="goToQuestion(item.id)">{{ item.title }}</a>

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
          </el-row>
        </div>
      </el-col>

      <!-- 右侧热门问题区域 -->
      <el-col :span="6">
        <div >
          <div class="header">
            <h3>热门问题</h3>
          </div>
          <el-divider></el-divider>

          <el-list>
            <el-list-item v-for="(item, index) in questions" :key="index" >
              <div style="font-size: 16px">
                <el-tag v-if="index===0" type="danger" size="small">{{index}}</el-tag>
                <el-tag v-else-if="index===1" type="warning" size="small">{{index}}</el-tag>
                <el-tag v-else-if="index===2" type="success" size="small">{{index}}</el-tag>
                <el-tag v-else type="info" size="small">{{index}}</el-tag>

                <a type="text" size="small" style="color:#555;margin-left:5px">{{ item.title }}</a>
                <el-divider  style="margin-top: 0px;margin-bottom: 2px"/>
              </div>

            </el-list-item>
          </el-list>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref,onMounted } from "vue";
import {type Question, getQuestions} from "@/api/questions"
import {useRouter} from "vue-router";

const router = useRouter()
// 模拟的问答数据


// 跳转到详细问题页面（示例）
const goToQuestion = (id: string) => {
  router.push({
    name: 'QuestionDetailView', // 使用路由名称
    params: { id: id } // 通过 params 传递动态路由参数
  });
};

const questions = ref([])
const newQuestions = ref([])
onMounted(async () => {
  const data = await getQuestions();
  console.log("data:", data);
  questions.value = data;
  newQuestions.value = questions.value.sort((a, b) => b.postTime - a.postTime)
  selectType(1)
})
const selectType = (id: number) =>{
  console.log("id:", id);
  if (id == 1){
    newQuestions.value = questions.value.sort((a, b) => b.postTime - a.postTime)
  }else if( id == 2) {
    newQuestions.value = questions.value.sort((a, b) => b.readCount - a.readCount)
  }else {
    newQuestions.value = questions.value.filter((item) => item.commentCount == 0).sort((a, b) => b.postTime - a.postTime)
  }
}





</script>

<style scoped>
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


</style>
