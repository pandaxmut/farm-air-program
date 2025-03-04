<script setup lang="ts">
import { postStreamChat,saveAIMessages} from "@/api/ai";
import {ref, computed, defineProps, onMounted} from "vue"
import VueMarkdownEditor, { xss } from '@kangc/v-md-editor';
import { InfoFilled } from '@element-plus/icons-vue'
const props = defineProps({
  id: {
    type: String,
    default: ''
  },
  content: {
    type: String,
    default: ''
  },

})

const isLoading = ref(false)
const answer = ref("")
const role = ref("你现在是一名文章总结大师")
const task = props.content
const question  = ref('')
const description = ref("")


const html = computed(() => xss.process(VueMarkdownEditor.vMdParser.themeConfig.markdownParser.render(answer.value)));
const getMessage = async () => {
  isLoading.value = true;
  answer.value = ""; // 清空历史内容

  await postStreamChat(
      role.value,task,question.value,
      (chunk) => {
        // 清理冗余的 "data: " 前缀

        // 如果 是结束标记，直接返回
        if (chunk === "[DONE]")
        {
          answer.value = answer.value.replace(/^(#+)([^\s#])/gm, '$1 $2');
          return;
        }

        // 拼接流式数据
        answer.value += chunk

      },
      (err) => {
        console.error("Error:", err);
        isLoading.value = false;
      },
      () => {
        console.log("已关闭");
      }
  );

  //保存数据到questionAI表中
  const aiMessage = {
    id:'',
    question: question.value,
    answer: answer.value,
    postTime:new Date()
  }
  const data = await saveAIMessages(aiMessage)
  console.log("问答问答是否保存到sql:",data);
  console.log("保存数据到questionAI表中");
};


</script>

<template>
  <el-row style="margin-top: 20px;">

    <el-col :span="21" offset="1">
      <el-input
          v-model="question"
          style=""
          autosize
          size="large"
          type="textarea"
          placeholder="基于该文章内容，继续向AI提问>>"
      />
    </el-col>
    <el-col :span="2"  >
      <el-button type="primary" @click="getMessage()">发送</el-button>
    </el-col>
    <el-col :span="24" style="padding-top: 30px">
      <el-card>
        <div v-if="!isLoading">
          <el-skeleton animated />
        </div>
        <div v-else>
          <div>
            <v-md-preview-html :html="html" preview-class="vuepress-markdown-body">
              {{answer}}
            </v-md-preview-html>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">

</style>