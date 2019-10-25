package com.yaohan.bbs.mail.service;

import com.yaohan.bbs.mail.dto.EmailDTO;

public interface MailSenderService {

	String sendSimpleMail(EmailDTO email);
	
	String sendHtmlMail(EmailDTO email);

	String sendMail(EmailDTO email, boolean realSend);
}
