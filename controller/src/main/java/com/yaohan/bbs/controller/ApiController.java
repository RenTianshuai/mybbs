package com.yaohan.bbs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Value("${upload.images-path}")
    private String imagesPath;

    @RequestMapping("upload")
    public Map upload(MultipartFile file){
        Map result = new HashMap();
        if (file.isEmpty()) {
            result.put("status", -1);
            result.put("msg", "上传文件为空");
            return result;
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 新文件名:ID + 后缀
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(imagesPath + "posts"+ File.separator + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            result.put("status", -1);
            result.put("msg", "文件上传出错");
            return result;
        }

        String img = "/upload/images/posts/" + fileName;

        result.put("status", 0);
        result.put("url", img);

        return result;
    }
}
