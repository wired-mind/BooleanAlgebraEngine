package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

/**
 * Tests whether a property is greater than a particular value.
 */
public class Gt extends AbstractBinaryRelation<Object, String> {

    public final static long serialVersionUID = 1L;

    public Gt(Object key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        setPropertyValue(context, key);

        if (comparePropertyTo(val) < 0) {
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
