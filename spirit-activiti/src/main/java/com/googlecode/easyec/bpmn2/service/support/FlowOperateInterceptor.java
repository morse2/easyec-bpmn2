package com.googlecode.easyec.bpmn2.service.support;

import com.googlecode.easyec.bpmn2.BadProcessStatusException;
import com.googlecode.easyec.bpmn2.BadTaskStatusException;
import com.googlecode.easyec.bpmn2.ProcessNotFoundException;
import com.googlecode.easyec.bpmn2.TaskNotFoundException;
import com.googlecode.easyec.bpmn2.engine.action.*;
import com.googlecode.easyec.bpmn2.dao.ProcessDao;
import com.googlecode.easyec.bpmn2.domain.Process;
import com.googlecode.easyec.bpmn2.engine.key.generator.BusinessKeyGenerator;
import com.googlecode.easyec.bpmn2.engine.key.generator.impl.DefaultBusinessKeyGenerator;
import com.googlecode.easyec.bpmn2.service.FlowStdService;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.easyec.bpmn2.domain.Process.*;
import static org.activiti.engine.impl.identity.Authentication.getAuthenticatedUserId;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

/**
 * 流程过程操作的业务拦截类。
 * <p>
 * 该类用于桥接和分离BPMN框架与业务层逻辑代码，
 * 使业务代码只需要兼顾业务，而不需要操作流程框架。
 * </p>
 *
 * @author JunJie
 */
