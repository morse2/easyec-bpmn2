package com.googlecode.easyec.bpmn2.engine.action.impl;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

abstract class TaskActionAdapter extends CommentActionAdapter {

    private Map<String, Object> localArgs = new HashMap<>();
    private Map<String, Object> args = new HashMap<>();
    private String taskId;

    public void addArg(String name, Object value) {
        this.args.put(name, value);
    }

    public void addLocalArg(String name, Object value) {
        this.localArgs.put(name, value);
    }

    public void setArgs(Map<String, Object> args) {
        if (MapUtils.isNotEmpty(args)) {
            this.args.putAll(args);
        }
    }

    public void setLocalArgs(Map<String, Object> localArgs) {
        if (MapUtils.isNotEmpty(localArgs)) {
            this.localArgs.putAll(localArgs);
        }
    }

    public Map<String, Object> getLocalArgs() {
        return localArgs;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
