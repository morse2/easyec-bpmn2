package com.googlecode.easyec.bpmn2.service;

public interface MyService extends FlowOperateService {


    boolean shouldRMApprove(String procId);

    String getQD(String procId);

    String getQDManager(String procId);

    String getRM(String procId);
}
