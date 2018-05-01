package com.googlecode.easyec.bpmn2.service.impl;

import com.googlecode.easyec.bpmn2.BadProcessStatusException;
import com.googlecode.easyec.bpmn2.engine.action.*;
import com.googlecode.easyec.bpmn2.service.FlowOperateService;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FlowOperateServiceAdapter implements FlowOperateService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void save(ProcessDraftAction action) throws DataPersistenceException, BadProcessStatusException {
        logger.info("['{}']'s method 'save'.", getClass());
    }

    @Override
    public void delete(ProcessDraftAction action) throws DataPersistenceException, BadProcessStatusException {
        logger.info("['{}']'s method 'delete'.", getClass());
    }

    @Override
    public void start(ProcessStartAction action) throws DataPersistenceException {
        logger.info("['{}']'s method 'start'.", getClass());
    }

    @Override
    public void discard(ProcessDiscardAction action) throws DataPersistenceException, BadProcessStatusException {
        logger.info("['{}']'s method 'discard'.", getClass());
    }

    @Override
    public void complete(TaskApproveAction action) throws DataPersistenceException {
        logger.info("['{}']'s method 'complete'.", getClass());
    }

    @Override
    public void addComment(CommentAddAction action) throws DataPersistenceException {
        logger.info("['{}']'s method 'addComment'.", getClass());
    }
}
