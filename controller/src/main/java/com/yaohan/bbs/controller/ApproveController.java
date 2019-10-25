package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.Message;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsApproveLog;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.MessageService;
import com.yaohan.bbs.service.PostsApproveLogService;
import com.yaohan.bbs.service.PostsServcie;
import com.yaohan.bbs.vo.PostsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/approve")
public class ApproveController extends BaseController {

    private final String messageApproveTemp = "<a href=\"/user/jump?username=%s\" target=\"_blank\"><cite>%s</cite></a>%s了您的帖子<a target=\"_blank\" href=\"/jie/detail?id=%s\"><cite>%s</cite></a>";


    @Autowired
    PostsServcie postsServcie;

    @Autowired
    PostsApproveLogService postsApproveLogService;

    @Autowired
    FlowNoService flowNoService;

    @Autowired
    MessageService messageService;

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
        Page<Posts> postsPage = postsServcie.findPostsByPage(page, limit, params);
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

        //

        return "sys/approve";
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Map approve(String jid, Byte score, String content, Byte status){
        Map result = new HashMap();
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("code", -1);
            result.put("msg", "亲，你还没有登录哟！");
            return result;
        }
        Posts posts = postsServcie.getWithoutBLOBS(jid);
        posts.setScore(score);
        posts.setStatus(status);
        posts.setApprover(user.getId());

        postsServcie.update(posts);

        //新增log
        PostsApproveLog approveLog = new PostsApproveLog();
        approveLog.setId(flowNoService.generateFlowNo());
        approveLog.setCreateTime(new Date());
        approveLog.setPostsId(jid);
        approveLog.setUserId(user.getId());
        approveLog.setContent(content);
        postsApproveLogService.add(approveLog);

        //添加一条消息
        try{
            Message message = new Message();
            message.setId(flowNoService.generateFlowNo());
            //接收人作者
            message.setUserId(posts.getUserId());
            message.setCreateTime(new Date());
            //格式化消息：昵称，昵称，状态，帖子ID,回复ID,标题
            String msg = String.format(messageApproveTemp, user.getUsername(), user.getUsername(), (status==4?"通过":"打回"), jid, posts.getTitle());
            message.setMessage(msg);
            messageService.add(message);

        }catch (Exception e){
            log.error("添加消息出错", e);
        }

        result.put("status", 0);
        result.put("msg","审核成功");
        result.put("action", "/approve/index");
        return result;
    }
}
