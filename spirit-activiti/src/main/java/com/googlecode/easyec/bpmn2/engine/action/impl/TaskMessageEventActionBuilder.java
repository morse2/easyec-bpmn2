package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.TaskMessageEventAction;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class TaskMessageEventActionBuilder<T extends TaskMessageEventActionBuilder<T>> {

    private TaskMessageEventActionImpl _inst = new TaskMessageEventActionImpl();

    public TaskMessageEventActionBuilder(String taskId) {
        Assert.notNull(taskId, "Task ID cannot be null.");
        _inst.setTaskId(taskId);
    }

    public T message(String s) {
        _inst.setMessage(s);
        return (T) this;
    }

    public TaskMessageEventAction build() {
        return _inst.clone();
    }
}
