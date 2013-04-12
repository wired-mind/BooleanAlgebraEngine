package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

import java.math.BigDecimal;

/**
 * Tests whether a property value matches a regular expression rule.
 */
public class Match extends AbstractBinaryRelation<Object, String> {

    public final static long serialVersionUID = 1L;

    public Match(Object key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        setPropertyValue(context, key);

        if (val == null) {
            return PROCESSING_COMPLETE;
        }

        switch (propertyType) {
            case NUMBER:
                String numberStr = (new BigDecimal(propertyValue.toString())).toPlainString();
                if (numberStr.matches(val)) {
                    truthValue = true;
                    return CONTINUE_PROCESSING;
                }
                break;
            case UNKNOWN:
                throw new IllegalArgumentException(key + ", " + propertyType.getDescription());
            default:
                if (propertyValue.toString().matches(val)) {
                    truthValue = true;
                    return CONTINUE_PROCESSING;
                }
        }

        return PROCESSING_COMPLETE;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }
}
