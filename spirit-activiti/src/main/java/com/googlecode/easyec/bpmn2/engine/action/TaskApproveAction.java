package com.googlecode.easyec.bpmn2.engine.action;

import java.util.Map;

public interface TaskApproveAction extends TaskAction {

    String ARG_APPROVED = "approved";

    boolean isApproved();

    Map<String, Object> getArgs();

    Map<String, Object> getLocalArgs();
}
