package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskSetDueDateAction;
import com.googlecode.easyec.bpmn2.engine.action.ex.TaskSetDueDateActionCtrl;

import java.util.Date;

class TaskSetDueDateActionImpl extends CommentActionAdapter implements TaskSetDueDateAction, TaskSetDueDateActionCtrl, Cloneable {

    private String taskId;
    private Date oldDate;
    private Date newDate;

    @Override
    public Date getOld() {
        return oldDate;
    }

    @Override
    public Date getNew() {
        return newDate;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setOld(Date old) {
        this.oldDate = old;
    }

    void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    void setNew(Date newDate) {
        this.newDate = newDate;
    }

    @Override
    protected TaskSetDueDateAction clone() {
        TaskSetDueDateActionImpl _clone = new TaskSetDueDateActionImpl();
        _clone.setComment(getComment());
        _clone.setTaskId(taskId);
        _clone.setNew(newDate);

        return _clone;
    }
}
