package com.googlecode.easyec.bpmn2.dao;

import com.googlecode.easyec.bpmn2.domain.Process;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ProcessDao {

    int deleteByPrimaryKey(String uidPk);

    int insert(Process record);

    Process selectByPrimaryKey(String uidPk);

    int countByPK(String id);

    int setDefinitionId(@Param("procId") String procId, @Param("defId") String definitionId);

    int setStart(@Param("procId") String procId, @Param("instId") String instId, @Param("time") Date startTime);

    int setDiscard(@Param("procId") String procId, @Param("time") Date endTime);

    int setFinished(@Param("procId") String procId, @Param("time") Date endTime);

    Process selectByInstanceId(String instId);
}