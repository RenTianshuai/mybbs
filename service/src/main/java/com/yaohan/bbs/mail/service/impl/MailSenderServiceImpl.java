package com.yaohan.bbs.mail.service.impl;

import com.yaohan.bbs.mail.config.EmailConfig;
import com.yaohan.bbs.mail.dto.EmailDTO;
import com.yaohan.bbs.mail.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class MailSenderServiceImpl implements MailSenderService {

	@Autowired
	EmailConfig emailConfig;


	@Bean(name = "JavaMailSenderImpl")
	public JavaMailSenderImpl getMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(emailConfig.getSmtpHost());
		javaMailSender.setUsername(emailConfig.getSmtpAccount());
		javaMailSender.setPassword(emailConfig.getSmtpPassword());

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", emailConfig.getAuth());
		properties.put("mail.smtp.timeout", emailConfig.getTimeout());

		// 解决阿里云上无法正常发送邮件的问题
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.port", emailConfig.getSmtpPort());
		properties.put("mail.smtp.port", emailConfig.getSmtpPort());

		javaMailSender.setJavaMailProperties(properties);

		return javaMailSender;
	}

	@Override
	public String sendSimpleMail(EmailDTO email) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		simpleMailMessage.setFrom(emailConfig.getSmtpAccount());
		String receiver = email.getReceiver();
		String receivers[] = receiver.split(";");
		simpleMailMessage.setTo(receivers);
		simpleMailMessage.setSubject(email.getSubject());
		simpleMailMessage.setText(email.getContent());
		try {
			getMailSender().send(simpleMailMessage);
		} catch (Exception e) {
			log.error("发送邮件发生错误",e);
		}

		addEmailJournal(email, receiver);

		log.info("邮件发送成功,收件人:" + email.getReceiver() + ",标题:" + email.getSubject());

		return "SUCC";
	}

	private void addEmailJournal(EmailDTO email, String receiver) {
		log.info("此处记录邮件发送历史-现阶段无");
	}

	@Override
	public String sendHtmlMail(EmailDTO email) {
		String result = "SUCC";

		MimeMessage message = getMailSender().createMimeMessage();
		try {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(emailConfig.getSmtpAccount());

			String receiver = email.getReceiver();
			String receivers[] = receiver.split(";");
			helper.setTo(receivers);

			helper.setSubject(email.getSubject());
			helper.setText(email.getContent(), true);
			try {
				getMailSender().send(message);
			} catch (Exception e) {
				log.error("发送邮件发生错误",e);
			}

			addEmailJournal(email, receiver);

			log.info("html格式邮件发送成功,收件人:" + email.getReceiver() + ",标题:" + email.getSubject());
		} catch (Exception e) {
			log.error("html格式邮件发送失败,收件人:" + email.getReceiver() + ",标题:" + email.getSubject());
			result = e.getMessage();
		}

		return result;
	}

	@Override
	public String sendMail(EmailDTO email, boolean realSend) {
		if (realSend){
			if (email.getContent().startsWith("<!DOCTYPE html>")){
				return sendHtmlMail(email);
			}else {
				return sendSimpleMail(email);
			}
		}else {
			addEmailJournal(email, email.getReceiver());
			return "SUCC";
		}
	}
}
