package com.googlecode.easyec.bpmn2.engine.action;

import com.googlecode.easyec.bpmn2.domain.Process;

import java.util.Map;

public interface ProcessStartAction extends ProcessAction {

    CommentAction getComment();

    Map<String, Object> getArgs();

    String getDefinitionKey();

    Process getProcess();
}
