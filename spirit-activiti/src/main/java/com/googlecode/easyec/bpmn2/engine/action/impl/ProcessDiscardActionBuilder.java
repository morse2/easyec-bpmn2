package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.ProcessDiscardAction;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class ProcessDiscardActionBuilder<T extends ProcessDiscardActionBuilder<T>> {

    private ProcessDiscardActionImpl _inst = new ProcessDiscardActionImpl();

    public ProcessDiscardActionBuilder(String processId) {
        Assert.notNull(processId, "Process ID mustn't be null.");
        _inst.setProcessId(processId);
    }

    public T comment(CommentAction commentAction) {
        _inst.setComment(commentAction);
        return (T) this;
    }

    public ProcessDiscardAction build() {
        return _inst.clone();
    }
}
