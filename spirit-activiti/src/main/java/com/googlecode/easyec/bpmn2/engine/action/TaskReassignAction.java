package com.googlecode.easyec.bpmn2.engine.action;

public interface TaskReassignAction extends TaskAction {

    String getTarget();

    String getOrigin();
}
