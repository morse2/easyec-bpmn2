package com.googlecode.easyec.bpmn2.engine.key.generator.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierGenerator;
import com.googlecode.easyec.spirit.dao.id.impl.LongValueHiloIdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class ProcessIdentifierGenerator implements IdentifierGenerator {

    private LongValueHiloIdentifierGenerator _delegate = new LongValueHiloIdentifierGenerator(50);

    @Override
    public boolean accept(Class<?> cls) {
        return String.class.isAssignableFrom(cls);
    }

    @Override
    public Serializable generate(String name, Connection conn) throws SQLException {
        return String.valueOf(_delegate.generateNumber(name, conn));
    }
}
