package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.ProcessStartAction;
import com.googlecode.easyec.bpmn2.domain.Process;

import java.util.Map;

@SuppressWarnings("unchecked")
public class ProcessStartActionBuilder<T extends ProcessStartActionBuilder<T>> {

    private ProcessStartActionImpl _inst = new ProcessStartActionImpl();

    public T comment(CommentAction commentAction) {
        this._inst.setComment(commentAction);
        return (T) this;
    }

    public T arg(String name, Object value) {
        this._inst.setArg(name, value);
        return (T) this;
    }

    public T args(Map<String, Object> variables) {
        this._inst.setArgs(variables);
        return (T) this;
    }

    public T object(Process model) {
        this._inst.setProcess(model);
        return (T) this;
    }

    public T definition(String key) {
        this._inst.setDefinitionKey(key);
        return (T) this;
    }

    public ProcessStartAction build() {
        return _inst.clone();
    }
}
