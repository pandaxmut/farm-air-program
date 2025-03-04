<template>
  <div class="containser">
    <h3>新增问答</h3>
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
          <el-row >
            <el-form-item  label="标签" prop="tag">
            <el-select
              v-model="ruleForm.tags"
              placeholder="Select"
              size="large"
              style="width: 240px"
            >
              <el-option
                v-for="item in tags" :key="item.label"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
            <el-form-item  label="模板" prop="template">
              <el-select
                  v-model="ruleForm.template"
                  placeholder="Select"
                  size="large"
                  style="width: 240px"
              >
                <el-option
                    v-for="item  in templates" :key="item.label"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
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
    </el-row>
    <v-md-editor v-model="text" height="400px"></v-md-editor>
    <div>
      <v-md-preview-html :html="html" preview-class="vuepress-markdown-body"></v-md-preview-html>
    </div>
  </div>
  
</template>

<script setup lang="ts"  >
  import { ref, reactive, computed,watch } from 'vue';
  import {type Question, createQuestion,getQuestionTags} from "@/api/questions";
  import VueMarkdownEditor, { xss } from '@kangc/v-md-editor';
  import type { FormInstance, FormRules } from 'element-plus'
  import {useUserStore} from "@/stores/userStore";

  const userStore = useUserStore();
  // 使用 storeToRefs 保持响应性 ✅
  const { userId,username, avatarUrl } = userStore


