package com.googlecode.easyec.bpmn2.domain;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

import java.util.Date;

public interface Process extends GenericPersistentDomainModel<String> {

    /** 状态：草稿 */
    String STATUS_DRAFT = "draft";
    /** 状态：进行中 */
    String STATUS_IN_PROGRESS = "in_progress";
    /** 状态：完成 */
    String STATUS_COMPLETE = "complete";
    /** 状态：归档 */
    String STATUS_ARCHIVE = "archive";
    /** 状态：废弃 */
    String STATUS_DISCARD = "discard";

    /** 流程参数：流程ID */
    String ARG_PROCESS_ID = "processId";
    /** 流程参数：流程执行ID */
    String ARG_EXEC_ID = "executionId";

    String getDefinitionId();

    void setDefinitionId(String definitionId);

    String getInstanceId();

    void setInstanceId(String instanceId);

    String getStatus();

    void setStatus(String status);

    String getType();

    void setType(String type);

    String getCreateBy();

    void setCreateBy(String createBy);

    Date getCreateTime();

    void setCreateTime(Date createTime);

    Date getStartTime();

    void setStartTime(Date startTime);

    Date getEndTime();

    void setEndTime(Date endTime);
}
