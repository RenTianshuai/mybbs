package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.PostsCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/collection")
public class PostsCollectionController extends BaseController{

    @Autowired
    PostsCollectionService postsCollectionService;

    @RequestMapping("/add")
    @ResponseBody
    public Map add(String cid){
        Map result = new HashMap();
        User user = checkUser();
        postsCollectionService.addByUserIdAndPostsId(user.getId(), cid);
        result.put("status", 0);
        return result;
    }

    @RequestMapping("/remove")
    @ResponseBody
    public Map remove(String cid){
        Map result = new HashMap();
        User user = checkUser();
        postsCollectionService.removeByUserIdAndPostsId(user.getId(), cid);
        result.put("status", 0);
        return result;
    }
}
