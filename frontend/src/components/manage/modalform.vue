<template>
  <!-- 可复用的弹框表单组件 -->
  <el-dialog v-model="visible" :width="800" :title="title" destroy-on-close closable @close="visible = false">
    <el-form
      class="modal-form"
      ref="formRef"
      :inline="false"
      :model="currentRecord"
      :rules="recordRuleMetas"
      :label-width="200"
      :gutter="20"
      :style="{ padding: '50px 50px' }"
    >
      <el-form-item v-for="(item, index) in recordMetas"  :key="item.name + '_' + index" :label="item.title" :prop="item.name" style="width: 100%; display: flex;">

        <var-field-input v-if="item.subName && item.dataType !== 'JSONObject'" v-model="currentRecord[item.name][item.subName]" :recordMeta="item" :disabled="!editable || item.disabled" style="width: 85%" />
        <var-field-input v-else-if="item.dataType !== 'JSONObject'" v-model="currentRecord[item.name]" :recordMeta="item" :disabled="!editable || item.disabled" style="width: 85%" />
        <el-form v-else :inline="false" :gutter="20">
          <el-form-item v-for="key in (currentRecord[item.name] ? Object.keys(currentRecord[item.name]) : {})" :label="key" style="width: 100%; display: flex;">
            <var-field-input v-model="currentRecord[item.name][key]" :recordMeta="{name: key, title: key, dataType: 'String'}" :hasInputAppend="true" :disabled="!editable || item.disabled">
              <template #append>
                <el-button :icon="Close" @click="handleDelItem(item.name, key)" />
              </template>
            </var-field-input>
          </el-form-item>
          <el-button style="margin-left: 20px" type="warning" @click="handleClickAddItem(item.name)">添加项目</el-button>
        </el-form>

      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">关闭</el-button>
        <el-button type="primary" @click="handleOk">确认</el-button>
      </div>
    </template>

    <!-- 内部弹窗 -->
    <el-dialog v-model="innerVisible" :width="500" title="添加子项目" append-to-body>
      <el-form-item label="子项目名">
        <el-input v-model="newSubName" placeholder="请输入子项目名" />
      </el-form-item>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" @click="innerVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleAddItem">确认</el-button>
        </div>
      </template>
    </el-dialog>

  </el-dialog>
</template>

<script setup lang="ts">
import varFieldInput from "@/components/manage/varfieldinput.vue";
import { ref, watchEffect } from "vue";
import { RecordMateType } from "@/types";
import {Close} from "@element-plus/icons-vue";

const formRef = ref<any>();
const editable = ref<Boolean>(false);
const isEdit = ref<Boolean>(false);
const visible = ref<Boolean>(false);
const innerVisible = ref<Boolean>(false);
const parentName = ref<string>("");
const newSubName = ref<string>("");
const recordMetas = ref<Array<RecordMateType>>([]);
let recordRuleMetas: Record<string, any> = {};
const currentRecord = ref<any>({});
const props = defineProps({
  hideIdField: {
    type: Boolean,
    default: true
  },
  title: String
});
watchEffect(() => {
  if (recordMetas.value.length > 0) {
    recordMetas.value = recordMetas.value.filter((item: any) => {
      if (props.hideIdField) {
        return item.name != "id";
      }
      return true;
    });
    recordMetas.value.filter((item: any) => item.required == null || item.required).forEach((item: any) => {
      recordRuleMetas[item.name] = [
        {
          required: true,
          message: "请输入" + item.title
        }
      ];
    });
  }
});
const handleClickAddItem = (parentName1: string) => {
  innerVisible.value = true;
  parentName.value = parentName1;
};
const handleAddItem = () => {
  if (newSubName.value) {
    if (!currentRecord.value[parentName.value]) {
      currentRecord.value[parentName.value] = {};
    }
    currentRecord.value[parentName.value][newSubName.value] = "";
    newSubName.value = "";
    innerVisible.value = false;
  }
};
const handleDelItem = (parentName1: string, subName1: string) => {
  delete currentRecord.value[parentName1][subName1];
};
const emits = defineEmits(["handleAdd", "handleEdit"]);
const handleOk = () => {
  if (!editable.value) {
    visible.value = false;
    return;
  }
  formRef.value
    .validate()
    .then(() => {
      if (isEdit.value) {
        emits("handleEdit", currentRecord.value);
      } else {
        emits("handleAdd", currentRecord.value);
      }
      visible.value = false;
    })
    .catch(() => {});
};
defineExpose({
  editable,
  isEdit,
  visible,
  recordMetas,
  currentRecord
});
</script>

<style scoped></style>