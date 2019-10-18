package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.PostsApproveLogService;
import com.yaohan.bbs.service.PostsServcie;
import com.yaohan.bbs.vo.PostsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/approve")
public class ApproveController extends BaseController {

    @Autowired
    PostsServcie postsServcie;

    @Autowired
    PostsApproveLogService postsApproveLogService;

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("nav", "approve");

        return "sys/approveList";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map list(Integer page, Integer limit){
        Map result = new HashMap();
        if (page == null || page == 0){
            page = 1;
        }
        if (limit == null || limit == 0){
            limit = 10;
        }
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("code", -1);
            result.put("msg", "亲，你还没有登录哟！");
            return result;
        }
        //设置班内提交的日记
        Map params = new HashMap();
        params.put("className", user.getClassName());
        params.put("status", (byte)2);
        Page<Posts> postsPage = postsServcie.findostsByPage(page, limit, params);
        List<Posts> postsList = postsPage.getResult();
        if (postsList!=null && postsList.size()>0){
            for (Posts posts:postsList){
                posts.setLabelName(postsLabelService.get(posts.getLabelId()).getName());
                User u = userService.get(posts.getUserId());
                posts.setUserName(u.getUsername());
                posts.setRealName(u.getRealname());
            }
        }

        result.put("code", 0);
        result.put("msg", "查询结果");
        result.put("count", postsPage.getTotal());
        result.put("data", postsList);
        return result;
    }

    @RequestMapping("/approvePage")
    public String aprovePage(String id, Model model){
        PostsVO vo = getPostsVO(postsServcie.get(id));
        model.addAttribute("vo", vo);
        return "sys/approve";
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Map approve(String jid, String score, String content, Byte status){
        Map result = new HashMap();

        result.put("status", 0);
        return result;
    }
}
