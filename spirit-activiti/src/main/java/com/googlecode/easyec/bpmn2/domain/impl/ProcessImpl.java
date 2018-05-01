package com.googlecode.easyec.bpmn2.domain.impl;

import com.googlecode.easyec.spirit.dao.id.annotation.Identifier;

import java.util.Date;

@Identifier("SEQ_BP_PROCESS")
public class ProcessImpl implements com.googlecode.easyec.bpmn2.domain.Process {

    private static final long serialVersionUID = -573407114431527604L;
    private String uidPk;
    private String definitionId;
    private String instanceId;
    private String status;
    private String type;
    private String createBy;
    private Date createTime;
    private Date startTime;
    private Date endTime;

    public String getUidPk() {
        return uidPk;
    }

    public void setUidPk(String uidPk) {
        this.uidPk = uidPk == null ? null : uidPk.trim();
    }

    @Override
    public String getDefinitionId() {
        return definitionId;
    }

    @Override
    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId == null ? null : definitionId.trim();
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}