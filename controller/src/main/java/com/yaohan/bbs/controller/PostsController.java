package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.PostsLabelService;
import com.yaohan.bbs.service.PostsServcie;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/posts")
public class PostsController extends BaseController {

    @Autowired
    PostsServcie postsServcie;
    @Autowired
    FlowNoService flowNoService;
    @Autowired
    PostsLabelService postsLabelService;

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

        result.put("status", 0);
        result.put("action", "/");

        return result;
    }

}
