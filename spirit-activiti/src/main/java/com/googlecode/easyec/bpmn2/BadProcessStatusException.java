package com.googlecode.easyec.bpmn2;

/**
 * 表示错误的流程状态的异常类
 *
 * @author junjie
 */
public class BadProcessStatusException extends Exception {

    private static final long serialVersionUID = 5948032592326519187L;

    public BadProcessStatusException(String message) {
        super(message);
    }

    public BadProcessStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadProcessStatusException(Throwable cause) {
        super(cause);
    }
}
