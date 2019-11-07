package com.yaohan.bbs.elastic;

public class PostsQuery {
    private String condition;
    private int from;
    private int size;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String matchString(){
        return "{\n" +
                "  \"query\" : { \"match\" : { \"title\" : \""+ condition +"\",\"detail\" : \""+ condition +"\" }},\n" +
                "  \"from\": "+ from +",\n" +
                "  \"size\": "+ size +"\n" +
                "  \"highlight\" : {\n" +
                "        \"pre_tags\" : [\"<tag1>\", \"<tag2>\"],\n" +
                "        \"post_tags\" : [\"</tag1>\", \"</tag2>\"],\n" +
                "        \"fields\" : {\n" +
                "            \"detail\" : {}\n" +
                "        }\n" +
                "    }" +
                "}";
    }
}
