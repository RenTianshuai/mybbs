package com.yaohan.bbs.controller;

import com.yaohan.bbs.common.Constant;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.mail.dto.EmailDTO;
import com.yaohan.bbs.mail.service.MailSenderService;
import com.yaohan.bbs.service.PostsLabelService;
import com.yaohan.bbs.service.RoleService;
import com.yaohan.bbs.service.UserService;
import com.yaohan.bbs.vo.PostsVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public class BaseController {

    @Autowired
    PostsLabelService postsLabelService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    MailSenderService mailSenderService;

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

    void sendActivateMail(HttpServletRequest request, User user) {
        //设置邮箱激活码
        if (StringUtils.isEmpty(user.getAuthInfo())){
            user.setAuthInfo(UUID.randomUUID().toString());
            userService.update(user);
        }
        //发送激活邮件
        String path = request.getRequestURL().toString();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("来自青少年传习论坛的激活邮件");
        emailDTO.setReceiver(user.getEmail());
        emailDTO.setContent(String.format(Constant.ACTIVATE_EMAIL, path.substring(0, path.indexOf(request.getServletPath())), user.getAuthInfo()));
        mailSenderService.sendMail(emailDTO, true);
    }
}
