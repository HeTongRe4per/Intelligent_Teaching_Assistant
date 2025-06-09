package com.hetongre4per.ita.modules.common.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.hetongre4per.ita.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 上传预览控制器
 */
@Slf4j
@Controller
@RequestMapping("/manage/common")
public class CommonController {
    
    @Value(value = "${uploadPath}")
    private String uploadPath;
    
    /**
     * 统一的文件上传方法
     */
    @PostMapping(value = "/upload")
    @ResponseBody
    public Result<String> upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<String> result = new Result<>();
        String savePath;
        String bizPath = request.getParameter("biz");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件对象
        MultipartFile file = multipartRequest.getFile("file");
        if (StrUtil.isEmpty(bizPath)) {
            bizPath = "upload";
        }
        savePath = this.uploadLocal(file, bizPath);
        if (StrUtil.isNotEmpty(savePath)) {
            result.setCode(200);
            result.setData(savePath);
            result.setSuccess(true);
        } else {
            result.setMessage("上传失败");
            result.setSuccess(false);
        }
        return result;
    }
    
    /**
     * 预览图片&下载文件
     */
    @GetMapping(value = "/preview")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        String shortFileName = request.getParameter("path");
        if (StrUtil.isEmpty(shortFileName)) {
            return;
        }
        shortFileName = shortFileName.replace("..", "").replace("../", "");
        String filePath = uploadPath + File.separator + shortFileName;
        File file = new File(filePath);
        if (!file.exists()) {
            response.setStatus(404);
            log.error("文件[{}]不存在..", shortFileName);
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 设置强制下载不打开
            response.setContentType(getContentType(shortFileName));
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getName(), "UTF-8"));
            inputStream = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败{}", e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * 获取文件类型
     * @param fileName
     * @return
     */
    private String getContentType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (fileName.endsWith(".mkv")) {
            return "video/x-x-matroska";
        } else if (fileName.endsWith(".mov")) {
            return "video/x-x-quicktime";
        } else {
            return "application/octet-stream";
        }
    }
    
    /**
     * 本地文件上传
     */
    private String uploadLocal(MultipartFile mf, String bizPath) {
        try {
            String ctxPath = uploadPath;
            File file = new File(ctxPath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                // 创建文件根目录
                file.mkdirs();
            }
            // 获取文件名
            String orgName = mf.getOriginalFilename();
            String fileName = FileUtil.getPrefix(orgName) + "_" + System.currentTimeMillis() + "." + FileUtil.getSuffix(orgName);
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbPath;
            if (StrUtil.isNotEmpty(bizPath)) {
                dbPath = bizPath + "/" + fileName;
            } else {
                dbPath = fileName;
            }
            return dbPath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
    
}
