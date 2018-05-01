package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;

/**
 * <code>CommentAction</code>构建器对象类
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class CommentActionBuilder<T extends CommentActionBuilder<T>> {

    private CommentActionImpl _inst = new CommentActionImpl();

    public CommentActionBuilder(String type) {
        _inst.setType(type);
    }

    public T role(String role) {
        _inst.setRole(role);
        return (T) this;
    }

    public T action(String action) {
        _inst.setAction(action);
        return (T) this;
    }

    public T content(String content) {
        _inst.setContent(content);
        return (T) this;
    }

    public CommentAction build() {
        return _inst.clone();
    }
}
