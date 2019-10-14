package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsCollection;
import com.yaohan.bbs.dao.entity.PostsReply;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.*;
import com.yaohan.bbs.shiro.CustomPasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController extends BaseController{

    @Value("${upload.images-path}")
    private String imagesPath;

    @Autowired
    UserService userService;
    @Autowired
    FlowNoService flowNoService;
    @Autowired
    PostsServcie postsServcie;
    @Autowired
    PostsCollectionService postsCollectionService;
    @Autowired
    PostsReplyService postsReplyService;
    @Autowired
    RoleService roleService;

    @RequestMapping("/login")
    public String login(String username, String email, String password, String vercode, Model model){
        Subject subject = SecurityUtils.getSubject();
        CustomPasswordToken token = new CustomPasswordToken(username, password, email, vercode);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            subject.getSession().removeAttribute("user");
            model.addAttribute("msg", "密码错误，请重试");
            return "user/login";
        } catch (AuthenticationException e) {
            subject.getSession().removeAttribute("user");
            model.addAttribute("msg", e.getMessage());
            return "user/login";
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().getSession().removeAttribute("user");
        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String email, String username, String password, String repassword, String vercode, Model model){
        if (StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16){
            model.addAttribute("msg", "密码为6到16个字符");
            return "user/reg";
        }
        if (!password.equals(repassword)){
            model.addAttribute("msg", "确认密码与密码不符");
            return "user/reg";
        }
        User user = userService.getByEmail(email);
        if (user != null){
            model.addAttribute("msg", "用户邮箱已存在");
            return "user/reg";
        }
        user = userService.getByUserName(username);
        if (user != null){
            model.addAttribute("msg", "用户昵称已存在");
            return "user/reg";
        }
        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String)session.getAttribute("validateCode");
        if (vercode == null || !vercode.toUpperCase().equals(code)){
            model.addAttribute("msg", "验证码错误,请重试");
            return "user/reg";
        }

        user = new User();
        user.setId(flowNoService.generateFlowNo());
        user.setRoleId("member");
        user.setExperience(0);
        user.setEmail(email);
        user.setUsername(username);
        user.setPortrait("/images/avatar/default.png");
        //生成盐值
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        //生成的密文
        String ciphertext = new Md5Hash(password, salt,1).toString();
        user.setSalt(salt);
        user.setPassword(ciphertext);

        user.setSex("M");
        user.setRegisterDate(new Date());

        userService.add(user);

        return "user/login";
    }

    @RequestMapping("/index")
    public String userIndex(Integer pageNo, Integer pageSize, Model model){
        //设置菜单点击项
        model.addAttribute("nav", "index");
        if (pageNo == null || pageNo == 0){
            pageNo = 1;
        }
        if (pageSize == null || pageSize == 0){
            pageSize = 10;
        }
        //获取回复
        model.addAttribute("count", 0);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        Map params = new HashMap(1);
        params.put("userId", checkUser().getId());
        Page<Posts> page = postsServcie.allPublishPostsByPage(pageNo, pageSize, params);
        model.addAttribute("count", page.getTotal());
        model.addAttribute("postsList", page.getResult());
        return "user/index";
    }

    @RequestMapping("/collection")
    public String userCollection(Integer pageNo, Integer pageSize, Model model){
        //设置菜单点击项
        model.addAttribute("nav", "collection");
        if (pageNo == null || pageNo == 0){
            pageNo = 1;
        }
        if (pageSize == null || pageSize == 0){
            pageSize = 10;
        }
        //获取回复
        model.addAttribute("count", 0);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        Map params = new HashMap(1);
        params.put("userId", checkUser().getId());
        Page<PostsCollection> page = postsCollectionService.pageByUserId(pageNo, pageSize, checkUser().getId());
        model.addAttribute("count", page.getTotal());
        model.addAttribute("collections", page.getResult());
        return "user/collection";
    }

    @RequestMapping("/home")
    public String userHome(String id, Model model){
        User user = null;
        if (StringUtils.isEmpty(id)){
            user =  checkUser();
        }else {
            user = userService.get(id);
        }
        model.addAttribute("userInfo", user);
        model.addAttribute("roleInfo", roleService.get(user.getRoleId()));

        //最近发帖10条
        Map params = new HashMap(1);
        params.put("userId", user.getId());
        List<Posts> recentPosts = postsServcie.allPublishPostsByPage(1, 10, params).getResult();
        if (recentPosts!=null && recentPosts.size()>0){
            model.addAttribute("currPosts", recentPosts);
        }
        //最近评论10条
        List<PostsReply> recentReplys = postsReplyService.getRecentReplyByUserId(user.getId());
        if (recentReplys!=null && recentReplys.size()>0){
            model.addAttribute("currReplys", recentReplys);
        }
        return "user/home";
    }

    @RequestMapping("/set")
    public String userSet(Model model){
        //设置菜单点击项
        model.addAttribute("nav", "set");
        return "user/set";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map userSet(String email, String username, String sex, String phone, String city, String sign, Model model){
        Map result = new HashMap();

        User user = checkUser();
        //修改用户信息

        result.put("status", 0);
        result.put("action", "/user/set");

        return result;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map userSet(@RequestParam MultipartFile file, HttpServletRequest request, Model model){
        User user = checkUser();
        Map result = new HashMap();
        if (file.isEmpty()) {
            result.put("status", -1);
            result.put("msg", "上传文件为空");
            return result;
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 新文件名:用户ID + 后缀
        fileName = user.getId() + suffixName;
        File dest = new File(imagesPath + "avatar"+ File.separator + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            result.put("status", -1);
            result.put("msg", "文件上传出错");
            return result;
        }

        String avatar = "/upload/images/avatar/" + fileName;
        //修改用户信息
        user = userService.get(user.getId());
        user.setPortrait(avatar);

        userService.update(user);

        user.setRoleName(roleService.get(user.getRoleId()).getName());
        refreshUser(user);

        result.put("status", 0);
        result.put("url", avatar);
        result.put("msg", "更新头像成功");

        return result;
    }

    @RequestMapping("/repass")
    @ResponseBody
    public Map userSet(String nowpass, String pass, String repass, Model model){
        User user = checkUser();
        Map result = new HashMap();
        if (StringUtils.isEmpty(pass) || pass.length() < 6 || pass.length() > 16){
            result.put("status", -1);
            result.put("msg", "密码为6到16个字符");
            return result;
        }
        if (!pass.equals(repass)){
            result.put("status", -1);
            result.put("msg", "确认密码与密码不符");
            return result;
        }
        //生成的密文
        String ciphertext = new Md5Hash(nowpass, user.getSalt(),1).toString();
        if (!ciphertext.equals(user.getPassword())){
            result.put("status", -1);
            result.put("msg", "密码输入错误");
            return result;
        }
        //重置密码
        ciphertext = new Md5Hash(pass, user.getSalt(),1).toString();
        //修改用户信息
        user = userService.get(user.getId());
        user.setPassword(ciphertext);
        userService.update(user);

        user.setRoleName(roleService.get(user.getRoleId()).getName());
        refreshUser(user);

        result.put("status", 0);
        result.put("msg", "密码修改成功");
        result.put("action", "/user/set");

        return result;
    }
}
