package com.googlecode.easyec.bpmn2.engine.action.impl;

import com.googlecode.easyec.bpmn2.engine.action.CommentAction;

abstract class CommentActionAdapter {

    private CommentAction commentAction;

    public CommentAction getComment() {
        return commentAction;
    }

    void setComment(CommentAction commentAction) {
        this.commentAction = commentAction;
    }
}
