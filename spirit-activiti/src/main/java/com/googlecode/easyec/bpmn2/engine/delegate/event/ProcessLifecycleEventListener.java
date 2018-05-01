package com.googlecode.easyec.bpmn2.engine.delegate.event;

import com.googlecode.easyec.bpmn2.service.FlowStdService;
import org.activiti.engine.delegate.event.ActivitiCancelledEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import static com.googlecode.easyec.bpmn2.domain.Process.ARG_PROCESS_ID;

public class ProcessLifecycleEventListener extends AbstractProcessEventListener implements InitializingBean {

    private FlowStdService flowStdService;

    public void setFlowStdService(FlowStdService flowStdService) {
        this.flowStdService = flowStdService;
    }

    @Override
    protected void onStarted(ActivitiProcessStartedEvent event) {
        flowStdService.makeAsStarted(
            ((String) event.getVariables().get(ARG_PROCESS_ID)),
            event.getProcessInstanceId()
        );
        logger.info("Process is started. Instance id: [{}].",
            event.getProcessInstanceId());
    }

    @Override
    protected void onCancelled(ActivitiCancelledEvent event) {
        flowStdService.makeAsCancelled(_getProcessId(event));
        logger.info("Process is cancelled. Instance id: [{}].",
            event.getProcessInstanceId());
    }

    @Override
    protected void onCompleted(ActivitiEvent event) {
        flowStdService.makeAsFinished(_getProcessId(event));
        logger.info("Process [{}] is completed.",
            event.getProcessInstanceId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(flowStdService, "FlowStdService cannot be null.");
    }

    private String _getProcessId(ActivitiEvent event) {
        return (String) event.getEngineServices()
            .getRuntimeService()
            .getVariable(
                event.getProcessInstanceId(),
                ARG_PROCESS_ID
            );
    }
}
