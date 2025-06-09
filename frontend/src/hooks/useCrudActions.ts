import { reactive, ref, unref } from "vue";
import request from "@/utils/request";
import { filterDateTimeParams } from "@/utils/paramfilter.ts";
import { ElMessage } from "element-plus";

export default (urls: any, queryParams?: any) => {
  const tableData = reactive<Array<any>>([]);
  const currentData = ref<any>();
  // let tableLoading = reactive<Boolean>(false);
  const pagination = ref({
    size: "small",
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    pageSizeOptions: [10, 20, 50],
    onChange: (current: number) => {
      pagination.value.current = current;
      handleList();
    },
    onShowSizeChange: (pageSize: number) => {
      pagination.value.pageSize = pageSize;
      handleList();
    }
  });
  const reCalCurrentPage = (delCnt: number) => {
    // 如果是最后一页
    if (delCnt > 0 && pagination.value.current === Math.ceil(pagination.value.total / pagination.value.pageSize)) {
      if (pagination.value.total % pagination.value.pageSize <= delCnt) {
        pagination.value.current -= 1;
      }
    }
  };
  const handleList = () => {
    let finalQueryParams = filterDateTimeParams(unref(queryParams));
    // tableLoading = true;
    request
      .get(urls.list, {
        ...finalQueryParams,
        pageNo: pagination.value.current,
        pageSize: pagination.value.pageSize
      })
      .then((data: any) => {
        tableData.splice(0, tableData.length, ...data.records);
        pagination.value.total = data.total;
      })
      .catch((err) => {
        console.log("error", err);
      })
      .finally(() => {
        // tableLoading = false;
      });
  };
  const handleDetail = (id: any) => {
    request
      .get(urls.detail, { id: id })
      .then((data: any) => {
        currentData.value = data;
      })
      .catch((err) => {
        console.log("error", err);
      });
  };
  const handleAdd = (record: any) => {
    request
      .post(urls.add, record)
      .then((data: any) => {
        console.log("data", data);
        ElMessage.success("添加成功");
        handleList();
      })
      .catch((err) => {
        console.log("error", err);
        ElMessage.error(err || "添加失败");
        handleList();
      });
  };
  const handleEdit = (record: any) => {
    request
      .post(urls.edit, record)
      .then((data: any) => {
        console.log("data", data);
        ElMessage.success("更新成功");
        handleList();
      })
      .catch((err) => {
        console.log("error", err);
        ElMessage.error(err || "更新失败");
        handleList();
      });
  };
  const handleDelete = (id: any) => {
    reCalCurrentPage(1);
    request
      .get(urls.delete, { id: id })
      .then((data: any) => {
        console.log("data", data);
        ElMessage.success("删除成功");
        handleList();
      })
      .catch((err) => {
        console.log("error", err);
        ElMessage.error(err || "删除失败");
        handleList();
      });
  };
  const handleBatchDelete = (ids: string) => {
    reCalCurrentPage(ids.split(",").length);
    request
      .get(urls.batchDelete, { ids: ids })
      .then((data: any) => {
        console.log("data", data);
        ElMessage.success("批量删除成功");
        handleList();
      })
      .catch((err) => {
        console.log("error", err);
        ElMessage.error(err || "批量删除失败");
        handleList();
      });
  };
  return {
    // tableLoading,
    tableData,
    currentData,
    pagination,
    handleList,
    handleDetail,
    handleAdd,
    handleEdit,
    handleDelete,
    handleBatchDelete
  };
};