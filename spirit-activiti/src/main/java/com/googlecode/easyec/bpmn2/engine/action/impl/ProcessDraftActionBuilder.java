package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.ProcessDraftAction;
import com.googlecode.easyec.bpmn2.domain.Process;

@SuppressWarnings("unchecked")
public class ProcessDraftActionBuilder<T extends ProcessDraftActionBuilder<T>> {

    private ProcessDraftActionImpl _inst = new ProcessDraftActionImpl();

    public T object(Process model) {
        _inst.setProcess(model);
        return (T) this;
    }

    public T definition(String key) {
        _inst.setDefinitionKey(key);
        return (T) this;
    }

    public ProcessDraftAction build() {
        return _inst.clone();
    }
}
