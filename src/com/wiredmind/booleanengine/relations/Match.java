package com.wiredmind.booleanengine.core.relations;

import org.apache.commons.chain.Context;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a property value matches a regular expression rule.
 * 
 * 
 */
public class Match extends AbstractBinaryRelation<String, String> {

    public final static long serialVersionUID = 1L;

    public Match(String key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        switch (propertyType) {
            case STRING:
                if (propertyValue.toString().matches(val)) {
                    return CONTINUE_PROCESSING;
                }
                break;
            default:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
        }
        return PROCESSING_COMPLETE;
    }
}
