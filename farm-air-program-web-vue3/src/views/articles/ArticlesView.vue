<script setup lang="ts">


  import type {Article} from "@/api/articles";
  import { getArticleById } from '@/api/articles';
  import { defineProps, ref, onMounted } from 'vue';
  import ArticleComponent from '@/components/articles/ArticleComponent.vue';

  const props = defineProps({
    id: {
      type: String,
      default: ''
    }
  });


</script>

<template>

  <ArticleComponent v-if="props.id" :id="props.id" />
  
</template>


<style lang="scss" scoped>
</style>
