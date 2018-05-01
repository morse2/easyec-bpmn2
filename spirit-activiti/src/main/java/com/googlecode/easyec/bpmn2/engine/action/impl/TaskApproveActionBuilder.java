package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskApproveAction;
import org.springframework.util.Assert;

import java.util.Map;

@SuppressWarnings("unchecked")
public class TaskApproveActionBuilder<T extends TaskApproveActionBuilder<T>> {

    private TaskApproveActionImpl _inst = new TaskApproveActionImpl();

    public TaskApproveActionBuilder(String taskId) {
        Assert.notNull(taskId, "Task ID mustn't be null.");
        _inst.setTaskId(taskId);
    }

    public T comment(CommentAction commentAction) {
        _inst.setComment(commentAction);
        return (T) this;
    }

    public T approved(boolean approved) {
        _inst.setApproved(approved);
        return (T) this;
    }

    public T addArg(String name, Object value) {
        _inst.addArg(name, value);
        return (T) this;
    }

    public T addLocalArg(String name, Object value) {
        _inst.addLocalArg(name, value);
        return (T) this;
    }

    public T setArgs(Map<String, Object> args) {
        _inst.setArgs(args);
        return (T) this;
    }

    public T setLocalArgs(Map<String, Object> localArgs) {
        _inst.setLocalArgs(localArgs);
        return (T) this;
    }

    public TaskApproveAction build() {
        return _inst.clone();
    }
}
