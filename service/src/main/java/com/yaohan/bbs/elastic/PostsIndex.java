package com.yaohan.bbs.elastic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PostsIndex{

    @JsonIgnore
    private String id;

    private String title;

    private String detail;
}
