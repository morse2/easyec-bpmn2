package com.googlecode.easyec.bpmn2.dao;

import com.googlecode.easyec.bpmn2.domain.Comment;

import java.util.List;
import java.util.Map;

public interface CommentDao {

    int deleteByPrimaryKey(String uidPk);

    int insert(Comment record);

    List<Comment> find(Map<String, Object> params);
}