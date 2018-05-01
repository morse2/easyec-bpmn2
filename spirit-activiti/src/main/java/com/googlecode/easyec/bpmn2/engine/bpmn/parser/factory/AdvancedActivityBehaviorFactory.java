package com.googlecode.easyec.bpmn2.engine.bpmn.parser.factory;

import com.googlecode.easyec.bpmn2.engine.bpmn.behavior.UserTaskExclusiveGatewayActivityBehavior;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

public class AdvancedActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Override
    public ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(ExclusiveGateway exclusiveGateway) {
        return new UserTaskExclusiveGatewayActivityBehavior();
    }
}
