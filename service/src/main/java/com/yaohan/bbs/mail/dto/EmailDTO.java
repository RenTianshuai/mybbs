package com.yaohan.bbs.mail.dto;

import lombok.Data;

@Data
public class EmailDTO {

    String subject;

    String content;

    String receiver;
}
