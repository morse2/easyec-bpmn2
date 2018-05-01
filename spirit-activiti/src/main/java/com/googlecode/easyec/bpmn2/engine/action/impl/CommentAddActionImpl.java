package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAddAction;
import com.googlecode.easyec.bpmn2.engine.action.ex.CommentActionCtrl;

class CommentAddActionImpl extends CommentActionImpl implements CommentAddAction, CommentActionCtrl {

    private String id;
    private String taskId;
    private String instanceId;

    void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected CommentAddAction clone() {
        CommentAddActionImpl _clone = new CommentAddActionImpl();
        _clone.setInstanceId(instanceId);
        _clone.setTaskId(taskId);
        _clone.setId(id);

        _clone.setType(getType());
        _clone.setRole(getRole());
        _clone.setAction(getAction());
        _clone.setContent(getContent());
        _clone.setSystem(isSystem());

        return _clone;
    }
}
