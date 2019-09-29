package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController extends BaseController{

    @GetMapping("/")
    public String index(@RequestParam(value = "label", required = false) String label, Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if (user != null){
            model.addAttribute("user", user);
        }
        model.addAttribute("labels", getPostsLabel());
        return "index";
    }

}
