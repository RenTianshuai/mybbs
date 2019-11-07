package com.yaohan.bbs.controller;

import com.github.pagehelper.Page;
import com.yaohan.bbs.common.Constant;
import com.yaohan.bbs.dao.entity.Organization;
import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.OrganizationService;
import com.yaohan.bbs.service.RoleService;
import com.yaohan.bbs.service.UserService;
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

@Controller
@RequestMapping("/org")
public class OrganizationController extends BaseController {

    @Autowired
    OrganizationService organizationService;
    @Autowired
    FlowNoService flowNoService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;


    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("nav", "sysschool");

        return "sys/organization";

    }

    @RequestMapping("user")
    public String user(Model model){
        model.addAttribute("nav", "sysuser");

        return "sys/user";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map add(String parentId, String grandId, String name, Model model){
        Map result = new HashMap();
        Organization organization = new Organization();
        organization.setId(flowNoService.generateFlowNo());
        organization.setCreateTime(new Date());
        organization.setName(name);
        organization.setDelFlag("0");
        if (StringUtils.isNotEmpty(parentId)){
            organization.setParentId(parentId);
        }
        if (StringUtils.isNotEmpty(grandId)){
            organization.setGrandId(grandId);
        }
        organizationService.add(organization);
        result.put("status", 0);
        result.put("msg", "保存成功");
        result.put("action", "/org/index");
        return result;
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
        Page<Organization> organizationsPage = organizationService.pageList(page, limit);
        result.put("code", 0);
        result.put("msg", "查询结果");
        result.put("count", organizationsPage.getTotal());
        result.put("data", organizationsPage.getResult());
        return result;
    }

    @RequestMapping("/del")
    @ResponseBody
    public Map del(String id){
        Map result = new HashMap(1);
        organizationService.deleteById(id);
        result.put("status", 0);
        return result;
    }

    @RequestMapping("/mod")
    @ResponseBody
    public Map mod(String id, String name){
        Map result = new HashMap(1);
        organizationService.modify(id, name);
        result.put("status", 0);
        return result;
    }

    @RequestMapping("/user/list")
    @ResponseBody
    public Map userList(Integer page, Integer limit, String username){
        Map result = new HashMap();
        if (page == null || page == 0){
            page = 1;
        }
        if (limit == null || limit == 0){
            limit = 10;
        }

        User user = checkUser();

        Map params = new HashMap();
        params.put("username", username);
        if (Constant.Role.TCH.equals(user.getRoleId())){
            params.put("roleId", Constant.Role.STU);
            params.put("school", user.getSchool());
            params.put("grade", user.getGrade());
            params.put("className", user.getClassName());
        }
        Page<User> users = userService.pageAll(page, limit, params);
        List<User> list = users.getResult();
        //替换学校和班级
        if (list != null){
            for (User u:list){
                if (StringUtils.isNotEmpty(u.getSchool())){
                    Organization o = organizationService.get(u.getSchool());
                    if (o!=null){
                        u.setSchool(o.getName());
                    }
                }
                if (StringUtils.isNotEmpty(u.getGrade())){
                    Organization o = organizationService.get(u.getGrade());
                    if (o!=null){
                        u.setGrade(o.getName());
                    }
                }
                if (StringUtils.isNotEmpty(u.getClassName())){
                    Organization o = organizationService.get(u.getClassName());
                    if (o!=null){
                        u.setClassName(o.getName());
                    }
                }
            }
        }
        result.put("code", 0);
        result.put("msg", "查询结果");
        result.put("count", users.getTotal());
        result.put("data", list);
        return result;
    }

    @RequestMapping("/user/mod")
    @ResponseBody
    public Map userMod(String id, String roleId, String school, String grade, String className){
        Map result = new HashMap(2);

        Organization organizationSchool = organizationService.findSchoolByName(school);
        if (organizationSchool == null){
            result.put("status", -1);
            result.put("msg", "学校名称有误");
            return result;
        }
        Organization organizationGrade = organizationService.findByParentIdAndName(organizationSchool.getId(), grade);
        if (organizationGrade == null){
            result.put("status", -1);
            result.put("msg", "年级名称有误");
            return result;
        }
        Organization organizationClass = organizationService.findByParentIdAndName(organizationGrade.getId(), className);
        if (organizationClass == null){
            result.put("status", -1);
            result.put("msg", "班级名称有误");
            return result;
        }
        Role role = roleService.get(roleId);
        if (role == null){
            result.put("status", -1);
            result.put("msg", "角色ID有误");
            return result;
        }
        User user = userService.get(id);
        user.setRoleId(roleId);
        user.setSchool(organizationSchool.getId());
        user.setGrade(organizationGrade.getId());
        user.setClassName(organizationClass.getId());
        userService.update(user);
        result.put("status", 0);
        return result;
    }

    @RequestMapping("/user/remove")
    @ResponseBody
    public Map userRemove(String id){
        Map result = new HashMap(2);

        User user = userService.get(id);

        user.setRoleId(Constant.Role.MEM);
        user.setSchool(null);
        user.setGrade(null);
        user.setClassName(null);

        result.put("status", 0);
        return result;
    }
}
