package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.PostsLabelService;
import com.yaohan.bbs.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public class BaseController {

    @Autowired
    PostsLabelService postsLabelService;

    @ModelAttribute("labels")
    List<PostsLabel> getPostsLabel(){
        return postsLabelService.getUsablePostsLabel();
    }

    @ModelAttribute("user")
    public User checkUser(){
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return null;
        }
        Object object = subject.getSession().getAttribute("user");
        if (object == null){
            return null;
        }
        User user = (User) object;
        if (user.getId() == null){
            return null;
        }
        return user;
    }

    void refreshUser(User user){
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return ;
        }
        subject.getSession().setAttribute("user", user);
    }

}
