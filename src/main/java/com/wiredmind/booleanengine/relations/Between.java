package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

/**
 * Tests whether a property value is between two distinct values.
 */
public class Between extends AbstractTernaryRelation<Object, String, String> {

    public final static long serialVersionUID = 1L;

    public Between(Object key, String lhs, String rhs) {
        super(key, lhs, rhs);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        setPropertyValue(context, key);

        if (comparePropertyTo(lhs) <= 0 && 0 <= comparePropertyTo(rhs)) {
            truthValue = true;
            return CONTINUE_PROCESSING;
        }

        return PROCESSING_COMPLETE;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }
}
