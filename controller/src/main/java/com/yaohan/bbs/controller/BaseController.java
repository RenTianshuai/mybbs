package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.PostsLabelService;
import com.yaohan.bbs.service.RoleService;
import com.yaohan.bbs.service.UserService;
import com.yaohan.bbs.vo.PostsVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public class BaseController {

    @Autowired
    PostsLabelService postsLabelService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

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

    /**
     * 获取PostsVO
     * @param posts
     * @return
     */
     PostsVO getPostsVO(Posts posts) {
        PostsVO vo = new PostsVO();
        vo.setLabel(postsLabelService.get(posts.getLabelId()));
        vo.setPosts(posts);
        vo.setUser(userService.get(posts.getUserId()));
        vo.setRole(roleService.get(vo.getUser().getRoleId()));
        return vo;
    }
}
