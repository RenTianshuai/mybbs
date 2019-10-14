package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.PostsReplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PostsReplyController extends BaseController {

    @Autowired
    PostsReplyService postsReplyService;

    @RequestMapping("/jie/reply")
    @ResponseBody
    public Map reply(String jid, String content){
        Map result = new HashMap(2);
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("status", -1);
            result.put("msg", "亲，您还没有登录哟");
            return result;
        }
        postsReplyService.addReply(jid, user.getId(), content);

        result.put("status", 0);
        result.put("action", "/jie/detail?id=" + jid);

        return result;
    }
}
