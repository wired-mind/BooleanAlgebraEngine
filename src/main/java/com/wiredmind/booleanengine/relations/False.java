package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;

import java.io.Serializable;

/**
 * A zero-place relation. There are only two zero-place relations:
 * the one that always holds, and the one that never holds.
 * The {@link False} relation performs no operation
 * and its <code>execute()</code> method always returns
 * <code>PROCESSING_COMPLETE</code>.
 */
public class False implements Relation, Serializable {

    private final static long serialVersionUID = 1L;

    @Override
    public boolean execute(Context context) throws Exception {
        return PROCESSING_COMPLETE;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        return false;
    }

    @Override
    public boolean isTruthValue() {
        return false;
    }
}
