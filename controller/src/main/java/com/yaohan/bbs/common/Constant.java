package com.yaohan.bbs.common;

public class Constant {

    public static final String ACTIVATE_EMAIL = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>青少年传习论坛激活邮件</title>\n" +
            "</head>\n" +
            "<body style='text-align: center'>\n" +
            "    <h1>欢迎使用青少年传习论坛</h1>\n" +
            "    <br/>\n" +
            "    <h3>请点击链接：<a href='%s/user/activate?code=%s'>激活</a>登录邮箱</h3>\n" +
            "</body>\n" +
            "</html>";

    public static final String REPASS_EMAIL = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>青少年传习论坛找回密码邮件</title>\n" +
            "</head>\n" +
            "<body style='text-align: center'>\n" +
            "    <h1>欢迎使用青少年传习论坛</h1>\n" +
            "    <br/>\n" +
            "    <h3>请点击链接重置为新密码：<a href='%s/user/findNewPass?code=%s'>%s</a></h3>\n" +
            "</body>\n" +
            "</html>";


    public static class Role{
        public static final String MEM = "member";
        public static final String TCH = "teacher";
        public static final String STU = "student";
        public static final String SYS = "sys_admin";
        public static final String VIP = "vip";
        public static final String SCH = "school_admin";
    }

    public static final String YES = "Y";
    public static final String NO = "N";
}
