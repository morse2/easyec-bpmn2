package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskReassignAction;
import com.googlecode.easyec.bpmn2.engine.action.ex.TaskReassignActionCtrl;

class TaskReassignActionImpl extends CommentActionAdapter implements TaskReassignAction, TaskReassignActionCtrl, Cloneable {

    private String taskId;
    private String origin;
    private String target;

    @Override
    public String getOrigin() {
        return origin;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    protected TaskReassignAction clone() {
        TaskReassignActionImpl _clone = new TaskReassignActionImpl();
        _clone.setComment(getComment());
        _clone.setTaskId(getTaskId());
        _clone.setTarget(target);

        return _clone;
    }
}
