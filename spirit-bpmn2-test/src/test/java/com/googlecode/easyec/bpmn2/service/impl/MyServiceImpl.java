package com.googlecode.easyec.bpmn2.service.impl;

import com.googlecode.easyec.bpmn2.service.MyService;
import org.springframework.stereotype.Service;

@Service("myService")
public class MyServiceImpl extends FlowOperateServiceAdapter implements MyService {

    @Override
    public boolean shouldRMApprove(String procId) {
        return false;
    }

    @Override
    public String getQD(String procId) {
        return "kermit";
    }

    @Override
    public String getQDManager(String procId) {
        return "gonzo";
    }

    @Override
    public String getRM(String procId) {
        return "evans";
    }
}
