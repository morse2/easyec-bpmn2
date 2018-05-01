package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.ProcessStartAction;
import com.googlecode.easyec.bpmn2.domain.Process;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

class ProcessStartActionImpl extends CommentActionAdapter implements ProcessStartAction, Cloneable {

    private Map<String, Object> args = new HashMap<String, Object>();
    private String definitionKey;
    private Process process;

    void setArg(String name, Object value) {
        this.args.put(name, value);
    }

    void setArgs(Map<String, Object> args) {
        if (MapUtils.isNotEmpty(args)) {
            this.args.putAll(args);
        }
    }

    void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public Map<String, Object> getArgs() {
        return args;
    }

    @Override
    public String getDefinitionKey() {
        return definitionKey;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    protected ProcessStartAction clone() {
        ProcessStartActionImpl _clone = new ProcessStartActionImpl();
        _clone.setComment(getComment());
        _clone.definitionKey = definitionKey;
        _clone.process = process;
        _clone.args.putAll(args);

        return _clone;
    }
}
