package com.yaohan.bbs.controller;

import com.yaohan.bbs.service.MessageService;
import com.yaohan.bbs.service.PostsReplyService;
import com.yaohan.bbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Value("${upload.images-path}")
    private String imagesPath;

    @Autowired
    PostsReplyService postsReplyService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

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

    @RequestMapping("/top/reply")
    public Map topReply(Integer limit){
        Map result = new HashMap();
        List<Map> data = postsReplyService.getWeeklyTopReplys(limit);
        result.put("status", 0);
        result.put("data", data);
        return result;
    }

    @RequestMapping("/hot/reply")
    public Map hotReply(Integer limit){
        Map result = new HashMap();
        List<Map> data = postsReplyService.getWeeklyHotReplys(limit);
        result.put("status", 0);
        result.put("data", data);
        return result;
    }

    @RequestMapping("/message/nums")
    public Map messageNums(){
        Map result = new HashMap();
        int count = messageService.countByUserId(checkUser().getId());
        result.put("status", 0);
        result.put("count", count);
        return result;
    }

    @RequestMapping("/message/remove")
    public Map messageremove(String id, Boolean all){
        Map result = new HashMap();

        if (all != null && all){
            messageService.deleteByUserId(checkUser().getId());
        }else {
            messageService.delete(id);
        }

        result.put("status", 0);
        return result;
    }
}
