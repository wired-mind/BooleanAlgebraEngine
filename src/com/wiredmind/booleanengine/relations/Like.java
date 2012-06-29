package com.wiredmind.booleanengine.core.relations;

import java.math.BigDecimal;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a property value is like a particular value. 
 *
 * This works for both strings and numbers by testing
 * whether the propertyValue 'contains' the provided
 * substring expression.
 *
 * 
 */
public class Like extends AbstractBinaryRelation<String, String> {

    public final static long serialVersionUID = 1L;

    public Like(String key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        switch (propertyType) {
            case DATE:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
                return PROCESSING_COMPLETE;
            case NUMBER:
                String numberStr = (new BigDecimal(propertyValue.toString())).toPlainString();
                if (StringUtils.contains(numberStr, val)) {
                    return CONTINUE_PROCESSING;
                }
                return PROCESSING_COMPLETE;
            case STRING:
                String s = propertyValue.toString();
                if (StringUtils.contains(s, val)) {
                    return CONTINUE_PROCESSING;
                }
                return PROCESSING_COMPLETE;
            default:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
        }
        return PROCESSING_COMPLETE;
    }
}
