package com.yaohan.bbs.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserModel {

    @ExcelProperty(value = "用户名", index = 0)
    String username;
    @ExcelProperty(value = "实名", index = 1)
    String realname;
    @ExcelProperty(value = "角色", index = 2)
    String roleId;
    @ExcelProperty(value = "学校", index = 3)
    String school;
    @ExcelProperty(value = "班级", index = 4)
    String className;
}
