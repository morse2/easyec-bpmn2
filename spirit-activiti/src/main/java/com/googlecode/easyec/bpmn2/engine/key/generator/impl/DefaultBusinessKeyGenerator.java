package com.googlecode.easyec.bpmn2.engine.key.generator.impl;

import com.googlecode.easyec.bpmn2.domain.Process;
import com.googlecode.easyec.bpmn2.engine.key.generator.BusinessKeyGenerator;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DefaultBusinessKeyGenerator implements BusinessKeyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBusinessKeyGenerator.class);
    private String _formatter;

    public DefaultBusinessKeyGenerator() {
        this(9);
    }

    public DefaultBusinessKeyGenerator(int padding) {
        Assert.isTrue(padding > 0, "Padding should be greater than 0.");
        this._formatter
            = new StringBuffer()
            .append("%1$0")
            .append(padding)
            .append('d')
            .toString();
    }

    @Override
    public String generateKey(Process proc) {
        String _id = proc.getUidPk();

        if (isNotBlank(_id) && isNumeric(_id)) {
            long _l = NumberUtils.toLong(_id, -1);
            if (_l < 0) return null;

            try {
                return String.format(_formatter, _l);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
