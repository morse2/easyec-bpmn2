package com.googlecode.easyec.bpmn2.service;

import com.googlecode.easyec.bpmn2.BadProcessStatusException;
import com.googlecode.easyec.bpmn2.engine.action.*;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;

/**
 * 表示流程的基本操作的业务接口类
 *
 * @author JunJie
 */
public interface FlowOperateService {

    /**
     * 暂存流程数据为草稿状态
     *
     * @param action <code>ProcessDraftAction</code>对象实例
     * @throws DataPersistenceException  操作数据库时发生的异常信息
     * @throws BadProcessStatusException 当流程状态不是进行中的时候，调用此方法将抛出该异常
     */
    void save(ProcessDraftAction action) throws DataPersistenceException, BadProcessStatusException;

    /**
     * 删除草稿状态的准流程数据
     *
     * @param action <code>ProcessDraftAction</code>对象实例
     * @throws DataPersistenceException  操作数据库时发生的异常信息
     * @throws BadProcessStatusException 当流程状态不是进行中的时候，调用此方法将抛出该异常
     */
    void delete(ProcessDraftAction action) throws DataPersistenceException, BadProcessStatusException;

    /**
     * 开始一个新流程
     *
     * @param action <code>ProcessStartAction</code>对象实例
     * @throws DataPersistenceException 操作数据库时发生的异常信息
     */
    void start(ProcessStartAction action) throws DataPersistenceException;

    /**
     * 丢弃（撤销）一个正在进行中的流程
     *
     * @param action <code>ProcessDiscardAction</code>对象实例
     * @throws DataPersistenceException  操作数据库时发生的异常信息
     * @throws BadProcessStatusException 当流程状态不是进行中的时候，调用此方法将抛出该异常
     */
    void discard(ProcessDiscardAction action) throws DataPersistenceException, BadProcessStatusException;

    /**
     * 完成一个用户任务
     *
     * @param action <code>TaskApproveAction</code>对象实例
     * @throws DataPersistenceException 操作数据库时发生的异常信息
     */
    void complete(TaskApproveAction action) throws DataPersistenceException;

    /**
     * 添加一个备注
     *
     * @param action <code>CommentAddAction</code>对象实例
     * @throws DataPersistenceException 操作数据库时发生的异常信息
     */
    void addComment(CommentAddAction action) throws DataPersistenceException;
}
