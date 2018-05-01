package com.googlecode.easyec.bpmn2;

/**
 * 表示任务不存在的异常类
 *
 * @author junjie
 */
public class TaskNotFoundException extends Exception {

    private static final long serialVersionUID = -5811372128627793325L;

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotFoundException(Throwable cause) {
        super(cause);
    }
}
