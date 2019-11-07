package com.yaohan.bbs.elastic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ESconfig {

    @Value("${es.url}")
    private String url;

    @Value("${es.index}")
    private String indexName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
