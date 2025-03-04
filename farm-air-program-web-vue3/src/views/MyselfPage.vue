<script setup lang="ts">
import {ref,markRaw} from "vue";
import {
  ElContainer,
  ElAside,
  ElMenu,
  ElMenuItem,
  ElMain,
  ElTimeline,
  ElTimelineItem,
  ElCard,
  ElAvatar,
  ElButton,
  ElAlert, ElNotification, ElInputTag
} from 'element-plus';
import {type User, updateUser,updateUserPassword,updateUserRole} from "@/api/users"
import {type ArticleColumns, getSearchArticle,getSearchColumns} from "@/api/articles"
import {useUserStore} from "@/stores/userStore";
const userStore = useUserStore();

const user = userStore.getUser;  // 获取用户数据并初始化
const index = ref('1');
console.log("user11111111111111111",user)

//更新个人信息
const saveProfile = async () => {
    console.log("user", user.username)

    const clonedUser = JSON.parse(JSON.stringify(userStore.$state));
    const {data} = await updateUser(clonedUser)


  // 更新用户信息
    if (data) {
      //alert
      ElAlert('更新成功', 'success')
    }
}

const updateRole = async () => {

}


import { ref, reactive, computed } from 'vue'
import {useRouter} from "vue-router";
import type { FormInstance, FormRules } from 'element-plus'
import type {Article} from "@/api/articles";
const router = useRouter()

const dialogVisible = ref(false)
const active = ref(0)
const submitting = ref(false)



// 表单数据
const formData = reactive({
  password: '',
  role: ''
})

// 表单引用
const step1Form = ref<FormInstance>()
const step2Form = ref<FormInstance>()

// 角色选项
const roleOptions = [
  { value: '0', label: '普通用户' },
  { value: '1', label: '教授' },
  { value: '2', label: '农民' }
]

// 显示角色名称
const showRoleName = computed(() => {
  return roleOptions.find(item => item.value === formData.role)?.label || ''
})

// 下一步
const next = async () => {
  if (active.value === 0) {
    await step1Form.value?.validate()
  } else if (active.value === 1) {
    await step2Form.value?.validate()
  }

  if (active.value < 2) {
    active.value++
  } else {
    submitting.value = true
    // 这里执行提交操作
    const data = await updateUserRole(formData.password,formData.role)
    // 检查返回结果
    if (data.code === 200) {
      ElNotification({
        title: "Success",
        message: "更新成功",
        type: "success",
      });
      user.role = formData.role
    } else {
      ElNotification({
        title: "Error",
        message: "密码错误",
        type: "error",
      });
    }
    setTimeout(() => {
      submitting.value = false
      dialogVisible.value = false
      ElMessage.success('角色变更成功！')
    }, 1000)
  }
}

// 上一步
const prev = () => {
  if (active.value > 0) active.value--
}

// 关闭对话框时重置
const handleClose = () => {
  active.value = 0
  formData.password = ''
  formData.role = ''
}



//修改密码逻辑

const ruleFormRef = ref<FormInstance>()

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password'))
  } else {
    if (ruleForm.checkPass !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPass')
    }
    callback()
  }
}
const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password again'))
  } else if (value !== ruleForm.pass) {
    callback(new Error("Two inputs don't match!"))
  } else {
    callback()
  }
}

const ruleForm = reactive({
  oldPass:'',
  pass: '',
  checkPass: '',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  pass: [{ validator: validatePass, trigger: 'blur' }],
  checkPass: [{ validator: validatePass2, trigger: 'blur' }],
})

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;

  // 验证表单
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        // 调用更新密码的异步方法
        const passwordDataCheck = await updateUserPassword(
            user.userId,
            ruleForm.oldPass,
            ruleForm.pass
        );
        console.log("passwordDataCheck", passwordDataCheck);

        // 检查返回结果
        if (passwordDataCheck.code === 200) {
          ElNotification({
            title: "Success",
            message: "更新成功",
            type: "success",
          });
        } else {
          ElNotification({
            title: "Error",
            message: "密码错误",
            type: "error",
          });
        }
      } catch (error) {
        // 捕获异步请求中的错误
        console.error("Error updating password:", error);
        ElNotification({
          title: "Error",
          message: "更新失败，请稍后再试",
          type: "error",
        });
      }
    } else {
      console.log("error submit!");
      ElNotification({
        title: "Error",
        message: "表单验证失败，请检查输入",
        type: "error",
      });
    }
  }).catch((error) => {
    // 捕获表单验证过程中可能抛出的错误
    console.error("Form validation error:", error);
    ElNotification({
      title: "Error",
      message: "表单验证失败，请检查输入",
      type: "error",
    });
  });
};

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}



