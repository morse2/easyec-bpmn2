package com.googlecode.easyec.bpmn2;

/**
 * 表示错误的任务状态的异常类
 *
 * @author junjie
 */
public class BadTaskStatusException extends Exception {

    private static final long serialVersionUID = 8634857062918939580L;

    public BadTaskStatusException(String message) {
        super(message);
    }

    public BadTaskStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadTaskStatusException(Throwable cause) {
        super(cause);
    }
}
