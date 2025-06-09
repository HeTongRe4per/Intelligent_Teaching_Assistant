import axios from "axios";

const instance = axios.create({
  timeout: 8000,
  timeoutErrorMessage: "请求超时"
});

instance.interceptors.request.use(
  (config) => {
    config.baseURL = "/api";
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = token;
    } else {
      if (config.url.endsWith("/add") || config.url.endsWith("/edit")) {
        return Promise.reject("请先登录");
      }
    }
    return config;
  },
  (error) => {
    console.log("请求失败");
    return Promise.reject(error);
  }
);

instance.interceptors.response.use(
  (response) => {
    if (response.config.responseType && response.config.responseType === "blob") {
      // 下载请求响应，直接返回data
      return response.data;
    } else {
      const {data} = response;
      if (data.code === 200) {
        return data.data;
      } else {
        if (data.code === 50001) {
          localStorage.removeItem("token");
          window.location.href = "/login";
        } else {
          return Promise.reject(data.message);
        }
      }
    }
  },
  (error) => {
    console.log("响应失败");
    if (error.response && error.response.status === 403) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);
export default {
  get(url: string, params?: any) {
    if (params) {
      return instance.get(url, {params: JSON.parse(JSON.stringify(params))});
    } else {
      return instance.get(url);
    }
  },
  post(url: string, data: any) {
    return instance.post(url, data);
  },
  download(url: string, params?: any) {
    if (params) {
      return instance.get(url, {
        params: JSON.parse(JSON.stringify(params)),
        responseType: "blob"
      });
    } else {
      return instance.get(url, {
        responseType: "blob"
      });
    }
  },
  upload(url: string, data: { file: File, biz: string }) {
    let formData = new FormData();
    formData.append('file', data.file);
    formData.append('biz', data.biz);
    return instance.post(url, formData, {
      headers: {"Content-Type": "multipart/form-data"}
    });
  }
};
