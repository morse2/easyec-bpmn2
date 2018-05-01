package com.googlecode.easyec.bpmn2.service.impl;

import com.googlecode.easyec.bpmn2.TaskNotFoundException;
import com.googlecode.easyec.bpmn2.dao.CommentDao;
import com.googlecode.easyec.bpmn2.dao.ProcessDao;
import com.googlecode.easyec.bpmn2.domain.Comment;
import com.googlecode.easyec.bpmn2.domain.Process;
import com.googlecode.easyec.bpmn2.domain.impl.CommentEntity;
import com.googlecode.easyec.bpmn2.engine.action.CommentAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskMessageEventAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskReassignAction;
import com.googlecode.easyec.bpmn2.engine.action.TaskSetDueDateAction;
import com.googlecode.easyec.bpmn2.engine.action.ex.CommentActionCtrl;
import com.googlecode.easyec.bpmn2.engine.action.ex.TaskReassignActionCtrl;
import com.googlecode.easyec.bpmn2.engine.action.ex.TaskSetDueDateActionCtrl;
import com.googlecode.easyec.bpmn2.engine.cmd.AddCommentCmdEx;
import com.googlecode.easyec.bpmn2.service.FlowStdService;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service("flowStdService")
public class FlowStdServiceImpl implements FlowStdService {

    private static final Logger logger = LoggerFactory.getLogger(FlowStdServiceImpl.class);

    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ManagementService managementService;

    @Resource
    private ProcessDao processDao;
    @Resource
    private CommentDao commentDao;

    @Override
    public Process findByPK(String id) {
        return processDao.selectByPrimaryKey(id);
    }

    @Override
    public Process findByInstId(String instId) {
        return processDao.selectByInstanceId(instId);
    }

    @Override
    public void makeAsStarted(String procId, String instId) {
        int i = processDao.setStart(procId, instId, new Date());
        logger.debug("Effect rows for starting T_BP_PROCESS. [{}]", i);
    }

    @Override
    public void makeAsFinished(String procId) {
        int i = processDao.setFinished(procId, new Date());
        logger.debug("Effect rows for finishing T_BP_PROCESS. [{}]", i);
    }

    @Override
    public void makeAsCancelled(String procId) {
        int i = processDao.setDiscard(procId, new Date());
        logger.debug("Effect rows for cancelling T_BP_PROCESS. [{}]", i);
    }

    @Override
    public void reassign(TaskReassignAction action) throws TaskNotFoundException, DataPersistenceException {
        Assert.notNull(action, "TaskReassignAction cannot be null.");

        Task _task
            = taskService.createTaskQuery()
            .taskId(action.getTaskId())
            .singleResult();

        if (null == _task) {
            throw new TaskNotFoundException(
                "Task cannot be found. [" + action.getTaskId() + "]."
            );
        }

        if (action instanceof TaskReassignActionCtrl) {
            ((TaskReassignActionCtrl) action).setOrigin(
                _task.getAssignee()
            );
        }

        taskService.setAssignee(
            action.getTaskId(),
            action.getTarget()
        );

        addComment(
            _task.getId(),
            _task.getProcessInstanceId(),
            action.getComment()
        );
    }

    @Override
    public void setDueDate(TaskSetDueDateAction action) throws TaskNotFoundException, DataPersistenceException {
        Task _task
            = taskService.createTaskQuery()
            .taskId(action.getTaskId())
            .singleResult();

        if (null == _task) {
            throw new TaskNotFoundException(
                "Task cannot be found. [" + action.getTaskId() + "]."
            );
        }

        if (action instanceof TaskSetDueDateActionCtrl) {
            ((TaskSetDueDateActionCtrl) action).setOld(
                _task.getDueDate()
            );
        }

        taskService.setDueDate(
            action.getTaskId(),
            action.getNew()
        );

        addComment(
            _task.getId(),
            _task.getProcessInstanceId(),
            action.getComment()
        );
    }

    @Override
    public void sendMessage(TaskMessageEventAction action) throws TaskNotFoundException {
        Task _task = taskService.createTaskQuery()
            .taskId(action.getTaskId())
            .singleResult();

        if (null == _task) {
            throw new TaskNotFoundException(
                "Task isn't found. [" + action.getTaskId() + "]."
            );
        }

        List<Execution> result
            = runtimeService.createExecutionQuery()
            .messageEventSubscriptionName(action.getMessage())
            .processInstanceId(_task.getProcessInstanceId())
            .list();

        if (isNotEmpty(result)) {
            result.forEach(_exec ->
                runtimeService.messageEventReceived(
                    action.getMessage(), _exec.getId()
                )
            );
        }
    }

    @Override
    public void addComment(String taskId, String instId, CommentAction comAct) throws DataPersistenceException {
        if (null != comAct) {
            try {
                CommentEntity _inst = (CommentEntity)
                    managementService.executeCommand(
                        new AddCommentCmdEx(
                            taskId, instId,
                            comAct.getType(),
                            comAct.getContent()
                        )
                    );

                // 设置扩展字段值
                _inst.setRoleAction(comAct.getAction());
                _inst.setSystem(comAct.isSystem());
                _inst.setRole(comAct.getRole());

                int j = commentDao.insert(_inst);
                logger.debug("Effect rows for inserting T_BP_COMMENT. [{}]", j);

                // 回填备注的ID值
                if (comAct instanceof CommentActionCtrl) {
                    ((CommentActionCtrl) comAct).setId(_inst.getId());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new DataPersistenceException(e);
            }
        }
    }

    @Override
    public List<Comment> findTaskComments(String taskId) {
        return findTaskComments(taskId, "by_approval");
    }

    @Override
    public List<Comment> findTaskComments(String taskId, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("taskId", taskId);
        params.put("type", type);

        return commentDao.find(params);
    }

    @Override
    public List<Comment> findComments(String procId) {
        return findComments(procId, "by_approval");
    }

    @Override
    public List<Comment> findComments(String procId, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("procId", procId);
        params.put("type", type);

        return commentDao.find(params);
    }
}
