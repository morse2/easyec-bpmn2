package com.googlecode.easyec.bpmn2.engine.delegate.event;

import org.activiti.engine.delegate.event.ActivitiCancelledEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProcessEventListener implements ActivitiEventListener {

    protected final Logger logger = LoggerFactory.getLogger(ProcessLifecycleEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case PROCESS_STARTED:
                onStarted((ActivitiProcessStartedEvent) event);
                break;
            case PROCESS_CANCELLED:
                onCancelled((ActivitiCancelledEvent) event);
                break;
            case PROCESS_COMPLETED:
            case PROCESS_COMPLETED_WITH_ERROR_END_EVENT:
                onCompleted(event);
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

    abstract protected void onStarted(ActivitiProcessStartedEvent event);

    abstract protected void onCancelled(ActivitiCancelledEvent event);

    abstract protected void onCompleted(ActivitiEvent event);
}
