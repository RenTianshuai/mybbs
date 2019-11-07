package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.*;
import com.yaohan.bbs.service.*;
import com.yaohan.bbs.vo.PostsVO;
import com.yaohan.bbs.vo.ReplyVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController extends BaseController{

    @Value("${index.top.num}")
    private int topNum;
    @Value("${index.jie.num}")
    private int jieNum;
    @Value("${index.jie.page}")
    private int jiePage;

    @Autowired
    PostsServcie postsServcie;
    @Autowired
    PostsCollectionService postsCollectionService;
    @Autowired
    PostsReplyService postsReplyService;
    @Autowired
    PostsApproveLogService postsApproveLogService;
    @Autowired
    UserLikeLogService userLikeLogService;

    @GetMapping("/")
    public String indexPage(String label, Integer sort,  Model model){
        if (sort==null) {
            sort=0;
        }
        model.addAttribute("selabel", label==null?"":label);
        model.addAttribute("sort", sort);
        //获取前五条置顶贴
        List<Posts> top = postsServcie.topPublishPostsByNum(topNum);
        if (top!=null && top.size()>0){
            List<PostsVO> tops = new ArrayList<>(top.size());
            for (Posts posts:top){
                tops.add(getPostsVO(posts));
            }
            model.addAttribute("tops", tops);
        }

        //获取最近的10条贴
        Map params = new HashMap(1);
        if (StringUtils.isNotEmpty(label)){
            params.put("labelId", label);
        }
        List<Posts> publishPosts = null;
        if (sort==1){
            //按热议
            publishPosts = postsServcie.allPublishPostsByPageAndReplys(1, jieNum, params).getResult();
        }else {
            publishPosts = postsServcie.allPublishPostsByPage(1, jieNum, params).getResult();
        }
        if (publishPosts!=null && publishPosts.size()>0){
            List<PostsVO> vos = new ArrayList<>(publishPosts.size());
            for (Posts posts:publishPosts){
                vos.add(getPostsVO(posts));
            }
            model.addAttribute("vos", vos);
        }

        return "index";
    }

    @RequestMapping("/user/loginPage")
    public String loginPage(){
        return "user/login";
    }

    @RequestMapping("/user/regPage")
    public String regPage(){
        return "user/reg";
    }

    @RequestMapping("/jie/add")
    public String addPage(){
        return "jie/add";
    }

    @RequestMapping("/jie/index")
    public String jieIndex(Integer pageNo, Integer pageSize, String label, Integer sort, Model model){
        if (sort==null) {
            sort=0;
        }
        model.addAttribute("selabel", label==null?"":label);
        model.addAttribute("sort", sort);
        if (pageNo == null || pageNo == 0){
            pageNo = 1;
        }
        if (pageSize == null || pageSize == 0){
            pageSize = jiePage;
        }
        model.addAttribute("count", 0);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        Map params = new HashMap(1);
        if (StringUtils.isNotEmpty(label)){
            params.put("labelId", label);
        }
        Page<Posts> page = null;
        if (sort==1){
            page = postsServcie.allPublishPostsByPageAndReplys(pageNo, pageSize, params);
        }else {
            page = postsServcie.allPublishPostsByPage(pageNo, pageSize, params);
        }
        model.addAttribute("count", page.getTotal());
        List<Posts> publishPosts = page.getResult();
        if (publishPosts!=null && publishPosts.size()>0){
            List<PostsVO> vos = new ArrayList<>(publishPosts.size());
            for (Posts posts:publishPosts){
                vos.add(getPostsVO(posts));
            }
            model.addAttribute("vos", vos);
        }

        return "jie/index";
    }

    @RequestMapping("/jie/detail")
    public String jieDetail(String id, Integer repPageNo, Integer repPageSize, Model model){
        //判断用户是否登录
        User user = checkUser();
        if (user != null && StringUtils.isNotEmpty(user.getId())){
            //检查是否收藏
            model.addAttribute("collect", false);
            if (postsCollectionService.getByUserIdAndPostsId(user.getId(), id) != null){
                model.addAttribute("collect", true);
            }
        }

        if (repPageNo == null || repPageNo == 0){
            repPageNo = 1;
        }
        if (repPageSize == null || repPageSize == 0){
            repPageSize = 10;
        }

        //老师审批日志
        PostsApproveLog approveLog = postsApproveLogService.getNewByPostsId(id);
        model.addAttribute("log", approveLog);

        //获取回复
        model.addAttribute("repCount", 0);
        model.addAttribute("repPageNo", repPageNo);
        model.addAttribute("repPageSize", repPageSize);
        Page<PostsReply> page = postsReplyService.getReplyPageByPostsId(repPageNo, repPageSize, id);
        model.addAttribute("repCount", page.getTotal());
        List<PostsReply> replies = page.getResult();
        if (replies != null && replies.size() > 0){
            List<ReplyVO> replyVOS = new ArrayList<>(replies.size());
            for (PostsReply reply:replies){
                ReplyVO replyVO = new ReplyVO();
                replyVO.setPostsReply(reply);
                replyVO.setUser(userService.get(reply.getUserId()));
                replyVO.setRole(roleService.get(replyVO.getUser().getRoleId()));
                replyVOS.add(replyVO);
            }
            model.addAttribute("reps", replyVOS);
        }

        PostsVO vo = getPostsVO(postsServcie.get(id));

        //检查是否可编辑
        model.addAttribute("editable", false);
        if (user != null && StringUtils.isNotEmpty(user.getId())){
            if (user.getId().equals(vo.getUser().getId())){
                if (!"1".equals(vo.getLabel().getIsApprove())) {
                    //用户登录且不需要审批
                    model.addAttribute("editable", true);
                }else {
                    if (vo.getPosts().getStatus()!=4 && vo.getPosts().getStatus()!=2){
                        //审批帖子不在提交或审批完成时
                        model.addAttribute("editable", true);
                    }
                }
            }
        }

        //是否已点赞
        model.addAttribute("zan", false);
        if (user != null && StringUtils.isNotEmpty(user.getId())){
            if (userLikeLogService.get(user.getId(), id).getZan()){
                model.addAttribute("zan", true);
            }
        }

        //已点赞用户列表
        List<UserLikeLog> likeLogs = userLikeLogService.findZanUsers(id);
        model.addAttribute("likeLogs", likeLogs);

        model.addAttribute("vo", vo);
        return "jie/detail";
    }
}
