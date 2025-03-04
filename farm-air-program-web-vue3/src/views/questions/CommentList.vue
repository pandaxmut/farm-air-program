<template>
  <h2 style="font-family: 楷体">聊天列表</h2>
  <div v-for="item in comments" class="chat-list">

    <el-row  class="chat-item" >
      <el-col :span="1">
        <el-avatar :src="item.avatarUrl" style="height: 40px; width: 40px; line-height: 60px; cursor: pointer;" />
      </el-col>
      <el-col :span="23">
        <span class="name"> {{item.username}}</span>
        <span class="time">{{item.createTime}}</span>
        <el-row>
          <el-col :span="15" style="margin-left: 10px; margin-top: 10px;margin-bottom: 10px; border: 1px solid #ccc; min-height: 50px">
            {{item.commentText}}
          </el-col>
        </el-row>
        <el-row>
          <el-col :offset="1">
            <a type="primary" @click="toggleCommentBox(item.id)">回复</a>
            <a type="primary">点赞</a>
          </el-col>
          <!-- 评论框 -->
          <div v-if="showCommentBoxId === item.id" style="width: 1000px">
            <CommentBox
                :id="props.id"
                :parent-id="item.id"
                :receiver="{ id: item.userId, username: item.username }"
                @submit-success="handleReplySuccess"
                @cancel="closeCommentBox"
            />
          </div>
        </el-row>
      </el-col>
    </el-row>
    <el-row  v-for="childrenItem in item.children" class="chat-item" style="padding-left: 50px">
      <el-col :span="1">
        <el-avatar :src="childrenItem.userAvatar" style="height: 40px; width: 40px; line-height: 60px; cursor: pointer;" />
      </el-col>
      <el-col :span="23">
        <span class="name"> {{childrenItem.username}}</span>
        <span class="time">{{childrenItem.createTime}}</span>
        <span class="" v-if="childrenItem.receiverUsername"  >  回复   {{childrenItem.receiverUsername}}</span>
        <el-row>
          <el-col :span="15" style="margin-left: 10px; margin-top: 10px;margin-bottom: 10px; border: 1px solid #ccc; min-height: 50px">
            {{childrenItem.commentText}}
          </el-col>

        </el-row>
        <el-row>
          <el-col :offset="1">
            <a type="primary" @click="toggleCommentBox(childrenItem.id)">回复</a>
            <a type="primary">点赞</a>
          </el-col>
          <!-- 评论框 -->
          <div v-if="showCommentBoxId === childrenItem.id" style="width: 1000px">
            <CommentBox
                :id="props.id"
                :parent-id="item.id"
                :receiver="{ id: childrenItem.userId, username: childrenItem.username }"
                @submit-success="handleReplySuccess"
                @cancel="closeCommentBox"
            />
          </div>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import CommentBox from '@/views/questions/CommentBox.vue';
import { defineProps,reactive,ref,onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import  {type QuestionComments} from '@/api/questionComments';
import { insertArticleComments,getArticleComments } from '@/api/questionComments';
// 定义响应式数据
const userStore = useUserStore();
// 使用 storeToRefs 保持响应性 ✅
const { userId,username, avatarUrl } = userStore

const props = defineProps({
  id: {
    type: String,
    default: ''
  }
});


const userInfo = reactive({
  username: username,
  age: 18,
  sex: '男',
  description: '我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影',
  userAvatar: avatarUrl || new URL('@/assets/images/tag4.png', import.meta.url).href,
});
const comment = ref('我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影');

const articleComments = ref<QuestionComments | null>(
    {
      id: '1',
      parentId: null,
      questionId: props.id,
      commentText: comment.value,
      createTime: '',
      userId: userId,
      username: username,
      avatarUrl: avatarUrl,
      receiverId: null,
      receiverUsername: null,
      receiverAvatarUrl: null,
    }
);

const sendComment = async function sendComments() {
  const {data} = insertArticleComments(articleComments.value)
  alert(data)
}

//查看评论
const comments = ref<QuestionComments>([]);
//获取该文章所有评论
onMounted(async () => {
  const {data} = await getArticleComments(props.id)
  comments.value = data
  console.log("avatarUrl:",avatarUrl)
  console.log("comments:",comments.value)

})

// 控制评论框显示的状态
const showCommentBoxId = ref<string | null>(null)

// 切换评论框显示
const toggleCommentBox = (commentId: string) => {
  showCommentBoxId.value = showCommentBoxId.value === commentId ? null : commentId
}

// 关闭评论框
const closeCommentBox = () => {
  showCommentBoxId.value = null
}

// 处理回复成功
const handleReplySuccess = () => {
  closeCommentBox()
  // 这里可以刷新评论列表或更新本地数据
}

</script>

<style lang="scss" scoped>
.chat-list {
  width: 100%; /* 确保容器宽度占满全屏 */
  padding: 10px; /* 给聊天列表一些内边距 */
  .chat-item {
    margin-bottom: 20px;
    .name {
      margin-left: 10px;
      padding-top: 10px;
      font-size: 18px;
      color: #333;
      font-weight: bold;
    }
    .time {
      margin-left: 20px;
      padding-top: 10px;
      font-size: 12px;
      color: #999;
    }
    a {
      margin-left: 10px;
      color: #999;
      font-size: 12px;
    }

  }
}
</style>
