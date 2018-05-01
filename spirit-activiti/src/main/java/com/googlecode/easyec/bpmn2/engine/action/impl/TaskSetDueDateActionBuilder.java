package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskSetDueDateAction;
import org.springframework.util.Assert;

import java.util.Date;

@SuppressWarnings("unchecked")
public class TaskSetDueDateActionBuilder<T extends TaskSetDueDateActionBuilder<T>> {

    private TaskSetDueDateActionImpl _inst = new TaskSetDueDateActionImpl();

    public TaskSetDueDateActionBuilder(String taskId) {
        Assert.notNull(taskId, "Task id mustn't be null.");
        _inst.setTaskId(taskId);
    }

    public T newDate(Date date) {
        _inst.setNew(date);
        return (T) this;
    }

    public TaskSetDueDateAction build() {
        return _inst.clone();
    }
}
