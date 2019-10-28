package com.yaohan.bbs.controller;

import com.alibaba.excel.EasyExcel;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.dao.entity.UserLikeLog;
import com.yaohan.bbs.excel.UserModel;
import com.yaohan.bbs.excel.UserModelListener;
import com.yaohan.bbs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
    @Autowired
    PostsServcie postsServcie;
    @Autowired
    UserLikeLogService userLikeLogService;

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

    @RequestMapping("/jie-set")
    public Map jieSet(String id, int rank, String field){
        Map result = new HashMap();
        try {
            Posts p = postsServcie.getWithoutBLOBS(id);

            if ("essence".equals(field)){
                p.setEssence(rank==1);
            }
            if ("top".equals(field)){
                p.setTop(rank==1);
            }
            postsServcie.update(p);
        }catch (Exception e){
            log.error("发生错误", e);
            result.put("status", -1);
            result.put("msg", "发生了一个错误");
            return result;
        }

        result.put("status", 0);
        return result;
    }

    @RequestMapping("/activate")
    public Map activate(String email, HttpServletRequest request){
        Map result = new HashMap();
        try {
            User user = userService.getByEmail(email);
            if (!"Y".equals(user.getEmailActivate())){
                sendActivateMail(request, user);
            }
            result.put("status", 0);
        }catch (Exception e){
            result.put("msg", "发送激活邮件产生错误");
        }
        return result;
    }

    @RequestMapping("/changeEmail")
    public Map changeEmail(String o, String n,  HttpServletRequest request){
        Map result = new HashMap();
        try {
            User user = userService.getByEmail(o);
            user.setEmail(n);
            user.setEmailActivate("N");
            userService.update(user);
            //发送激活邮件
            sendActivateMail(request, user);
            result.put("status", 0);
        }catch (Exception e){
            result.put("msg", "发送激活邮件产生错误,请重新登录并再次发送激活邮件");
        }
        return result;
    }

    @RequestMapping("import/schoolUser")
    public Map importSchoolUser(MultipartFile file){
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
        if (!".xls.xlsx".contains(suffixName)){
            result.put("status", -1);
            result.put("msg", "仅支持excel文件");
            return result;
        }
        // 导入
        log.info("导入学校用户");
        try {
            EasyExcel.read(file.getInputStream(), UserModel.class, new UserModelListener(userService)).sheet().doRead();
        } catch (Exception e) {
            result.put("status", -1);
            result.put("msg", "导入失败，请确保Excel格式正确：" + e.getMessage());
            return result;
        }

        result.put("status", 0);
        result.put("msg", "导入成功");

        return result;
    }

    @RequestMapping("/jie-zan")
    public Map jieZan(Boolean ok, String id){
        Map result = new HashMap();
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("status", -1);
            result.put("msg", "亲，您还没有登录哟！");
            return result;
        }
        UserLikeLog userLikeLog = userLikeLogService.get(user.getId(), id);
        if (ok){
            userLikeLogService.cancel(userLikeLog);
        }else {
            userLikeLogService.zan(userLikeLog);
        }

        result.put("status", 0);
        return result;
    }

    @RequestMapping("/jie-delete")
    public Map jieDelete(String id){
        Map result = new HashMap();
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("status", -1);
            result.put("msg", "亲，您还没有登录哟！");
            return result;
        }

        Posts posts = postsServcie.get(id);
        posts.setDelFlag("1");
        postsServcie.update(posts);

        result.put("status", 0);
        return result;
    }
}
