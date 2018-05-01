package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.ProcessDraftAction;
import com.googlecode.easyec.bpmn2.domain.Process;

class ProcessDraftActionImpl implements ProcessDraftAction, Cloneable {

    private Process process;
    private String definitionKey;

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public String getDefinitionKey() {
        return definitionKey;
    }

    void setProcess(Process process) {
        this.process = process;
    }

    void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    @Override
    protected ProcessDraftAction clone() {
        ProcessDraftActionImpl _clone = new ProcessDraftActionImpl();
        _clone.definitionKey = definitionKey;
        _clone.process = process;

        return _clone;
    }
}