//我的文章

const searchForm = ref({
  articleType: '1',
  articleName: '',

})
const articleTypeOptions = [
  {
    value: '1',
    label: '全部',
  },
  {
    value: '2',
    label: '审核中',
  },
  {
    value: '3',
    label: '已发布',
  },
  {
    value: '4',
    label: '已下架',
  },
]
const articleList = ref<Article[]>([])
const searchArticle = async () => {
  console.log("searchForm", searchForm)
  const {data} = await getSearchArticle(searchForm.value.articleName, searchForm.value.articleType)
  // 确保 data 是一个数组
  if (Array.isArray(data)) {
    // 更新 articleList
    articleList.value = data; // 直接更新 ref 的值
  } else {
    console.error("Unexpected data format:", data);
    throw new Error("Invalid data format received from API");
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

//我的专栏
const dialogVisibleColumns = ref(false)
import { ElTable } from 'element-plus'
import {type Article, type ArticleColumns, getAllCategory} from "@/api/articles";
import { createArticleColumns } from "@/api/articles";
import type { FormInstance, FormRules } from 'element-plus'
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'
import type { UploadFile } from 'element-plus'

//预览图片相关信息
const uploadUrl = ref('/api/files/files/create');
const dialogVisiblePhoto = ref(false);  // 控制预览对话框显示
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
  dialogVisiblePhoto.value = true;
};
``
const handleUploadSuccess = (response: any, file: UploadFile, fileList: UploadFile[]) => {
  console.log('Upload success:', response, file, fileList);
  // 更新 fileList
  fileList.value = fileList;
  file.url = response.data.fileUrl;
  columnsRuleForm.cover = response.data.fileUrl;
  console.log("columnsRuleForm.cover", columnsRuleForm.cover);
};

// 处理文件删除
const handleRemove = (file: any) => {
  console.log('Removed file:', file);
};

//校验数据
const columnsRuleFormRef = ref<FormInstance>()

const categorys = ref({
  1: '农业',
  2: '生活',
  3: '科技',
  4: '其他',
})

const columnsRuleForm = reactive({
  title: '',
  category: '',
  summary:'',
  cover: '',
})

const columnsRules = reactive<FormRules<typeof columnsRuleForm>>({
  title: [{ required: true, message: 'Please input title', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select category', trigger: 'blur' }],
  summary: [{ required: true, message: 'Please input summary', trigger: 'blur' }]
})
const articlesSelection = ref([])
const articleColumnsList = ref<ArticleColumns[]>([])
const handleSelectionChange = (selection) => {
  articlesSelection.value = selection
  console.log('Selected Rows:', articlesSelection.value)
  if (articlesSelection.value.length > 0) {
    articleColumnsList.value = articlesSelection.value.map((article) => {
      return {
        id: article.id,
        title: article.title,
      }
    })
  }
}
const columnsSubmitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
      // 创建文章对象并符合 Article 接口的结构
      const articleColumns: ArticlesColumns = {
        id: "",  // id 应该是一个字符串，可能会由后台生成
        title: columnsRuleForm.title,
        categoryId: columnsRuleForm.category || null,  // 确保 categoryId 是数字或 null
        categoryName: categorys.value[columnsRuleForm.category] || '',  // 需要提供 categoryName
        userId: user.userId,  // 用户ID，应该从当前用户信息中获取
        username: user.username,  // 用户名，同上
        avatarUrl: user.avatarUrl,
        cover: columnsRuleForm.cover || 'https://img2.baidu.com/it/u=2183926017,1362331649&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=800',  // 这可以是默认封面
        summary: columnsRuleForm.summary || null,  // 可选项，可以是 null
        postTime: new Date(),  // 假设这是创建时间
        lastUpdateTime: new Date(),  // 更新时间
        readCount: 0,  // 初始阅读量
        collectionCount: 0, //收藏数
        status: 1,  // 假设 1 表示已发布
        articles: articleColumnsList.value
      };

      createArticleColumns(articleColumns).then((res) => {
        console.log(res)
        alert("success")
      })
    } else {
      console.log('error submit!')
    }
  })
}

