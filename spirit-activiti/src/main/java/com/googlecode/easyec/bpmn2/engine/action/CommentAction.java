package com.googlecode.easyec.bpmn2.engine.action;

public interface CommentAction {

    String getContent();

    String getType();

    String getRole();

    String getAction();

    boolean isSystem();
}
