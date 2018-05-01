package com.googlecode.easyec.bpmn2.engine.bpmn.behavior;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class UserTaskExclusiveGatewayActivityBehavior extends ExclusiveGatewayActivityBehavior {

    private static final Logger logger = LoggerFactory.getLogger(UserTaskExclusiveGatewayActivityBehavior.class);
    private static final String KEY_APPROVED = "approved";
    private static final long serialVersionUID = 1387722718590981548L;

    @Override
    protected void leave(ActivityExecution exec) {
        if (!_isFromUserTask(exec)) {
            super.leave(exec);

            return;
        }

        TaskService taskService = exec.getEngineServices().getTaskService();
        Task _task = taskService.createTaskQuery()
            .executionId(exec.getId())
            .singleResult();

        if (null != _task) {
            Boolean _approved
                = (Boolean) taskService.getVariableLocal(
                _task.getId(), KEY_APPROVED
            );
            logger.info("Task [{}] is approved? [{}]", _task.getId(), _approved);

            exec.setVariableLocal(KEY_APPROVED, _approved);
        }

        try {
            super.leave(exec);
        } finally {
            if (null != _task) {
                exec.removeVariableLocal(KEY_APPROVED);
            }
        }
    }

    private boolean _isFromUserTask(ActivityExecution exec) {
        List<PvmTransition> list = exec.getActivity().getIncomingTransitions();
        if (isEmpty(list)) return false;

        return list.stream().anyMatch(_tran ->
            "userTask".equals(_tran.getSource().getProperty("type"))
        );
    }
}
