package com.yaohan.bbs.mail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Base64;

@Configuration
@Slf4j
public class EmailConfig {
	@Autowired
	private Environment env;

	public String getSmtpHost() {
		return env.getProperty("smtp.host");
	}
	
	public int getSmtpPort() {
		return Integer.parseInt(env.getProperty("smtp.port"));
	}

	public String getSmtpAccount() {
		return env.getProperty("smtp.account");
	}

	public String getSmtpPassword() {
		byte[] dec = Base64.getDecoder().decode(env.getProperty("smtp.password"));
		return new String(dec);
	}

	public boolean getAuth() {
		return env.getProperty("mail.smtp.auth", Boolean.class);
	}

	public int getTimeout() {
		return env.getProperty("mail.smtp.timeout", Integer.class);
	}
}
