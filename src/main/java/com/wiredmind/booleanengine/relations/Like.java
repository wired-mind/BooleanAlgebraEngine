package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

/**
 * Tests whether a property value is like a particular value.
 * <p/>
 * This works easily for both strings and numbers by testing
 * whether the propertyValue contains the provided substring
 * expression. A date (or calendar) property is like another
 * value only when it represents the same date.
 */
public class Like extends AbstractBinaryRelation<Object, String> {

    public final static long serialVersionUID = 1L;

    public Like(Object key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        setPropertyValue(context, key);

        if (propertyContains(val)) {
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
