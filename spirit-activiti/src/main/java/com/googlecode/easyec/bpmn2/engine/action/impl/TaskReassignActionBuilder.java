package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskReassignAction;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class TaskReassignActionBuilder<T extends TaskReassignActionBuilder<T>> {

    private TaskReassignActionImpl _inst = new TaskReassignActionImpl();

    public TaskReassignActionBuilder(String taskId) {
        Assert.notNull(taskId, "Task ID mustn't be null.");
        _inst.setTaskId(taskId);
    }

    public T comment(CommentAction commentAction) {
        _inst.setComment(commentAction);
        return (T) this;
    }

    public T target(String target) {
        _inst.setTarget(target);
        return (T) this;
    }

    public TaskReassignAction build() {
        return _inst.clone();
    }
}
