package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

import java.io.Serializable;

/**
 * A zero-place relation. There are only two zero-place relations:
 * the one that always holds, and the one that never holds.
 * The {@link True} relation performs no operation
 * and its <code>execute()</code> method always returns
 * <code>CONTINUE_PROCESSING</code>.
 */
public class True implements Relation, Serializable {

    private final static long serialVersionUID = 1L;

    @Override
    public boolean execute(Context context) throws Exception {
        return CONTINUE_PROCESSING;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        return true;
    }

    @Override
    public boolean isTruthValue() {
        return true;
    }
}
