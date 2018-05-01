package com.googlecode.easyec.bpmn2.engine.action;

public interface CommentAddAction extends CommentAction {

    String getTaskId();

    String getInstanceId();

    String getId();
}
