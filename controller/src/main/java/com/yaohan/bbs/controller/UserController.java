package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.common.Constant;
import com.yaohan.bbs.dao.entity.*;
import com.yaohan.bbs.mail.config.EmailConfig;
import com.yaohan.bbs.mail.dto.EmailDTO;
import com.yaohan.bbs.service.*;
import com.yaohan.bbs.shiro.CustomPasswordToken;
import com.yaohan.bbs.utils.Util;
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
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController extends BaseController{

    @Value("${upload.images-path}")
    private String imagesPath;

    @Autowired
    EmailConfig emailConfig;

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
    @Autowired
    MessageService messageService;

    @RequestMapping("/login")
    @ResponseBody
    public Map login(String username, String email, String password, String vercode){
        Map result = new HashMap();
        Subject subject = SecurityUtils.getSubject();
        CustomPasswordToken token = new CustomPasswordToken(username, password, email, vercode);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            subject.getSession().removeAttribute("user");
            result.put("status", -1);
            result.put("msg", "密码错误，请重试");
            return result;
        } catch (AuthenticationException e) {
            subject.getSession().removeAttribute("user");
            result.put("status", -1);
            result.put("msg", e.getMessage());
            return result;
        }
        User u = (User) subject.getSession().getAttribute("user");
        result.put("status", 0);
        result.put("msg", "登录成功");
        if ("Y".equals(u.getEmailActivate())){
            result.put("action", "/");
        }else {
            //未激活不让登录
            subject.getSession().removeAttribute("user");
            result.put("action", "/user/activatePage?u="+u.getId());
        }
        return result;
    }

    @RequestMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().getSession().removeAttribute("user");
        return "redirect:/";
    }

    @RequestMapping("/register")
    @ResponseBody
    public Map register(String email, String username, String password, String repassword, String vercode){
        Map result = new HashMap();
        if (StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16){
            result.put("status", -1);
            result.put("msg", "密码为6到16个字符");
            return result;
        }
        if (!password.equals(repassword)){
            result.put("status", -1);
            result.put("msg", "确认密码与密码不符");
            return result;
        }
        User user = userService.getByEmail(email);
        if (user != null){
            result.put("status", -1);
            result.put("msg", "用户邮箱已存在");
            return result;
        }
        if (username.contains("admin") || username.contains("管理员")){
            result.put("status", -1);
            result.put("msg", "用户昵称不能包含admin或管理员");
            return result;
        }
        user = userService.getByUserName(username);
        if (user != null){
            result.put("status", -1);
            result.put("msg", "用户昵称已存在");
            return result;
        }
        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String)session.getAttribute("validateCode");
        if (vercode == null || !vercode.toUpperCase().equals(code)){
            result.put("status", -1);
            result.put("msg", "验证码错误，请重试");
            return result;
        }

        user = new User();
        user.setId(flowNoService.generateFlowNo());
        user.setRoleId("member");
        user.setExperience(0);
        user.setEmail(email);
        user.setEmailActivate("N");
        user.setUsername(username);
        user.setPortrait("/images/avatar/" + Util.randomInt(12) + ".jpg");
        //生成盐值
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        //生成的密文
        String ciphertext = new Md5Hash(password, salt,1).toString();
        user.setSalt(salt);
        user.setPassword(ciphertext);

        user.setSex("M");
        user.setRegisterDate(new Date());

        userService.add(user);

        result.put("status", 0);
        result.put("msg", "注册成功");
        result.put("action", "/user/activatePage?u="+user.getId());
        return result;
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
        Page<Posts> page = postsServcie.findPostsByPage(pageNo, pageSize, params);
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

    @RequestMapping("/jump")
    public String userJumpHome(String username, Model model){
        User user = userService.getByUserName(username);
        if (user == null || StringUtils.isEmpty(user.getId())){
            return "redirect:/";
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
    public Map userSet(String email, String username, String sex, String realname, String phone, String city, String sign, Model model){
        Map result = new HashMap();
        User user = checkUser();
        if (user == null || StringUtils.isEmpty(user.getId())){
            result.put("status", -1);
            result.put("msg", "亲，您还没有登录哦");
            return result;
        }
        User userOld = null;
        if (!user.getEmail().equals(email)){
            userOld = userService.getByEmail(email);
            if (userOld != null){
                result.put("status", -1);
                result.put("msg", "邮箱已使用");
                return result;
            }
            //更换邮箱需重新激活
            user.setEmailActivate("N");
        }
        if (!user.getUsername().equals(username)){
            userOld = userService.getByUserName(username);
            if (userOld != null){
                result.put("status", -1);
                result.put("msg", "用户名已使用");
                return result;
            }
        }
        if (StringUtils.isNotEmpty(phone) && !phone.equals(user.getPhone())){
            userOld = userService.getByPhone(phone);
            if (userOld != null){
                result.put("status", -1);
                result.put("msg", "手机号码已使用");
                return result;
            }
        }
        //修改用户信息
        user.setEmail(email);
        user.setUsername(username);
        user.setSex(sex);
        user.setRealname(realname);
        user.setPhone(phone);
        user.setCity(city);
        user.setSignature(sign);

        userService.update(user);
        refreshUser(user);

        result.put("status", 0);
        result.put("msg", "信息更新成功");
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

    @RequestMapping("/message")
    public String getMessage(Model model){
        //设置菜单点击项
        model.addAttribute("nav", "message");
        List<Message> messages = messageService.findByUserId(checkUser().getId());
        model.addAttribute("messages", messages);
        return "user/message";
    }

    @RequestMapping("/activatePage")
    public String userActivatePage(String u, HttpServletRequest request, Model model){
        User user = null;
        if (StringUtils.isEmpty(u)){
            user =  checkUser();
        }else {
            user = userService.get(u);
        }

        if (StringUtils.isEmpty(user.getAuthInfo())){
            //发送激活邮件
            sendActivateMail(request, user);
        }

        model.addAttribute("activateU", user);
        model.addAttribute("adminEmail", emailConfig.getSmtpAccount());
        model.addAttribute("userMailHost", "http://mail." + user.getEmail().substring(user.getEmail().indexOf("@")+1));

        return "user/activate";
    }

    @RequestMapping("/activate")
    public String userActivate(String code, Model model){
        User user = userService.getByAuthInfo(code);

        if (user == null){
            model.addAttribute("msg", "激活连接失效，请登录重试");
            return "user/login";
        }

        user.setEmailActivate("Y");
        user.setAuthInfo("");
        userService.update(user);

        model.addAttribute("msg", "邮箱激活成功，请登录");
        return "user/login";
    }

    @RequestMapping("/forget")
    public String userForget(Model model){
        return "user/forget";
    }

    @RequestMapping("/findPass")
    @ResponseBody
    public Map findPass(String email, String vercode, HttpServletRequest request) {
        Map result = new HashMap();
        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String) session.getAttribute("validateCode");
        if (vercode == null || !vercode.toUpperCase().equals(code)) {
            result.put("status", -1);
            result.put("msg", "验证码错误，请重试");
            return result;
        }

        User userOld = userService.getByEmail(email);
        if (userOld == null){
            result.put("status", -1);
            result.put("msg", "该邮箱未注册，请重试");
            return result;
        }

        //发送重置链接
        String path = request.getRequestURL().toString();
        String codePass = UUID.randomUUID().toString();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("来自青少年传习论坛的密码重置邮件");
        emailDTO.setReceiver(userOld.getEmail());
        emailDTO.setContent(String.format(Constant.REPASS_EMAIL, path.substring(0, path.indexOf(request.getServletPath())), codePass, codePass.substring(0, 6)));
        mailSenderService.sendMail(emailDTO, true);

        //暂存code在session
        session.setAttribute(codePass, email);

        result.put("status", 0);
        result.put("msg", "重置邮件已发送，请登陆邮箱重置，并使用新密码登录");
        result.put("action", "/user/loginPage");

        return result;
    }

    @RequestMapping("/findNewPass")
    public String findNewPass(String code, Model model){
        Session session = SecurityUtils.getSubject().getSession();
        Object object = session.getAttribute(code);
        if (object == null){
            model.addAttribute("msg", "重置密码链接已失效，请重试");
            return "user/login";
        }

        String email = (String) object;
        User user = userService.getByEmail(email);
        //重置密码
        String ciphertext = new Md5Hash(code.substring(0,6), user.getSalt(),1).toString();
        //修改用户信息
        user.setPassword(ciphertext);
        userService.update(user);

        model.addAttribute("msg", "重置密码成功，请登录");
        return "user/login";
    }
}
