package com.googlecode.easyec.bpmn2.service;

import com.googlecode.easyec.bpmn2.TaskNotFoundException;
import com.googlecode.easyec.bpmn2.domain.Comment;
import com.googlecode.easyec.bpmn2.domain.Process;
import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskMessageEventAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskReassignAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskSetDueDateAction;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;

import java.util.List;

public interface FlowStdService {

    Process findByPK(String id);

    Process findByInstId(String instId);

    void makeAsStarted(String procId, String instId);

    void makeAsFinished(String procId);

    void makeAsCancelled(String procId);

    void reassign(TaskReassignAction action) throws TaskNotFoundException, DataPersistenceException;

    void setDueDate(TaskSetDueDateAction action) throws TaskNotFoundException, DataPersistenceException;

    void sendMessage(TaskMessageEventAction action) throws TaskNotFoundException;

    void addComment(String taskId, String instId, CommentAction comAct) throws DataPersistenceException;

    List<Comment> findTaskComments(String taskId);

    List<Comment> findTaskComments(String taskId, String type);

    List<Comment> findComments(String procId);

    List<Comment> findComments(String procId, String type);
}
