package com.googlecode.easyec.bpmn2.domain;

import com.googlecode.easyec.spirit.domain.DomainModel;

public interface Comment extends DomainModel {

    String getRole();

    void setRole(String role);

    String getAction();

    void setAction(String action);

    boolean isSystem();

    void setSystem(boolean system);
}