const columnsResetForm = (formEl: FormInstance | undefined) => {
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



//查看专栏
const searchFormColumns = ref({
  columnsType: '1',
  columnsName: '',

})
const columnsTypeOptions = [
  {
    value: '1',
    label: '全部',
  },
  {
    value: '2',
    label: '审核中',
  },
  {
    value: '3',
    label: '已发布',
  },
  {
    value: '4',
    label: '已下架',
  },
]
const columnsList = ref<Article[]>([])
const searchColumns = async () => {
  console.log("searchFormColumns", searchFormColumns)
  const {data} = await getSearchColumns(searchFormColumns.value.columnsName, searchFormColumns.value.columnsType)
  // 确保 data 是一个数组
  if (Array.isArray(data)) {
    // 更新 articleList
    columnsList.value = data; // 直接更新 ref 的值
  } else {
    console.error("Unexpected data format:", data);
    throw new Error("Invalid data format received from API");
  }

}


function goToColumnsDetail(id:number) {
  router.push({
    name: 'ArticlesView',
    params: {
      id: id,
    },
  });
}



</script>

<template>
  <el-container style="height: 100vh;">
    <!-- 侧边栏 -->
    <el-aside width="100px" style="background-color: #f6f7fa;">
      <el-menu
          style="background-color: #f6f7fa;;"
          :default-active="index"
          class="el-menu-vertical-demo"
      >
        <el-menu-item index="1" @click="index = '1'">个人中心</el-menu-item>
        <el-menu-item index="2" @click="index = '2'">账号绑定</el-menu-item>
        <el-menu-item index="3" @click="index = '3'">我的评论</el-menu-item>
        <el-menu-item index="4" @click="index = '4'">我的收藏</el-menu-item>
        <el-menu-item index="5" @click="index = '5'">积分明细</el-menu-item>
        <el-menu-item index="6" @click="index = '6'">我的文章</el-menu-item>
        <el-menu-item index="7" @click="index = '7'">我的专栏</el-menu-item>
        <el-menu-item index="8" @click="index = '8'">修改密码</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->

    <el-main>
      <!-- 内容区1 -->
      <div v-if="index === '1'" style="padding: 20px;">
        <h2>个人中心</h2>
        <!-- 你可以在这里放置个人中心的具体内容 -->
        <el-row :gutter="20">
          <el-col :span="6">
            <!-- 头像 -->
            <el-upload
                class="avatar-uploader"
                action="#"
                show-file-list="false"
                :auto-upload="false"
            >
              <img
                  v-if="user"
                  :src="user.avatarUrl"
                  class="avatar"
                  alt="头像"
              />
              <el-button
                  v-else
                  size="small"
                  type="primary"
                  icon="el-icon-upload"
              >上传头像</el-button>
            </el-upload>
          </el-col>
          <el-col :span="18">
            <el-form :model="user">
              <!-- 昵称 -->
              <el-form-item label="昵称">
                <el-input v-model="user.username" placeholder="请输入昵称" />
              </el-form-item>

              <!-- 性别选择 -->
              <el-form-item label="性别">
                <el-radio-group v-model="user.sex">
                  <el-radio :value="1">男</el-radio>
                  <el-radio :value="0">女</el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- 钱包余额 -->
              <el-form-item label="钱包余额">
                <el-input  disabled placeholder="0 元" />
              </el-form-item>

              <!-- 邮件通知 -->
              <el-form-item label="邮件通知">
                <el-switch  active-text="是" inactive-text="否" />
              </el-form-item>


              <!-- 邮箱 -->
              <el-form-item label="邮箱">
                <el-input v-model="user.email" placeholder="请输入邮箱" />
              </el-form-item>

              <!-- 职业 -->
              <el-form-item label="角色">
                <el-row>
                  <el-col :span="18">
                    <el-input v-if="user.role == 0" disabled placeholder="普通用户"/>
                    <el-input v-else-if="user.role == 1" disabled placeholder="教授"/>
                    <el-input v-else= disabled placeholder="农业从事者"/>
                  </el-col>
                  <el-col :offset="2" :span="4" >
<!--                    <el-button type="primary" @click="updateRole">变更</el-button>-->
                    <el-button @click="dialogVisible = true">变更角色</el-button>
                  </el-col>
                </el-row>

              </el-form-item>

              <!-- 简介 -->
              <el-form-item label="简介">
                <el-input
                    v-model="user.description"
                    type="textarea"
                    placeholder="请输入个人简介"
                    rows="4"
                />
              </el-form-item>

              <el-button type="primary" @click="saveProfile">保存</el-button>
            </el-form>
          </el-col>
        </el-row>
        <!-- 对话框 -->
        <el-dialog
            v-model="dialogVisible"
            title="角色变更"
            width="600px"
            @closed="handleClose"
        >
          <!-- 步骤条 -->
          <el-steps :active="active" align-center>
            <el-step title="身份验证" />
            <el-step title="选择角色" />
            <el-step title="确认信息" />
          </el-steps>

          <!-- 步骤内容 -->
          <div class="step-content">
            <!-- 第一步：身份验证 -->
            <el-form
                v-show="active === 0"
                ref="step1Form"
                :model="formData"
                label-width="100px"
            >
              <el-form-item
                  label="密码"
                  prop="password"
                  :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]"
              >
                <el-input v-model="formData.password" type="password" />
              </el-form-item>
            </el-form>

            <!-- 第二步：选择角色 -->
            <el-form
                v-show="active === 1"
                ref="step2Form"
                :model="formData"
                label-width="100px"
            >
              <el-form-item
                  label="新角色"
                  prop="role"
                  :rules="[{ required: true, message: '请选择角色', trigger: 'change' }]"
              >
                <el-select v-model="formData.role" placeholder="请选择">
                  <el-option
                      v-for="item in roleOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-form>

            <!-- 第三步：确认信息 -->
            <div v-show="active === 2" class="confirm-info">
              <p>密码：******</p>
              <p>新角色：{{ showRoleName }}</p>
            </div>
          </div>

          <!-- 操作按钮 -->
          <template #footer>
            <el-button @click="prev" :disabled="active === 0">上一步</el-button>
            <el-button
                type="primary"
                @click="next"
                :loading="submitting"
            >
              {{ active === 2 ? '提交' : '下一步' }}
            </el-button>
          </template>
        </el-dialog>
      </div>

      <!-- 内容区2 -->
      <div v-if="index === '2'">
        <h2>账号绑定内容</h2>
        <!-- 你可以在这里放置账号绑定的具体内容 -->
      </div>

      <!-- 其他内容区... -->
      <div v-if="index === '3'">
        <h2>我的评论</h2>
        <el-main>
          <el-timeline>
            <el-timeline-item timestamp="1周前" placement="top">
              <el-card class="box-card">
                <div class="clearfix">
                  <el-avatar :src="'https://img.icons8.com/ios/452/cat.png'" size="medium" class="avatar"></el-avatar>
                  <div class="text">
                    <h3>熊猫与月亮</h3>
                    <el-button size="mini" type="text" class="button">博客详情</el-button>
                    <p>呵呵</p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="1周前" placement="top">
              <el-card class="box-card">
                <div class="clearfix">
                  <el-avatar :src="'https://img.icons8.com/ios/452/cat.png'" size="medium" class="avatar"></el-avatar>
                  <div class="text">
                    <h3>熊猫与月亮</h3>
                    <el-button size="mini" type="text" class="button">博客详情</el-button>
                    <p>酸酸</p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="3个月前" placement="top">
              <el-card class="box-card">
                <div class="clearfix">
                  <el-avatar :src="'https://img.icons8.com/ios/452/cat.png'" size="medium" class="avatar"></el-avatar>
                  <div class="text">
                    <h3>随溪</h3>
                    <el-button size="mini" type="text" class="button">用户动态</el-button>
                    <p>水到Lv6了</p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-main>
      </div>
      <!-- 我的文章  显示文章列表 -->
      <div v-if="index === '6'">
        <h2>我的文章</h2>

        <el-main>
          <el-row :gutter="20">
            <el-col :span="12" >
              <!--     输入博客名         -->
              <el-input v-model="searchForm.articleName" placeholder="请输入文章名"> </el-input>
            </el-col>
            <el-col :span="8" >
              <el-select v-model="searchForm.articleType" placeholder="请选择文章类型">
                <el-option
                    v-for="item in articleTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-col>
            <el-col :span="4" >
              <el-button type="primary" @click="searchArticle">搜索</el-button>
            </el-col>
          </el-row>

          <el-timeline  v-if="articleList.length>0" style="padding-top: 20px" >
            <el-timeline-item v-for="article in articleList" :key="article.id"  :timestamp="article.postTime" placement="top">
              <el-card class="box-card">
                <div class="clearfix">
                  <el-avatar :src="article.cover" size="medium" class="avatar"></el-avatar>
                  <div class="text">
                    <h3>{{article.title}}</h3>
                    <el-button @click="goToArticleDetail(article.id)"  size="mini"  type="text" class="button">博客详情</el-button>

                    <p>{{article.description}}</p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-main>
      </div>
      <div v-if="index ==='7'">
        <h2>我的专栏</h2>
        <el-main>
          <el-row :gutter="20">
            <el-col :span="8">

              <el-button plain @click="dialogVisibleColumns = true" size="large">
                添加专栏
              </el-button>
              <el-dialog
                  v-model="dialogVisibleColumns"
                  fullscreen
                  top="40vh"
                  width="70%"
                  draggable
              >
                <h2>添加专栏</h2>
                <el-main>

                  <el-row :gutter="20">
                    <el-col :span="14" :offset="2">
                      <el-form
                          ref="columnsRuleFormRef"
                          style="max-width: 700px"
                          :model="columnsRuleForm"
                          status-icon
                          :rules="columnsRules"
                          label-width="auto"
                          class="demo-ruleForm"
                      >
                        <el-form-item label="专栏名称" prop="title" >
                          <el-input v-model="columnsRuleForm.title"  size="large"/>
                        </el-form-item>
                        <el-form-item label="简介" prop="summary" >
                          <el-input v-model="columnsRuleForm.summary" size="large" />
                        </el-form-item>
                        <el-row >
                          <el-form-item  label="分类" prop="category">
                            <el-select
                                v-model="columnsRuleForm.category"
                                placeholder="Select"
                                size="large"
                                style="width: 240px"
                            >
                              <el-option
                                  v-for="(category, id) in categorys" :key="id"
                                  :label="category"
                                  :value="id"
                              />
                            </el-select>
                          </el-form-item>

                        </el-row>
                        <el-form-item>
                          <el-button type="primary" @click="columnsSubmitForm(columnsRuleFormRef)">
                            提交
                          </el-button>
                          <el-button @click="columnsResetForm(columnsRuleFormRef)">重置</el-button>
                        </el-form-item>
                      </el-form>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item label="标题封面">
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

                        <el-dialog v-model="dialogVisiblePhoto">
                          <img w-full :src="dialogImageUrl" alt="Preview Image" />
                        </el-dialog>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="20" style="margin-bottom: 20px">
                    <el-col :span="8"  :offset="2" >
                      <!--     输入博客名         -->
                      <el-input v-model="searchForm.articleName" placeholder="请输入文章名"> </el-input>
                    </el-col>
                    <el-col :span="3" >
                      <el-select v-model="searchForm.articleType" placeholder="请选择文章类型">
                        <el-option
                            v-for="item in articleTypeOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                      </el-select>
                    </el-col>
                    <el-col :span="4" >
                      <el-button type="primary" @click="searchArticle">搜索</el-button>
                    </el-col>
                  </el-row>
                  <el-table
                      v-if="articleList.length>0"
                      :data="articleList"
                      @selection-change="handleSelectionChange"
                      style="width: 100%;height: 200px">
                    <el-table-column type="selection" width="55" />
                    <el-table-column fixed label="Title" width="180">
                      <template #default="scope">{{ scope.row.title }}</template>
                    </el-table-column>
                    <el-table-column
                        property="categoryName"
                        label="categoryName"
                        width="240"
                        show-overflow-tooltip
                    />
                    <el-table-column
                        property="lastUpdateTime"
                        label="lastUpdateTime"
                        width="240"
                        show-overflow-tooltip
                    />
                    <el-table-column
                        property="summary"
                        label="summary"
                        width="240"
                        show-overflow-tooltip
                    />
                    <el-table-column
                        property="readCount"
                        label="readCount"
                        width="240"
                        show-overflow-tooltip
                    />
                    <el-table-column
                        property="goodCount"
                        label="goodCount"
                        width="240"
                        show-overflow-tooltip
                    />
                  </el-table>

                </el-main>
                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="dialogVisibleColumns = false">Cancel</el-button>
                    <el-button type="primary" @click="dialogVisibleColumns = false">
                      Confirm
                    </el-button>
                  </div>
                </template>
              </el-dialog>
            </el-col>
            <el-col :span="8">
              <el-button @click="goToArticleDetail('')">查看专栏</el-button>
            </el-col>
            <el-col :span="8">
              <el-button @click="goToArticleDetail('')">下架专栏</el-button>
            </el-col>
          </el-row>
        </el-main>
        <el-main>
          <el-row :gutter="20">
            <el-col :span="12" >
              <!--     输入博客名         -->
              <el-input v-model="searchFormColumns.columnsName" placeholder="请输入专栏名"> </el-input>
            </el-col>
            <el-col :span="8" >
              <el-select v-model="searchFormColumns.columnsType" placeholder="请选择专栏类型">
                <el-option
                    v-for="item in columnsTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-col>
            <el-col :span="4" >
              <el-button type="primary" @click="searchColumns">搜索</el-button>
            </el-col>
          </el-row>

          <el-timeline  v-if="columnsList.length>0" style="padding-top: 20px" >
            <el-timeline-item v-for="columns in columnsList" :key="columns.id"  :timestamp="columns.postTime" placement="top">
              <el-card class="box-card">
                <div class="clearfix">
                  <el-avatar :src="columns.cover" size="medium" class="avatar"></el-avatar>
                  <div class="text">
                    <h3>{{columns.title}}</h3>
                    <el-button @click="goToColumnsDetail(columns.id)"  size="mini"  type="text" class="button">博客详情</el-button>

                    <p>{{columns.summary}}</p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-main>



      </div>
      <div v-if="index === '8'">
        <h2>修改密码</h2>
        <el-main>
          <div>
            <el-form
                ref="ruleFormRef"
                style="max-width: 600px"
                :model="ruleForm"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
            >
              <el-form-item label="原密码" prop="oldPass">
                <el-input v-model.number="ruleForm.oldPass"  placeholder="请输入密码，第一次则不用填写" />
              </el-form-item>
              <el-form-item label="Password" prop="pass">
                <el-input v-model="ruleForm.pass" type="password" autocomplete="off"  placeholder="请输入新密码" />
              </el-form-item>
              <el-form-item label="Confirm" prop="checkPass" >
                <el-input
                    v-model="ruleForm.checkPass"
                    type="password"
                    autocomplete="off"
                    placeholder="确认密码"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm(ruleFormRef)">
                  Submit
                </el-button>
                <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
              </el-form-item>
            </el-form>
          </div>

        </el-main>
      </div>

      <!-- 更多内容项，根据activeIndex的值切换显示 -->
    </el-main>
  </el-container>
</template>




<style scoped lang="scss">


.el-menu-item.is-active {
  background-color: #ffffff !important;  /* 设置选中项的背景色为白色 */
}
.el-menu-vertical-demo {
  border-right: none;
}

.box-card {
  margin-top: 10px;
  margin-bottom: 10px;
}

.clearfix {
  display: flex;
  align-items: center;
}

.avatar {
  margin-right: 10px;
}

.text {
  flex: 1;
}

.button {
  margin-top: 10px;
}

.step-content {
  margin: 24px 0;
  min-height: 150px;
}

.confirm-info p {
  line-height: 2;
  margin: 20px 0;
}
</style>