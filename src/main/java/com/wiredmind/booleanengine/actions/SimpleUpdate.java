package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.actions.UpdateBase;
import org.apache.commons.chain.Context;

/**
 * Update the Award amount by the specified amount as a simple value.
 */
public class SimpleUpdate extends UpdateBase<Object, String> {

    public final static long serialVersionUID = 1L;

    public SimpleUpdate(Object key, String amount) {
        super(key, amount);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        setPropertyValue(cntxt, key);

        if (null == applicabilityMember || isApplicable) {
            cntxt.put(key, val);
        }
        truthValue = true;
        return CONTINUE_PROCESSING;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }
}
