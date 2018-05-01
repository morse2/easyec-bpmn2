package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskMessageEventAction;

class TaskMessageEventActionImpl extends CommentActionAdapter implements TaskMessageEventAction, Cloneable {

    private String taskId;
    private String message;

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected TaskMessageEventAction clone() {
        TaskMessageEventActionImpl _clone = new TaskMessageEventActionImpl();
        _clone.setComment(getComment());
        _clone.setMessage(message);
        _clone.setTaskId(taskId);

        return _clone;
    }
}
