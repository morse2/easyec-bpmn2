package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAddAction;

@SuppressWarnings("unchecked")
public class CommentAddActionBuilder<T extends CommentAddActionBuilder<T>> {

    private CommentAddActionImpl _inst = new CommentAddActionImpl();

    public T taskId(String id) {
        _inst.setTaskId(id);
        return (T) this;
    }

    public T processInstanceId(String id) {
        _inst.setInstanceId(id);
        return (T) this;
    }

    public CommentAddAction build() {
        return _inst.clone();
    }
}
