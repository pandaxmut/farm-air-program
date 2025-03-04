
<script setup lang="ts">
  import { defineProps,reactive,ref } from 'vue';
  import { useUserStore } from '@/stores/userStore';
  import  {type QuestionComments} from '@/api/questionComments';
  import { insertArticleComments } from '@/api/questionComments';
  import { ElNotification } from 'element-plus'
  // 定义响应式数据
  const userStore = useUserStore();
  // 使用 storeToRefs 保持响应性 ✅
  const { userId,username, avatarUrl } = userStore

  const props = defineProps({
    id: {
      type: String,
      default: ''
    },
    parentId: {
      type: Object,
      default: null
    },
    receiver: {
      type: Object,
      default: null
    }
  })
  const emit = defineEmits(['submitSuccess', 'cancel'])
  const commentText = ref('');
  const handleSubmit = async () => {
    const newComment: QuestionComments = {
      questionId: props.id, // 需要从父组件传入实际值
      commentText: commentText.value,
      userId: userId,
      username: username,
      avatarUrl: avatarUrl,
      parentId: props.parentId || null,
      receiverId: props.receiver?.id || null,
      receiverUsername: props.receiver?.username || null
    }

    const { code } = await insertArticleComments(newComment)
    if (code === 200) {
      ElNotification.success('评论成功')
      emit('submitSuccess')
      commentText.value = ''
    }
  }

  const handleCancel = () => {
    emit('cancel')
    commentText.value = ''
  }

  // const props = defineProps({
  //   id: {
  //     type: String,
  //     default: ''
  //   }
  // });

  const userInfo = reactive({
    username: username,
    age: 18,
    sex: '男',
    description: '我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影',
    userAvatar: avatarUrl || new URL('@/assets/images/tag4.png', import.meta.url).href,
  });
  const comment = ref('我是一个前端工程师，喜欢前端，喜欢技术，喜欢生活，喜欢读书，喜欢音乐，喜欢旅行，喜欢运动，喜欢摄影');
  // const articleComments = ref<ArticleComments | null>(
  //     {
  //       id: '1',
  //       parentId: props.parentId,
  //       articleId: props.id,
  //       commentText: comment.value,
  //       createTime: '',
  //       userId: userId,
  //       username: username,
  //       avatarUrl: avatarUrl,
  //       receiverId: props.receiver,
  //       receiverUsername: null,
  //       receiverAvatarUrl: null,
  //     }
  // );

  const sendComment = async function sendComments() {
    articleComments.value.commentText = comment.value;
    const data = await insertArticleComments(questionComments.value)
    if (data.code === 200) {
      ElNotification.success({
        title: '评论成功',
        message: '感谢你贡献的评论！',
        showClose: false,
      })
    } else {
      ElNotification.error({
        title: '评论失败',
        message: '系统繁忙，再评论试试~',
        showClose: false,
      })
    }
  }


</script>


<template>
  <div class="container">
    <el-row :gutter="20" >
        <el-col :span="1">
            <el-avatar :src=userInfo.userAvatar style="height: 40px; width: 40px; line-height: 60px; cursor: pointer;" />
        </el-col>
        <el-col :span="22">
          <div v-if="receiver" class="reply-hint">
            回复 @{{ receiver.username }}
          </div>
            <el-input 
                style="width: 100%;"
                :rows="5"
                v-model=commentText
                type="textarea"
                placeholder="请输入评论内容" clearable />
            <!-- 右对齐， 取消评论 和 发布评论按钮 -->
            <div style="margin: 10px;">
              <el-button @click="handleSubmit" type="primary" size="large" style="float: right; " >发布评论</el-button>
              <el-button @click="handleCancel" type="info" size="large" style="float: right; margin-right: 10px;" plain>取消评论</el-button>
            </div>
        </el-col>
    </el-row>
  </div>
</template>



<style lang="scss" scoped>
  .container {
    background-color: aliceblue;
    padding-bottom: 20px;
    padding-top: 20px;
  }

</style>
