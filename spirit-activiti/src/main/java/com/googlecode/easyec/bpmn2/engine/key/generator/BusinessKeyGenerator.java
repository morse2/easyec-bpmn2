package com.googlecode.easyec.bpmn2.engine.key.generator;

import com.googlecode.easyec.bpmn2.domain.Process;

/**
 * 业务流程KEY的生成器接口类
 *
 * @author JunJie
 */
public interface BusinessKeyGenerator {

    /**
     * 通过给定的流程实体对象，
     * 创建一个新的业务流程KEY
     *
     * @param proc 流程实体对象
     * @return 业务流程KEY
     */
    String generateKey(Process proc);
}
