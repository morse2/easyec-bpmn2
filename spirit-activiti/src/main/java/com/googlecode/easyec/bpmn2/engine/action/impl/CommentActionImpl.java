package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;

class CommentActionImpl implements CommentAction, Cloneable {

    private String type;
    private String role;
    private String action;
    private String content;
    private boolean system;

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public boolean isSystem() {
        return system;
    }

    void setType(String type) {
        this.type = type;
    }

    void setRole(String role) {
        this.role = role;
    }

    void setAction(String action) {
        this.action = action;
    }

    void setContent(String content) {
        this.content = content;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    @Override
    protected CommentAction clone() {
        CommentActionImpl _clone = new CommentActionImpl();
        _clone.type = this.type;
        _clone.role = this.role;
        _clone.action = this.action;
        _clone.content = this.content;
        _clone.system = system;

        return _clone;
    }
}
