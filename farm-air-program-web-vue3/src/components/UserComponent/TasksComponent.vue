<template>
    <el-card class="cardStyle is-always-shadow">
      <template #header>
        <div class="clearfix">
          <span style="font-weight: 600; font-size: 18px;">任务广场</span>
        </div>
      </template>
      <!-- TODO ：card body padding 找不到哪里修改 -->
      <div class=card-body>
        <el-carousel height="210px" arrow="never" indicator-position="outside">
            <el-carousel-item v-for="(group, index) in groupedTasks" :key="index">
            <el-row :gutter="20">
                <el-col :span="24" v-for="task in group" :key="task.id">
                
                    <el-row justify="center" class="text item" style="height: 40px;">
                    <el-col :span="3" style="line-height: 40px; text-align: center;">
                        <i :class="task.icon" style="font-size: 20px;"></i>
                    </el-col>
                    <el-col :span="16">
                        <div style=" font-size: 16px; font-weight: 600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                        {{ task.title }}
                        </div>
                        <div style=" font-size: 13px; color: rgb(114, 119, 123); margin-top: 5px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                        {{ task.description }}
                        </div>
                    </el-col>
                    <el-col :span="5" style="margin-top: 10px;">
                        <el-button type="text" style="float: right; padding: 3px 0px;" @click="handleClick(task.action)">
                        {{ task.buttonText }}
                        </el-button>
                    </el-col>
                    </el-row>
                    <ElDivider />
                
                </el-col>
            </el-row>
            </el-carousel-item>
        </el-carousel>
      </div>
        
      
    </el-card>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  import { ElCard, ElCarousel, ElCarouselItem, ElRow, ElCol, ElButton, ElDivider } from 'element-plus';
  
  const tasks = ref([
    {
      id: 1,
      icon: 'el-icon-video-play',
      title: '【每周】发布2篇文章',
      description: '审核通过，奖励6积分（进度：0/2）',
      buttonText: '去发布',
      action: 'publishArticle'
    },
    {
      id: 2,
      icon: 'el-icon-pie-chart',
      title: '【每周】完成抽奖3次',
      description: '奖励3积分（进度：1/3）',
      buttonText: '去抽奖',
      action: 'drawLottery'
    },
    {
      id: 3,
      icon: 'el-icon-s-promotion',
      title: '【每月】邀请3个好友',
      description: '邀请成功，奖励10积分（进度：0/3）',
      buttonText: '去邀请',
      action: 'inviteFriends'
    },
    {
      id: 4,
      icon: 'el-icon-s-comment',
      title: '【每日】评论5条',
      description: '评论成功，奖励2积分（进度：0/5）',
      buttonText: '去评论',
      action: 'comment'
    },
    {
      id: 5,
      icon: 'el-icon-s-data',
      title: '【每周】分享3次',
      description: '分享成功，奖励3积分（进度：0/3）',
      buttonText: '去分享',
      action: 'share'
    },
    {
      id: 6,
      icon: 'el-icon-s-order',
      title: '【每月】购买1次',
      description: '购买成功，奖励5积分（进度：0/1）',
      buttonText: '去购买',
      action: 'purchase'
    },
    {
      id: 6,
      icon: 'el-icon-s-order',
      title: '【每月】购买1次',
      description: '购买成功，奖励5积分（进度：0/1）',
      buttonText: '去购买',
      action: 'purchase'
    },
  ]);
  
  const groupedTasks = computed(() => {
    const groups = [];
    for (let i = 0; i < tasks.value.length; i += 3) {
      groups.push(tasks.value.slice(i, i + 3));
    }
    return groups;
  });
  
  const handleClick = (action) => {
    console.log(`执行动作: ${action}`);
    // 根据action执行相应的逻辑
  };
  </script>
  
  <style scoped>
  
  .cardStyle {
    
    width: 100%;
    max-width: 800px;
    margin-top: 10px;
    padding-bottom: 0px;
    
  }
  .text {
    font-size: 14px;
  }
  .item {
    margin-bottom: 18px;
  }
  .el-divider {
    margin: 5px 0; /* 减小分割线的间距 */
  }
  .card-body{
    padding:10px
  }
  
  </style>