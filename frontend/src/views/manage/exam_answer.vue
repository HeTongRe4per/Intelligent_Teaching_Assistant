<template>
  <div style="max-height: 80vh">
  <div class="item">
    <h1 class="detail-info">{{ currentData?.name }}</h1>
    <el-form ref="questionFormRef" :model="questionForm" :rules="questionFormRules" label-position="top">
      <el-form-item v-for="(item, idx) in questionTableData" :prop="'item_' + item.id" :name="'item_' + item.id">
        <template #label>
          <div style="display: inline-block;">
            <span v-if="item.type === 1" :style="{'color': currentData?.state===0?'':(questionForm['item_' + item.id].toUpperCase() === item.content.answer?'green':'red')}">{{(idx+1) + '. ' + item.content.title}}</span>
            <span v-if="item.type === 2">{{(idx+1) + '. ' + item.content.title + '（简答）'}}</span>
            <span style="color: #3f87ff">&nbsp;{{item.score + '分'}}</span>
            <span v-if="isTeacherGlobal">
              <span>&nbsp;得分：</span>
              <el-input-number controls-position="right" style="width: 100px;" :disabled="item.type===1 || currentData?.state === 2" :min="0" :max="item.score" v-model="scoreValues[idx]" />
            </span>
            <span v-if="currentData?.state===2">
              <span>&nbsp;得分：{{scoreValues[idx]}}</span>
            </span>
          </div>
        </template>
        <el-radio-group v-if="item.type === 1" :disabled="currentData?.state !== 0" style="width: 100%;" v-model="questionForm['item_' + item.id]">
          <el-radio value="A" style="width: 50%; margin: 0;">A {{item.content.itemA}}</el-radio>
          <el-radio value="B" style="width: 50%; margin: 0;">B {{item.content.itemB}}</el-radio>
          <el-radio value="C" style="width: 50%; margin: 0;">C {{item.content.itemC}}</el-radio>
          <el-radio value="D" style="width: 50%; margin: 0;">D {{item.content.itemD}}</el-radio>
        </el-radio-group>
        <el-input v-if="item.type === 2" :disabled="currentData?.state !== 0" style="width: 100%;" v-model="questionForm['item_' + item.id]" type="textarea" :rows="5" />
      </el-form-item>
    </el-form>
    <el-button v-if="currentData?.state === 0" type="primary" @click="handleSubmit" style="width: 50%; height: 35px;">提交</el-button>
    <el-button v-if="isTeacherGlobal && currentData?.state === 1" type="primary" @click="handleCheck" style="width: 50%; height: 35px;">提交批改</el-button>
  </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, ref, watch} from "vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import router from "@/router";
import request from "@/utils/request.ts";
import {ElMessage} from "element-plus";
import {isTeacherGlobal} from "@/utils/user.ts";

const scoreValues: any = ref([]);
const questionFormRef = ref();
const questionForm = ref<any>({});
const questionFormRules = ref<any>({});
const questionTableData = ref([]);
const urls = {
  detail: "/manage/studentExam/queryById"
};
let { currentData, handleDetail } = useCrudActions(urls);
watch(
    () => currentData.value,
    () => {
      if (currentData.value) {
        questionForm.value = currentData.value.answers || {};
        request.get("/manage/examQuestion/list", {
          examId: currentData.value.examId,
          pageSize: 1000,
          order: "sequence"
        }).then((data: any) => {
          questionTableData.value = data.records;
          if (questionTableData.value.length > 0) {
            questionTableData.value.forEach((item: any, idx) => {
              questionFormRules.value['item_' + item.id] = [
                {
                  required: true,
                  message: "请作答题目" + (idx+1)
                }
              ];
              if (currentData.value.state === 1) {
                if (item.type === 1) {
                  if (questionForm.value['item_' + item.id].toUpperCase() === item.content.answer) {
                    scoreValues.value[idx] = item.score;
                  } else {
                    scoreValues.value[idx] = 0;
                  }
                } else {
                  scoreValues.value[idx] = null;
                }
              } else if (currentData.value.state === 2) {
                scoreValues.value[idx] = currentData.value.answers['item_'+item.id+"_score"];
              }
            });
          }
        });
      }
    }
);
onMounted(() => {
  const id = router.currentRoute.value.params.id;
  if (id === "") {
    return;
  }
  handleDetail(id);
});
const handleSubmit = () => {
  questionFormRef.value.validate().then(() => {
    let score = 0;
    let answer: any = {};
    Object.keys(questionForm.value).forEach((questionProp: any) => {
      const questionId = parseInt(questionProp.substring(questionProp.indexOf("_")+1));
      const question: any = questionTableData.value.find((item: any) => item.id === questionId);
      if (questionForm.value[questionProp].toUpperCase() === question.content.answer) {
        score += question.score;
      }
      answer[questionProp] = questionForm.value[questionProp].toUpperCase();
    });
    let postData: any = {
      id: currentData.value.id,
      answers: answer,
      switchCount: localSwitchCount/2
    };
    if (questionTableData.value.every((item: any) => item.type === 1)) {
      postData.score = score;
      postData.state = 2;
    } else {
      postData.state = 1;
    }
    request.post("/manage/studentExam/edit", postData).then(() => {
      ElMessage.success("提交成功");
      localSwitchCount = 0;
      localStorage.removeItem('switchCount');
      router.push({ path: '/manage/my_exam' });
    });
  }).catch(() => {});
};
const handleCheck = () => {
  let score = 0;
  let answers = Object.assign({}, currentData.value.answers);
  scoreValues.value.forEach((item: any, index: number) => {
    if (item === null) {
      ElMessage.error("请输入题目" + (index+1) + "得分");
      return;
    }
    answers['item_' + questionTableData.value[index].id + '_score'] = item;
    score += item;
  });
  request.post("/manage/studentExam/edit", {
    id: currentData.value.id,
    score: score,
    answers: answers,
    state: 2
  }).then(() => {
    ElMessage.success("提交成功");
    router.push({ path: '/manage/my_exam' });
  });
};

let localSwitchCount = 0;
let switchScreenHandler: () => void;

onMounted(() => {
  // 初始化本地切屏计数
  const stored = localStorage.getItem('switchCount');
  localSwitchCount = stored ? parseInt(stored) : 0;

  switchScreenHandler = () => {
    localSwitchCount += 1;
    localStorage.setItem('switchCount', localSwitchCount.toString());
  };
  window.addEventListener("visibilitychange", switchScreenHandler);
});

onUnmounted(() => {
  window.removeEventListener("visibilitychange", switchScreenHandler);
});
</script>

<style scoped>
.item {
  padding: 20px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 18px !important;
  line-height: 18px !important;
  font-weight: bold;
}
</style>
