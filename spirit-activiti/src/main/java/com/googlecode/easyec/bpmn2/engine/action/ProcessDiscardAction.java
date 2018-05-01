package com.googlecode.easyec.bpmn2.engine.action;

public interface ProcessDiscardAction extends ProcessAction {

    CommentAction getComment();

    String getProcessId();
}
