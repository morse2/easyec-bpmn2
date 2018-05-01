package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.ProcessDiscardAction;

class ProcessDiscardActionImpl extends CommentActionAdapter implements ProcessDiscardAction, Cloneable {

    private String processId;

    @Override
    public String getProcessId() {
        return processId;
    }

    void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    protected ProcessDiscardAction clone() {
        ProcessDiscardActionImpl _clone = new ProcessDiscardActionImpl();
        _clone.setComment(getComment());
        _clone.processId = processId;

        return _clone;
    }
}
