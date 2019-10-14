package com.yaohan.bbs.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomPasswordToken extends UsernamePasswordToken {

    private String email;

    private String vercode;

    public CustomPasswordToken(String username, String password, String email, String vercode){
        super(username, password);
        this.email = email;
        this.vercode = vercode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }
}
