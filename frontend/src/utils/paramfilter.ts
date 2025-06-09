import dayjs from "dayjs";

export const filterDateTimeParams = (params: any) => {
  let finalParams: any = {};
  Object.assign(finalParams, params);
  for (const key in finalParams) {
    if (finalParams[key] instanceof Array && finalParams[key].length > 0) {
      let isDateArray = false;
      // 过滤并处理日期时间查询参数
      // 例如：createTime -> createTimeStart, createTimeEnd
      if (dayjs(finalParams[key][0]).isValid()) {
        finalParams[key + "Start"] = finalParams[key][0];
        isDateArray = true;
      }
      if (finalParams[key].length === 2 && dayjs(finalParams[key][1]).isValid()) {
        finalParams[key + "End"] = finalParams[key][1];
        isDateArray = true;
      }
      if (isDateArray) {
        delete finalParams[key];
      }
    }
  }
  return finalParams;
};
