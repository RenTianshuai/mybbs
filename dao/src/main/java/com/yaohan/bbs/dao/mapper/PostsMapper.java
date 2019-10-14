package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.Posts;

import java.util.List;
import java.util.Map;

public interface PostsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Posts record);

    int insertSelective(Posts record);

    Posts selectByPrimaryKey(String id);

    Posts selectByPrimaryKeyWithoutBLOBS(String id);

    int updateByPrimaryKeySelective(Posts record);

    int updateByPrimaryKeyWithBLOBs(Posts record);

    int updateByPrimaryKey(Posts record);

    List<Posts> findAllPubishPosts();

    List<Posts> findAllPubishPostsBy(Map params);

    List<Posts> topPublishPostsByNum(int num);

}