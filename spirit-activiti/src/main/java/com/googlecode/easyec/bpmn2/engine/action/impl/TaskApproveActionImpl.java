package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskApproveAction;

class TaskApproveActionImpl extends TaskActionAdapter implements TaskApproveAction, Cloneable {

    private boolean approved;

    TaskApproveActionImpl() {
        this(false);
    }

    private TaskApproveActionImpl(boolean approved) {
        setApproved(approved);
    }

    void setApproved(boolean approved) {
        this.approved = approved;
        addLocalArg(ARG_APPROVED, approved);
    }

    @Override
    public boolean isApproved() {
        return approved;
    }

    @Override
    protected TaskApproveAction clone() {
        TaskApproveActionImpl _clone = new TaskApproveActionImpl(approved);
        _clone.setComment(getComment());
        _clone.setLocalArgs(getLocalArgs());
        _clone.setTaskId(getTaskId());
        _clone.setArgs(getArgs());

        return _clone;
    }
}
