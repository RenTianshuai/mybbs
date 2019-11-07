package com.yaohan.bbs.elastic;

import com.alibaba.fastjson.JSONObject;
import com.yaohan.bbs.elastic.config.ESconfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ESOperatorService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ESconfig eSconfig;

    private final String mappingConfig = "{\n" +
            "  \"mappings\": {\n" +
            "    \"posts\": {\n" +
            "      \"properties\": {\n" +
            "        \"title\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"search_analyzer\": \"ik_smart\"\n" +
            "        },\n" +
            "        \"detail\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"search_analyzer\": \"ik_smart\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private void createIndex(){
        try {
            restTemplate.put(eSconfig.getUrl() + eSconfig.getIndexName(), mappingConfig);
        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }

    public void addPostsIndex(PostsIndex postsIndex){
        try {
            restTemplate.put(eSconfig.getUrl() + eSconfig.getIndexName()+"/posts/"+postsIndex.getId(), postsIndex);
        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }

    public void deletePostsIndex(PostsIndex postsIndex){
        try {
            restTemplate.delete(eSconfig.getUrl() + eSconfig.getIndexName()+"/posts/"+postsIndex.getId());
        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }

    public JSONObject queryIndex(PostsQuery postsQuery){
        return restTemplate.getForObject(eSconfig.getUrl() + eSconfig.getIndexName()+"/posts/_search", JSONObject.class, postsQuery.matchString());
    }
}
