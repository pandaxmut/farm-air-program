<template>
  <div class="containser">
    <h3>文章创作</h3>
    <el-row :gutter="20">
      <el-col :span="14" :offset="2">
        <el-form
            ref="ruleFormRef"
            style="max-width: 700px"
            :model="ruleForm"
            status-icon
            :rules="rules"
            label-width="auto"
            class="demo-ruleForm"
        >
          <el-form-item label="标题" prop="title" >
            <el-input v-model="ruleForm.title"  size="large"/>
          </el-form-item>
          <el-form-item label="简介" prop="summary" >
            <el-input v-model="ruleForm.summary" size="large" />
          </el-form-item>

          <el-row>
            <el-form-item  label="分类" prop="category">
              <el-select
                  v-model="ruleForm.category"
                  placeholder="Select"

                  style="width: 150px"
              >
                <el-option
                    v-for="(category, id) in categorys" :key="id"
                    :label="category"
                    :value="id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="单价" prop="price" >
              <el-input v-model="price"  draggable placeholder="单价为积分" style="width: auto" />
            </el-form-item>
            <el-form-item label="库存" prop="price" >
              <el-input v-model="stock" draggable placeholder="库存" style="width: auto" />
            </el-form-item>

          </el-row>

          <el-form-item>
            <el-button type="primary" @click="submitForm(ruleFormRef)">
              提交
            </el-button>
            <el-button @click="resetForm(ruleFormRef)">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="6">
        <el-form-item label="商品封面">
          <el-upload
              :action="uploadUrl"
              :headers="{'authorization': token}"
              list-type="picture-card"
              :auto-upload="true"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :before-upload="beforeUpload"
              :on-success="handleUploadSuccess"
              :file-list="fileList"
          >
            <el-icon><Plus /></el-icon>
            <template #file="{ file }">
              <div>
                <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                <span class="el-upload-list__item-actions">
              <span
                  class="el-upload-list__item-preview"
                  @click="handlePictureCardPreview(file)"
              >
                <el-icon><zoom-in /></el-icon>
              </span>
              <span
                  class="el-upload-list__item-delete"
                  @click="handleRemove(file)"
              >
                <el-icon><Delete /></el-icon>
              </span>
            </span>
              </div>
            </template>
          </el-upload>

          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
          </el-dialog>
        </el-form-item>
      </el-col>
    </el-row>

  </div>

</template>

<script setup lang="ts"  >
import { ref, reactive, computed } from 'vue';
import {type Article, getAllCategory} from "@/api/articles";
import { createArticle } from "@/api/articles";
import VueMarkdownEditor, { xss } from '@kangc/v-md-editor';
import type { FormInstance, FormRules } from 'element-plus'
import { ElInputTag } from 'element-plus'; // 引入 el-input-tag
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'
import type { UploadFile } from 'element-plus'
import {useUserStore} from "@/stores/userStore";

const userStore = useUserStore();
// 使用 storeToRefs 保持响应性 ✅
const { userId,username, avatarUrl } = userStore

//预览图片相关信息
const uploadUrl = ref('/api/files/files/create');
const dialogVisible = ref(false);  // 控制预览对话框显示
const dialogImageUrl = ref('');    // 预览图片的 URL
const fileList = ref<any[]>([]);   // 用来存储文件列表，显示在上传区域
//上传图片
const token = localStorage.getItem('authorization')

// 上传前检查
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error('只能上传图片文件');
  }
  return isImage;
};

// 处理图片预览
const handlePictureCardPreview = (file: any) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};
``
const handleUploadSuccess = (response: any, file: UploadFile, fileList: UploadFile[]) => {
  console.log('Upload success:', response, file, fileList);
  // 更新 fileList
  fileList.value = fileList;
  file.url = response.data.fileUrl;
  ruleForm.cover = response.data.fileUrl;
};

// 处理文件删除
const handleRemove = (file: any) => {
  console.log('Removed file:', file);
};

const tags = ref<string[]>()
const text = ref('# 主题\n' +
    '请记录你的内容');

// 计算属性，将 Markdown 转换为 HTML 并进行 XSS 过滤
const html = computed(() => xss.process(VueMarkdownEditor.vMdParser.themeConfig.markdownParser.render(text.value)));



//校验数据
const ruleFormRef = ref<FormInstance>()

// const checkTitle = (rule:any,value: any, callback: any) => {
//   if (!value) {
//     return callback(new Error('Please input the title'))
//   }
// }

const categorys = ref({
  1: '农业',
  2: '生活',
  3: '科技',
  4: '其他',
})

const ruleForm = reactive({
  title: '',
  tags: tags,
  category: '',
  summary:'',
  cover: '',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  title: [{ required: true, message: 'Please input title', trigger: 'blur' }],
  tags: [{ required: true, message: 'Please select tag', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select category', trigger: 'blur' }],
  summary: [{ required: true, message: 'Please input summary', trigger: 'blur' }]
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
      // 创建文章对象并符合 Article 接口的结构
      const article: Article = {
        id: "",  // id 应该是一个字符串，可能会由后台生成
        categoryId: ruleForm.category || null,  // 确保 categoryId 是数字或 null
        categoryName: categorys.value[ruleForm.category] || '',  // 需要提供 categoryName
        tags: ruleForm.tags || [],
        userId: userId,  // 用户ID，应该从当前用户信息中获取
        username: username,  // 用户名，同上
        avatarUrl: avatarUrl,
        title: ruleForm.title,
        cover: ruleForm.cover || 'https://img2.baidu.com/it/u=2183926017,1362331649&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=800',  // 这可以是默认封面
        content: text.value,
        summary: ruleForm.summary || null,  // 可选项，可以是 null
        postTime: new Date(),  // 假设这是创建时间
        lastUpdateTime: new Date(),  // 更新时间
        readCount: 0,  // 初始阅读量
        goodCount: 0,  // 初始点赞数
        collectionCount: 0, //初始收藏数
        commentCount: 0,  // 初始评论数
        topType: 0,  // 默认不是置顶
        status: 1,  // 假设 1 表示已发布
      };
      console.log("article:",article)
      createArticle(article).then((res) => {
        console.log(res)
        alert("success")
      })
    } else {
      console.log('error submit!')
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

const fetchCategories  = async () => {
  const { data } = await getAllCategory();
  console.log("data:", data);
  categorys.value = data;
  console.log("赋值之后categorys",categorys.value)
};
fetchCategories()




</script>

<style lang="scss" scoped>
.containser {
  display: block;
  // place-items: center;
  min-height: 100px;
  padding: 20px;
  margin-top: 60px;
  margin-bottom: 10px;
  margin-left: 20px;
  background-color: white;
  border-radius: 10px;
}
</style>