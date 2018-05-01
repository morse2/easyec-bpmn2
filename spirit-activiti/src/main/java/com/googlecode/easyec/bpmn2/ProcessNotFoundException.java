package com.googlecode.easyec.bpmn2;

/**
 * 表示流程信息没找到的异常类
 *
 * @author junjie
 */
public class ProcessNotFoundException extends Exception {

    private static final long serialVersionUID = 8531553507130991125L;

    public ProcessNotFoundException(String message) {
        super(message);
    }

    public ProcessNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessNotFoundException(Throwable cause) {
        super(cause);
    }
}