//校验数据
  const ruleFormRef = ref<FormInstance>()


  const tags = ref([
    {label: "农业", value: 1},
      {label: "畜牧业", value: 2},
      {label: "致富心得", value: 3},
      {label: "创业", value: 4},
      {label: "其他", value: 5},
  ])

  const templates = ref([
    {
      label: "农业模板",
      value: "# 农业模板\n" +
          "\n" +
          "## 介绍\n" +
          "在农业模板中，您将找到关于农业生产、种植技术、作物管理等方面的详细信息，帮助您提升农业生产效率与可持续发展。\n" +
          "\n" +
          "## 主要内容\n" +
          "- **作物种植技术**：介绍不同作物的种植技巧和注意事项。\n" +
          "- **土壤管理**：如何改良土壤，提高土壤肥力。\n" +
          "- **灌溉与排水**：优化灌溉系统和排水系统。\n" +
          "- **农产品市场分析**：对市场需求、价格等方面的分析。\n" +
          "\n" +
          "---"
    },
    {
      label: "畜牧业模板",
      value: "# 畜牧业模板\n" +
          "\n" +
          "## 介绍\n" +
          "畜牧业模板提供了关于养殖业的核心知识，包括家禽、家畜的养殖技术、疾病防控等，帮助您建立健康、可持续的养殖系统。\n" +
          "\n" +
          "## 主要内容\n" +
          "- **动物饲养技术**：如何合理配制饲料，保障动物的营养需求。\n" +
          "- **动物健康管理**：常见的动物疾病及其预防与治疗方法。\n" +
          "- **养殖设备**：适用于不同规模养殖场的设备推荐。\n" +
          "- **畜牧产品市场趋势**：了解畜牧产品的市场动态，掌握价格走势。\n" +
          "\n" +
          "---"
    },
    {
      label: "创业模板",
      value: "# 创业模板\n" +
          "\n" +
          "## 介绍\n" +
          "创业模板提供了成功创业所需的各种资源，包括商业计划书、资金管理、市场营销策略等内容，帮助创业者顺利启动并发展自己的事业。\n" +
          "\n" +
          "## 主要内容\n" +
          "- **商业计划书**：如何编写一份清晰、可行的商业计划。\n" +
          "- **资金管理与融资**：如何管理资金、寻找投资和融资渠道。\n" +
          "- **市场营销**：制定市场营销战略，提升品牌知名度。\n" +
          "- **法律与税务**：了解创业所需的法律基础和税务管理。\n" +
          "\n" +
          "---"
    },
    {
      label: "致富心得",
      value: "# 致富心得\n" +
          "\n" +
          "## 介绍\n" +
          "致富心得模板包含了一些成功的商业经验、投资理财技巧及实现财富自由的方法，帮助您在财富管理和投资中做出明智决策。\n" +
          "\n" +
          "## 主要内容\n" +
          "- **成功案例**：通过成功人士的经历，学习如何致富。\n" +
          "- **投资理财**：理财的基本原则，股票、基金等投资产品的选择。\n" +
          "- **时间管理与效率提升**：如何合理规划时间，提高工作效率。\n" +
          "- **风险管理**：识别和应对财务、投资过程中的风险。\n" +
          "\n" +
          "---"
    },
    {
      label: "生活随笔",
      value: "# 生活随笔\n" +
          "\n" +
          "## 介绍\n" +
          "生活随笔模板记录了日常生活中的感悟和思考，包含个人经历、情感故事以及对社会、生活的观察与反思。\n" +
          "\n" +
          "## 主要内容\n" +
          "- **个人成长**：分享自我提升和成长的过程。\n" +
          "- **旅行与探索**：记录旅行中的见闻与感悟。\n" +
          "- **人际关系**：关于家庭、朋友、职场的人际交往技巧。\n" +
          "- **心灵鸡汤**：关于生活中的一些哲理和人生智慧。\n" +
          "\n" +
          "---"
    }
  ]);


  const ruleForm = reactive({
    title: '',
    tags: '',
    summary:'',
    template: '',
  })
  const text = ref("# hello");

  // 监视 ruleForm.template 的变化，确保 text 内容更新
  watch(() => ruleForm.template, (newTemplate) => {
    text.value = newTemplate; // 更新 text 内容
  });

  // 计算属性，将 Markdown 转换为 HTML 并进行 XSS 过滤
  const html = computed(() => xss.process(VueMarkdownEditor.vMdParser.themeConfig.markdownParser.render(text.value)));

  const rules = reactive<FormRules<typeof ruleForm>>({
    title: [{ required: true, message: 'Please input title', trigger: 'blur' }],
    tags: [{ required: true, message: 'Please select tag', trigger: 'blur' }],
    summary: [{ required: true, message: 'Please input summary', trigger: 'blur' }]
  })

  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        console.log('submit!')
        console.log("sub", ruleForm.tags);
        // 创建文章对象并符合 Article 接口的结构
        const question: Question = {
          id: "",  // id 应该是一个字符串，可能会由后台生成
          tagId: ruleForm.tags || null,  // 直接使用 ruleForm.tags 即可
          tagName: tags.value.find(tag => tag.value === ruleForm.tags)?.label || null,  // 获取选中的标签名称
          userId: userId,  // 用户ID，应该从当前用户信息中获取
          username: username,  // 用户名，同上
          avatar: avatarUrl,
          title: ruleForm.title,
          content: text.value,
          summary: ruleForm.summary || null,  // 可选项，可以是 null
          postTime: new Date(),  // 假设这是创建时间
          readCount: 0,  // 初始阅读量
          commentCount: 0,  // 初始评论数
          topType: 0,  // 默认不是置顶
          status: 1,  // 假设 1 表示已发布
        };
        console.log("article:",question)
         createQuestion(question).then((res) => {
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

  const fetchTags = async () => {
    const response = await getQuestionTags();
    const {data} = response;


    tags.value = [];  // 清空tags数组
    // 如果 data 为空或 undefined，打印日志并返回
    if (!data.data || data.data.length === 0) {
      console.log("data is empty or undefined");
      return;
    }else {
      console.log("data is not empty");
      const number = data.data.length;
      console.log(number)
    }

    // 使用 for 循环来迭代每个data中的元素
    for (let i = 0; i < data.data.length; i++) {
      console.log('data[i]', data.data[i].name);  // 打印每个标签的名称

      tags.value[i] = {
        label: data.data[i].name,  // 正确地访问数据
        value: data.data[i].id     // 正确地访问数据
      };
    }

    console.log("赋值之后tags", tags.value);
  };

  fetchTags()




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