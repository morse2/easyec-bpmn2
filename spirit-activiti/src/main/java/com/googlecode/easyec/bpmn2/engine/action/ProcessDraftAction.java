package com.googlecode.easyec.bpmn2.engine.action;

import com.googlecode.easyec.bpmn2.domain.Process;

public interface ProcessDraftAction extends ProcessAction {

    Process getProcess();

    String getDefinitionKey();
}