@Aspect
public final class FlowOperateInterceptor implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(FlowOperateInterceptor.class);

    private int order;

    /**
     * 设置此拦截类的执行顺序
     *
     * @param order 数值
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    // ----- inject spring beans
    @Resource
    private ProcessDao processDao;

    @Resource
    private FlowStdService flowStdService;

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    private BusinessKeyGenerator businessKeyGenerator = new DefaultBusinessKeyGenerator();

    public void setBusinessKeyGenerator(BusinessKeyGenerator businessKeyGenerator) {
        this.businessKeyGenerator = businessKeyGenerator;
    }
    // ----- business process operation logic here

    /**
     * 执行预创建流程数据的后置方法。
     *
     * @param action <code>ProcessDraftAction</code>对象实例
     */
    @Before("execution(* com.*..*.service.*Service.save(..)) && args(action,..)")
    public void beforeSave(ProcessDraftAction action) throws Throwable {
        Process _proc = action.getProcess();
        Assert.notNull(_proc, "Process object mustn't be null.");

        _saveAsDraft(_proc, action.getDefinitionKey());
    }

    /**
     * 执行删除流程数据的后置方法。
     *
     * @param action <code>ProcessDraftAction</code>对象实例
     */
    @AfterReturning("execution(* com.*..*.service.*Service.delete(..)) && args(action,..)")
    public void afterDelete(ProcessDraftAction action) throws Throwable {
        Process _proc = action.getProcess();
        Assert.notNull(_proc, "No Process object was present.");

        if (logger.isInfoEnabled()) {
            logger.info("Prepare to delete a draft process. {");
            logger.info("\tProcess create user: [{}].", _proc.getCreateBy());
            logger.info("\tProcess definition id: [{}]. }", _proc.getDefinitionId());
        }

        String _procId = _proc.getUidPk();
        logger.debug("Process ID: [{}].", _procId);

        _proc = processDao.selectByPrimaryKey(_procId);
        if (null == _proc) {
            throw new ProcessNotFoundException(
                "Process cannot be found. ID: [" + _procId + "]."
            );
        }

        if (!STATUS_DRAFT.equals(_proc.getStatus())) {
            throw new BadProcessStatusException(
                "Process [" + _procId + "] isn't draft. Actual: ["
                    + _proc.getStatus() + "]."
            );
        }

        try {
            int i = processDao.deleteByPrimaryKey(_procId);
            logger.debug("Effect rows for deleting T_BP_PROCESS. [{}]", i);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }
    }

    /**
     * 执行废弃流程实例的后置方法
     *
     * @param action 流程实体对象
     */
    @AfterReturning(value = "execution(* com.*..*.service.*Service.discard(..)) && args(action,..)")
    public void afterDiscard(ProcessDiscardAction action) throws Throwable {
        String _procId = action.getProcessId();
        Assert.isTrue(isNotBlank(_procId), "Process ID cannot be null.");

        Process _proc = processDao.selectByPrimaryKey(_procId);
        if (null == _proc) {
            throw new ProcessNotFoundException(
                "Process cannot be found. ID: [" + _procId + "]."
            );
        }

        String _curStatus = _proc.getStatus();
        if (STATUS_DISCARD.equals(_curStatus)) {
            throw new BadProcessStatusException(
                "Process has already been discard. ID: [" + _procId + "]."
            );
        }

        if (!STATUS_IN_PROGRESS.equals(_curStatus)) {
            throw new BadProcessStatusException(
                "Only the process which is in-progress could discard. ID: ["
                    + _procId + "], status: [" + _curStatus + "]."
            );
        }

        long i = runtimeService.createExecutionQuery()
            .processInstanceId(_proc.getInstanceId())
            .count();
        logger.info("Is current process [{}] running? [{}].",
            _proc.getInstanceId(), i > 0);

        if (i < 1) {
            logger.warn(
                "There is no any process in progress. So ignore discard logic else. Process ID: [{}]",
                _proc.getInstanceId()
            );

            return;
        }

        // 如果用户提供了备注，则记录备注信息
        CommentAction _comAct = action.getComment();
        String _discardReason = null;
        if (null != _comAct) {
            _discardReason = _comAct.getContent();
            // 添加备注
            flowStdService.addComment(null, _proc.getInstanceId(), _comAct);
        }

        try {
            // 删除流程实例
            runtimeService.deleteProcessInstance(
                _proc.getInstanceId(),
                _discardReason
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }
    }

    /**
     * 创建流程处理的环绕方法。
     *
     * @param action <code>ProcessStartAction</code>对象实例
     * @throws Throwable
     */
    @Around(
        value = "execution(* com.*..*.service.*Service.start(..)) && args(action,..)",
        argNames = "jp,action"
    )
    public Object aroundStart(ProceedingJoinPoint jp, ProcessStartAction action) throws Throwable {
        Object _ret;

        try {
            Process _proc = action.getProcess();
            Assert.notNull(_proc, "Process object cannot be null.");

            // 如果流程对象主键没设置，则默认创建草稿
            _saveAsDraft(_proc, action.getDefinitionKey());

            // 回调被拦截的业务方法
            _ret = jp.proceed(jp.getArgs());

            /* 开始启动流程的逻辑 */
            // 从数据库中再次加载流程信息
            _proc = processDao.selectByPrimaryKey(_proc.getUidPk());
            // 检查流程实体的状态是否已启动
            if (!STATUS_DRAFT.equals(_proc.getStatus()) || isNotBlank(_proc.getInstanceId())) {
                throw new BadProcessStatusException(
                    "The process' status isn't draft. So it doesn't start "
                        + "repeat. Please check it. Process id: ["
                        + _proc.getUidPk() + "]."
                );
            }

            // 强制设置流程定义的ID
            _proc.setDefinitionId(
                _getLatestDefinition(
                    action.getDefinitionKey()
                ).getId()
            );

            int k = processDao.setDefinitionId(
                _proc.getUidPk(),
                _proc.getDefinitionId()
            );
            logger.debug("Effect rows updating process definition id. [{}]", k);

            // 为即将启动的流程设置business key
            String _businessKey = null;
            if (null != businessKeyGenerator) {
                _businessKey = businessKeyGenerator.generateKey(_proc);
            }
            logger.info(
                "Business key for process [{}] is: [{}].",
                _proc.getUidPk(), _businessKey
            );

            // 设置流程参数
            Map<String, Object> _variables = new HashMap<>(action.getArgs());
            _variables.put(ARG_PROCESS_ID, _proc.getUidPk());

            ProcessInstance pi
                = runtimeService.startProcessInstanceById(
                _proc.getDefinitionId(),
                _businessKey,
                _variables
            );

            flowStdService.addComment(null,
                pi.getProcessInstanceId(),
                action.getComment()
            );

            Process _procInDB = processDao.selectByPrimaryKey(_proc.getUidPk());
            BeanUtils.copyProperties(_proc, _procInDB);

            if (logger.isInfoEnabled()) {
                logger.info(
                    "The process is started. Process ID: [{}], instance id: [{}], start time is: [{}].",
                    _proc.getUidPk(), _proc.getInstanceId(), format(_proc.getStartTime(), "yyyy-MM-dd HH:mm")
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }

        return _ret;
    }

    /**
     * 任务完成的后置方法
     *
     * @param action <code>TaskApproveAction</code>对象实例
     */
    @AfterReturning(value = "execution(* com.*..*.service.*Service.complete(..)) && args(action,..)")
    public void afterCompleted(TaskApproveAction action) throws Throwable {
        String _taskId = action.getTaskId();

        Assert.isTrue(isNotBlank(_taskId), "Task ID mustn't be null.");

        logger.info("Prepare to complete task. ID: [{}]", _taskId);

        // 获取任务历史记录
        HistoricTaskInstance _task
            = historyService.createHistoricTaskInstanceQuery()
            .taskId(_taskId)
            .singleResult();

        // 任务没找到
        if (null == _task) {
            throw new TaskNotFoundException(
                "Task can't be found. ID: [" + _taskId + "]."
            );
        }

        // 任务已完成
        if (null != _task.getEndTime()) {
            throw new BadTaskStatusException(
                "Task has been finished. ID: [" + _taskId + "]."
            );
        }

        // 添加备注
        flowStdService.addComment(_taskId, null, action.getComment());

        // 设置任务的全局变量
        Map<String, Object> args = action.getArgs();
        if (isNotEmpty(args)) {
            taskService.setVariables(_taskId, args);
        }

        Map<String, Object> localArgs = action.getLocalArgs();
        /*if (isNotEmpty(localArgs)) {
            taskService.setVariablesLocal(_taskId, localArgs);
        }*/

        // 标记任务完成
        taskService.complete(_taskId, localArgs, true);

        if (logger.isInfoEnabled()) {
            logger.info("Task completed. {");
            logger.info("\tTask id: [{}].", _taskId);
            logger.info("\tProcess instance id: [{}].", _task.getProcessInstanceId());
            logger.info("\tProcess definition id: [{}].", _task.getProcessDefinitionId());
            logger.info("\tTask key: [{}].", _task.getTaskDefinitionKey());
            logger.info("\tTask assignee: [{}].", _task.getAssignee());
            logger.info("}.");
        }
    }

    /**
     * 添加备注的方法
     *
     * @param action <code>CommentAddAction</code>对象实例
     */
    @AfterReturning(value = "execution(* com.*..*.service.*Service.addComment(..)) && args(action,..)")
    public void beforeCommentAdd(CommentAddAction action) throws Throwable {
        Assert.isTrue(
            isNotBlank(action.getTaskId()) || isNotBlank(action.getInstanceId()),
            "Both task id and process instance id are null."
        );

        flowStdService.addComment(
            action.getTaskId(),
            action.getInstanceId(),
            action
        );
    }

    /* 查找给定流程定义KEY的最后一个版本的流程定义信息 */
    private ProcessDefinition _getLatestDefinition(String definitionKey) {
        ProcessDefinition processDefinition
            = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(definitionKey)
            .latestVersion()
            .active()
            .singleResult();

        if (processDefinition == null) {
            throw new ActivitiIllegalArgumentException(
                "No process definition was found. [" + definitionKey + "]."
            );
        }

        return processDefinition;
    }

    /* 保存给定的流程信息为草稿状态 */
    private void _saveAsDraft(Process _proc, String definitionKey) throws DataPersistenceException {
        String _procId = _proc.getUidPk();
        logger.debug("Process ID: [{}].", _procId);

        if (isBlank(_procId) || processDao.countByPK(_procId) < 1) {
            Assert.isTrue(isNotBlank(definitionKey),
                "Definition Key cannot be null when save a draft process."
            );

            // 设置process definition id
            _proc.setDefinitionId(
                _getLatestDefinition(definitionKey).getId()
            );

            _proc.setStatus(STATUS_DRAFT);

            if (isBlank(_proc.getCreateBy())) {
                _proc.setCreateBy(getAuthenticatedUserId());
            }

            if (null == _proc.getCreateTime()) {
                _proc.setCreateTime(new Date());
            }

            try {
                int i = processDao.insert(_proc);
                logger.debug("Effect rows for inserting T_BP_PROCESS. [{}]", i);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new DataPersistenceException(e);
            }
        }
    }

    /**
     * 选取任务的后置方法。
     *
     * @param task 任务实体对象
     * @throws Throwable
     */
    /*@AfterReturning("execution(* com.*..*.service.*Service.claim(..)) && args(task,..)")
    public void afterClaim(TaskObject task) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("Prepare to claim this task. Task id: [{}].", task.getTaskId());
        }

        try {
            userTaskService.claimTask(
                task.getTaskId(),
                getAuthenticatedUserId()
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Task has been claimed. Task id: [{}].", task.getTaskId());
        }
    }*/

    /**
     * 释放已选取任务的方法。
     *
     * @param task 任务实体对象
     * @throws Throwable
     */
    /*@AfterReturning("execution(* com.*..*.service.*Service.unclaim(..)) && args(task,..)")
    public void afterUnclaim(TaskObject task) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("Prepare to unclaim this task. Task id: [{}].", task.getTaskId());
        }

        try {
            userTaskService.unclaimTask(task.getTaskId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Task has been unclaimed. Task id: [{}].", task.getTaskId());
        }
    }*/

    /**
     * 添加任务备注的前置方法。
     *
     * @param task     任务实体对象
     * @param behavior 备注行为对象
     * @throws Throwable
     */
    /*@Before(
        value = "execution(* com.*..*.service.*Service.addComment(..)) && args(task,behavior,..)",
        argNames = "task,behavior")
    public void beforeAddComment(TaskObject task, CommentBehavior behavior) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug(
                "Prepare to add a comment. Comment type: ["
                    + behavior.getType() + "], content: ["
                    + behavior.getComment() + "]."
            );
        }

        try {
            userTaskService.createComment(
                task, behavior.getType(),
                behavior.getComment(),
                behavior.getRole(),
                behavior.getAction()
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }*/
}
