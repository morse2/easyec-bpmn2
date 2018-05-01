package com.googlecode.easyec.bpmn2.engine.cmd;

import com.googlecode.easyec.bpmn2.domain.impl.CommentEntity;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.impl.cmd.AddCommentCmd;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.Task;

public class AddCommentCmdEx extends AddCommentCmd {

    public AddCommentCmdEx(String taskId, String processInstanceId, String message) {
        super(taskId, processInstanceId, message);
    }

    public AddCommentCmdEx(String taskId, String processInstanceId, String type, String message) {
        super(taskId, processInstanceId, type, message);
    }

    @Override
    public Comment execute(CommandContext commandContext) {
        TaskEntity _task = null;

        // Validate task
        if (taskId != null) {
            _task = commandContext.getTaskEntityManager().findTaskById(taskId);

            if (_task == null) {
                throw new ActivitiObjectNotFoundException("Cannot find task with id " + taskId, Task.class);
            }

            if (_task.isSuspended()) {
                throw new ActivitiException(getSuspendedTaskException());
            }
        }

        if (processInstanceId != null) {
            ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);

            if (execution == null) {
                throw new ActivitiObjectNotFoundException("execution " + processInstanceId + " doesn't exist", Execution.class);
            }

            if (execution.isSuspended()) {
                throw new ActivitiException(getSuspendedExceptionMessage());
            }
        }

        if (_task != null && processInstanceId == null) {
            processInstanceId = _task.getProcessInstanceId();
        }

        String userId = Authentication.getAuthenticatedUserId();
        CommentEntity comment = new CommentEntity();
        comment.setUserId(userId);
        comment.setType((type == null) ? CommentEntity.TYPE_COMMENT : type);
        comment.setTime(commandContext.getProcessEngineConfiguration().getClock().getCurrentTime());
        comment.setTaskId(taskId);
        comment.setProcessInstanceId(processInstanceId);
        comment.setAction(Event.ACTION_ADD_COMMENT);

        String eventMessage = message.replaceAll("\\s+", " ");
        if (eventMessage.length() > 163) {
            eventMessage = eventMessage.substring(0, 160) + "...";
        }
        comment.setMessage(eventMessage);

        comment.setFullMessage(message);

        commandContext
            .getCommentEntityManager()
            .insert(comment);

        return comment;
    }
}
