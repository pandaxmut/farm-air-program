<script setup>

import test from '@/views/login/test.vue'
import { ref,onMounted } from "vue";
import { ElMessage } from "element-plus";



//api
import { login,sendEmailCode,feishuLogin } from "@/api/login.ts"


const activeTab = ref("email");

const formPhone = ref({
  phone: "18706038070",
  code: "9999",
});

const formEmail = ref({
  email: "linyhpanda@163.com",
  code: "9999",
});

const emailRules = {
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
  ],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
};

const countdown = ref(0);

let timer = null;

const phoneRules = {
  phone: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "手机号码格式不正确", trigger: "blur" },
  ],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
};

// 模拟发送验证码
const sendPhoneCode = () => {
  if (!formPhone.value.phone) {
    ElMessage.warning("请先输入手机号");
    return;
  }
  if (countdown.value > 0) return;
  console.log("发送验证码");


  countdown.value = 60;
  ElMessage.success("验证码发送成功");
  timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
};

// 模拟手机登录
const loginByPhone = () => {

  ElMessage.success("登录成功");
};


// 模拟发送验证码
const sendEmailCode1 = () => {
  if (!formEmail.value.email) {
    ElMessage.warning("请先输入邮箱");
    return;
  }
  if (countdown.value > 0) return;
  countdown.value = 60;
  const res = sendEmailCode(formEmail.value.email)
  if (res == null){
    ElMessage.warning("发送失败");
    return;
  }
  ElMessage.success("验证码发送成功");
  timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
};



// 模拟手机登录
const loginByEMail = () => {
  const response = login(0,formEmail.value.email,formEmail.value.code)
  if (response !=null){
    ElMessage.success("登录成功");
  //   跳转到首页
  //   window.location.href = "/";
  }else {
    ElMessage.warning("登录失败");
  }
};

//飞书登录
const feishuCode = ref(''); // 飞书二维码URL
const centerDialogVisible = ref(false)

async function getFeishuCode() {
  centerDialogVisible.value=true;
  const res = await feishuLogin();
  if (res.code == 200 && res.data != null){
    feishuCode.value = res.data;
    window.location.href = feishuCode.value;
  }else{
    console.log('feishuCode error')
  }
}

