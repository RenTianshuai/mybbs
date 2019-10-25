package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.*;
import com.yaohan.bbs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/posts")
public class PostsController extends BaseController {

    private final String messageSubmitTemp = "<a href=\"/user/jump?username=%s\" target=\"_blank\"><cite>%s</cite></a>提交了需要您的审核的帖子<a target=\"_blank\" href=\"/approve/approvePage?id=%s\"><cite>%s</cite></a>";


    @Autowired
    PostsServcie postsServcie;
    @Autowired
    FlowNoService flowNoService;
    @Autowired
    PostsLabelService postsLabelService;
    @Autowired
    MessageService messageService;
    @Autowired
    PostsApproveLogService postsApproveLogService;

    @RequestMapping("/add")
    @ResponseBody
    public Map add(String label, String title, String content, String vercode, User user){
        Map result = new HashMap();
        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String)session.getAttribute("validateCode");
        if (vercode == null || !vercode.toUpperCase().equals(code)){
            result.put("msg", "验证码错误,请重试");
            return result;
        }

        Posts posts = new Posts();
        posts.setId(flowNoService.generateFlowNo());
        posts.setLabelId(label);
        posts.setTitle(title);
        posts.setContent(content);
        posts.setUserId(user.getId());
        posts.setPublishTime(new Date());
        posts.setExperience(0);
        posts.setReadCount(0);
        posts.setDelFlag("0");

        PostsLabel postsLabel = postsLabelService.get(label);
        if (user.getRoleId().equals("student") && "1".equals(postsLabel.getIsApprove())){
            //如果是学生发布需要审核的贴
            posts.setStatus((byte)2);
        }else {
            posts.setStatus((byte)4);
        }

        postsServcie.add(posts);

        //添加一条消息
        addMessage(user, posts);

        result.put("status", 0);
        result.put("action", "/");

        return result;
    }

    @RequestMapping("edit")
    public String editPage(String id, Model model){

        Posts posts = postsServcie.get(id);
        model.addAttribute("posts", posts);

        PostsApproveLog approveLog = postsApproveLogService.getNewByPostsId(id);
        model.addAttribute("log", approveLog);

        return "jie/edit";
    }

    @RequestMapping("/upd")
    @ResponseBody
    public Map upd(String label, String jid, String title, String content, String vercode, User user){
        Map result = new HashMap();
        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String)session.getAttribute("validateCode");
        if (vercode == null || !vercode.toUpperCase().equals(code)){
            result.put("msg", "验证码错误,请重试");
            return result;
        }

        Posts posts = postsServcie.get(jid);
        posts.setLabelId(label);
        posts.setTitle(title);
        posts.setContent(content);

        PostsLabel postsLabel = postsLabelService.get(label);
        if (user.getRoleId().equals("student") && "1".equals(postsLabel.getIsApprove())){
            //如果是学生发布需要审核的贴
            posts.setStatus((byte)2);
        }else {
            posts.setStatus((byte)4);
        }

        postsServcie.update(posts);

        //添加一条消息
        addMessage(user, posts);

        result.put("status", 0);
        result.put("action", "/");

        return result;
    }

    private void addMessage(User user, Posts posts) {
        try{
            if (posts.getStatus() == 2){
                Message message = new Message();
                message.setCreateTime(new Date());
                //格式化消息：昵称，实名，帖子ID,回复ID,标题
                String msg = String.format(messageSubmitTemp, user.getUsername(), user.getRealname(), posts.getId(), posts.getTitle());
                message.setMessage(msg);
                //接收人老师
                Map params = new HashMap(3);
                params.put("roleId", "teacher");
                params.put("school", user.getSchool());
                params.put("className", user.getClassName());
                List<User> teachers = userService.getBy(params);
                if (teachers!=null){
                    for (User t:teachers){
                        message.setId(flowNoService.generateFlowNo());
                        message.setUserId(t.getId());
                        messageService.add(message);
                    }
                }
            }
        }catch (Exception e){
            log.error("添加消息出错", e);
        }
    }
}
