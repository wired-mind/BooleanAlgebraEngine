package com.wiredmind.booleanengine.relations.plugins;

import com.wiredmind.booleanengine.actions.ActionBase;
import org.apache.commons.chain.Context;

import java.io.Serializable;

/**
 * Update the Award amount by the specified amount as a simple value.
 */
public class SimpleUpdate extends ActionBase<String> implements Serializable {

    public final static long serialVersionUID = 1L;

    public SimpleUpdate(String amount) {
        super(amount);
    }

    @Override
    public boolean performAction(Context cntxt) throws Exception {
        if (cntxt != null)
            cntxt.put("award", val);

        return true;
    }
}