</script>
<template>
  <div class="login-container">
    <el-card class="login-card gradient-background">

      <el-row>
        <!-- 登录旁白边栏 -->
        <el-col :span="8">
          <el-row :gutter="20">
            <el-col :span="4">
              <div class="logo" style="padding-top: 6px;">
                <img src="@/assets/images/助农.svg" alt="logo" height="36"/>
              </div>
            </el-col>
            <el-col :span="20">
              <div >
                <h3 class="title">助农</h3>
                <h5  class="subtitle">从群众中来到群众中去</h5>
              </div>
            </el-col>
            
          </el-row>

          <el-row :gutter="20" style="padding-top: 70px;">
            <el-col :span="4">
              <div class="logo" style="padding-top: 6px;">
                <img src="@/assets/images/沟通.svg" alt="logo" height="36"/>
              </div>
            </el-col>
            <el-col :span="20">
              <div >
                <h3 class="title1">沟通</h3>
                <h5  class="subtitle1">连接每一户家庭</h5>
              </div>
            </el-col>
            
          </el-row>

          <el-row :gutter="20" style="padding-top: 70px;">
            <el-col :span="4">
              <div class="logo" style="padding-top: 6px;">
                <img src="@/assets/images/健康.svg" alt="logo" height="36"/>
              </div>
            </el-col>
            <el-col :span="20">
              <div >
                <h3 class="title1">健康</h3>
                <h5  class="subtitle1">保护每一个家庭的饮食健康</h5>
              </div>
            </el-col>
            
          </el-row>
        </el-col>
        <!-- TODO: 登录  拦截器,邮箱登录|手机号登录|飞书登录 -->
        <el-col :span="15" :offset="1">
          <H2 style="text-align: center; padding-bottom: 10px;">
             登录 注册
          </H2>
          <div class="tabs">
            
            <el-tabs v-model="activeTab" tab-position="top" stretch="true">

              <el-tab-pane label="邮箱登录" name="email">
                <div class="tab-content">
                  <el-form :model="formEmail" ref="emailForm" :rules="emailRules" label-width="0" size="large">
                    <el-form-item prop="email" >
                      <el-input
                        v-model="formEmail.email"
                        placeholder="请输入邮箱号码"
                        clearable
                        
                      >
                        <template #prepend>email</template>
                        {{formEmail.email}}
                      </el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                      <el-input
                        v-model="formEmail.code"
                        placeholder="短信验证码"
                        clearable
                      >
                        <template #append>
                          <el-button
                            :disabled="countdown > 0"
                            @click="sendEmailCode1"
                            size="small"
                          >
                            {{ countdown > 0 ? `${countdown}s后重试` : '发送验证码' }}
                          </el-button>
                        </template>
                      </el-input>
                    </el-form-item>
                  </el-form>
                  <el-button
                    type="danger"
                    block
                    @click="loginByEMail"
                    plain
                    size="large"
                    style="width: 100%"
                  >
                    登录/注册
                  </el-button>
                </div>
              </el-tab-pane>
              <el-tab-pane label="手机号码登录" name="phone">
                <div class="tab-content">
                  <el-form :model="formPhone" ref="phoneForm" :rules="phoneRules" label-width="0" size="large">
                    <el-form-item prop="phone">
                      <el-input
                        v-model="formPhone.phone"
                        placeholder="请输入手机号码"
                        clearable
                      >
                        <template #prepend>+86</template>
                      </el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                      <el-input
                        v-model="formPhone.code"
                        placeholder="短信验证码"
                        clearable
                      >
                        <template #append>
                          <el-button
                            :disabled="countdown > 0"
                            @click="sendPhoneCode"
                            size="small"
                          >
                            {{ countdown > 0 ? `${countdown}s后重试` : '发送验证码' }}
                          </el-button>
                        </template>
                      </el-input>
                    </el-form-item>
                  </el-form>
                  <el-button
                    type="success"
                    block
                    @click="loginByPhone"
                    plain
                    size="large"
                    style="width: 100%"
                  >
                    登录/注册
                  </el-button>
                </div>
              </el-tab-pane>
              <el-tab-pane label="飞书扫码登录" name="feishu" >
                <div class="tab-content feishu-content">
<!--                  <el-button  type="primary" plain size="large" style="width: 100%" @click="getFeishuCode">-->
<!--                    获取二维码-->
<!--                  </el-button>-->

<!--                  <el-button  type="danger" plain size="large" style="width: 100%; height:200px" @click="getFeishuCode" >-->
<!--                    获取二维码-->
<!--                  </el-button>-->
                      <el-button  type="danger" plain size="large" style="width: 100%; height:200px" @click="getFeishuCode" >
                        获取二维码
                      </el-button>
<!--                  <el-dialog v-model="centerDialogVisible" title="获取二维码" width="50%" height="50%" center>-->

<!--                    <template #footer>-->
<!--                      <div class="dialog-footer">-->
<!--                        <div class="qr-code" >-->
<!--                          <iframe :src="feishuCode" width="100%" height="500px" frameborder="0"></iframe>-->
<!--                        </div>-->
<!--                      </div>-->
<!--                    </template>-->
<!--                  </el-dialog>-->

                  <p class="hint">请使用飞书扫描二维码登录</p>
                </div>
              </el-tab-pane>

            </el-tabs>
          </div>
        </el-col>
      </el-row>
         
    </el-card>
    
  </div>
</template>


<style lang="scss" scoped>

.title {
  color: #8ad395 /* 红色 */
}

.subtitle {
  color: #8ad395; /* 蓝色 */
}

.title1 {
  color: #8a8a8a /* 红色 */
}

.subtitle1 {
  color: #8a8a8a; /* 蓝色 */
}

.gradient-background {
  background: linear-gradient(135deg, #ebfafd, #f8f6f4);
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
  

  .login-card {

    width: 700px;
    min-height: 500px;
    padding: 20px;
    padding-top: 50px;
    padding-left: 5px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .tabs {
    .el-tabs__item {
      font-weight: bold;
    }

    .tab-content {
      margin-top: 20px;
    }

    .feishu-content {
      text-align: center;

      .qr-code {
        margin-bottom: 10px;

        img {
          width: 150px;
          height: 150px;
        }
      }

      .hint {
        color: #666;
      }
    }
  }
}
</style>
