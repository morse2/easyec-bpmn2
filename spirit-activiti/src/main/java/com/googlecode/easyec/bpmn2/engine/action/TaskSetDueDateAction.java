package com.googlecode.easyec.bpmn2.engine.action;

import java.util.Date;

public interface TaskSetDueDateAction extends TaskAction {

    Date getOld();

    Date getNew();
}
