<template>
  <div class="var-field-input">
    <component
      :is="componentName"
      :name="recordMeta.name"
      v-model="computedModelValue"
      :placeholder="'请输入' + recordMeta.title"
      :disabled="disabled"

      :filterable="true"
      :clearable="true"

      :httpRequest="handleUpload"
      :beforeUpload="handlePreUpload"
      :limit="1"
      :list-type="props.recordMeta.dataType === 'Picture' ? 'picture' : 'text'"

      :type="recordMeta.dataType === 'BigString' ? 'textarea' : 'datetime'"
      :format="props.recordMeta.dataType === 'Date' ? 'YYYY-MM-DD HH:mm:ss' : 'HH:mm'"
      :valueFormat="props.recordMeta.dataType === 'Date' ? 'YYYY-MM-DD HH:mm:ss' : 'HH:mm'"

      controls-position="right"
      :style="{ width: '100%' }"
    >
      <el-option v-for="option in recordMeta.options" :key="option.value" :label="option.label" :value="option.value"></el-option>
      <el-button v-if="props.recordMeta.dataType === 'Picture' || props.recordMeta.dataType === 'File'">
        <el-icon><Upload /></el-icon>
        选取
      </el-button>
      <template v-if="hasInputAppend" #append>
        <slot name="append"></slot>
      </template>
    </component>
  </div>
</template>

<script setup lang="ts">
import {ElMessage} from "element-plus";
import { RecordMateType } from "@/types";
import { computed } from "vue";
import request from "@/utils/request";

const props = defineProps({
  modelValue: {
    type: [String, Number]
  },
  recordMeta: {
    type: Object as () => RecordMateType,
    default: {}
  },
  hasInputAppend: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  }
});
const componentName = computed(() => {
  if (props.recordMeta.selectable) {
    return "ElSelect"
  }
  if (props.recordMeta.dataType === "Integer" || props.recordMeta.dataType === "Long" || props.recordMeta.dataType === "Double" || props.recordMeta.dataType === "BigDecimal") {
    return "ElInputNumber";
  } else if (props.recordMeta.dataType === "String") {
    return "ElInput";
  } else if (props.recordMeta.dataType === "RichString") {
    return "VueEditor";
  } else if (props.recordMeta.dataType === "Enum") {
    return "ElSelect";
  } else if (props.recordMeta.dataType === "Date") {
    return "ElDatePicker";
  } else if (props.recordMeta.dataType === "Time") {
    return "ElTimePicker";
  } else if (props.recordMeta.dataType === "Picture" || props.recordMeta.dataType === "File") {
    return "ElUpload";
  }
  return "ElInput";
});
const emits = defineEmits(["update:modelValue"]);
const computedModelValue = computed({
  get() {
    return props.modelValue;
  },
  set(val) {
    emits("update:modelValue", val);
  }
});
const handleUpload = ({file, onSuccess, onError}) => {
  request.upload("/manage/common/upload", {
    file: file,
    biz: props.recordMeta.dataType === 'Picture' ? "pic" : "file"
  }).then((res) => {
    onSuccess(res);
    emits("update:modelValue", res);
  }).catch(err => {
    onError(err);
  });
}
const handlePreUpload = (file) => {
  if (props.recordMeta.dataType !== 'Picture') {
    return true;
  }
  const isImg = ["image/png", "image/jpeg", "image/gif"].indexOf(file.type) !== -1;
  if (!isImg) {
    ElMessage.error("只能上传图片");
  }
  // const isLt2M = file.size / 1024 / 1024 < 2;
  // if (!isLt2M) {
  //   this.$message.error('Image must smaller than 2MB!');
  // }
  return isImg;
};
</script>

<style scoped>
.var-field-input {
  width: 100%;
}
</style>
