package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.Message;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsReply;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.MessageService;
import com.yaohan.bbs.service.PostsReplyService;
import com.yaohan.bbs.service.PostsServcie;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class PostsReplyController extends BaseController {

    private final String messageReplyTemp = "<a href=\"/user/jump?username=%s\" target=\"_blank\"><cite>%s</cite></a>评论了您的帖子<a target=\"_blank\" href=\"/jie/detail?id=%s#item-%s\"><cite>%s</cite></a>";

    private final String messageAnswerTemp = "<a href=\"/user/jump?username=%s\" target=\"_blank\"><cite>%s</cite></a>在帖子里<a target=\"_blank\" href=\"/jie/detail?id=%s#item-%s\"><cite>%s</cite></a>回复了您";


    @Autowired
    PostsReplyService postsReplyService;

    @Autowired
    PostsServcie postsServcie;

    @Autowired
    MessageService messageService;

    @Autowired
    FlowNoService flowNoService;

    @Value("${posts.reply.timeInHour}")
    private int replyTimesInHour;

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

        int times = postsReplyService.replyTimesInHour(jid, user.getId());
        if (times >= replyTimesInHour){
            result.put("status", -1);
            result.put("msg", "亲，您一个小时内已评论" + replyTimesInHour + "次，不要刷评哦！");
            return result;
        }

        PostsReply reply = postsReplyService.addReply(jid, user.getId(), content);

        //添加一条消息
        try{
            Posts p = postsServcie.getWithoutBLOBS(jid);
            //不是作者自己评论就发送消息
            if (!p.getUserId().equals(user.getId())){
                Message message = new Message();
                message.setId(flowNoService.generateFlowNo());
                //接收人作者
                message.setUserId(p.getUserId());
                message.setCreateTime(new Date());
                //格式化消息：昵称，昵称，帖子ID,回复ID,标题
                String msg = String.format(messageReplyTemp, user.getUsername(), user.getUsername(), jid, reply.getId(), p.getTitle());
                message.setMessage(msg);
                messageService.add(message);
                log.debug(msg);
            }

            //被回复者消息被回复者格式：@username xxx
            Arrays.asList(content.split("@"))
                    .stream()
                    .filter(n -> StringUtils.isNotEmpty(n))
                    .filter(n -> n.indexOf(" ")>-1)
                    .map(n -> n.substring(0, n.indexOf(" ")))
                    .forEach(n -> {
                        User u = userService.getByUserName(n);
                        if (u != null){
                            Message message = new Message();
                            message.setId(flowNoService.generateFlowNo());
                            message.setUserId(u.getId());
                            message.setCreateTime(new Date());
                            //格式化消息：昵称，昵称，帖子ID,回复ID,标题
                            String msg = String.format(messageAnswerTemp, user.getUsername(), user.getUsername(), jid, reply.getId(), p.getTitle());
                            message.setMessage(msg);
                            messageService.add(message);
                            log.debug(msg);
                        }
                    });

        }catch (Exception e){
            log.error("添加消息出错", e);
        }

        result.put("status", 0);
        result.put("action", "/jie/detail?id=" + jid);

        return result;
    }
}
