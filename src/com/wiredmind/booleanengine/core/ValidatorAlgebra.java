package com.wiredmind.booleanengine.core;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * A validator algebra.
 * 
 * 
 */
public interface ValidatorAlgebra extends Command {

    /**
     * Evaluates the expression and returns its
     * truth value for the given context in one step.
     * @param context The context to be processed
     * @return The truth value
     * @throws Exception Indicating abnormal termination
     */
    boolean isTruthValue(Context context) throws Exception;

    /**
     * Returns the truth value of the expression.
     * It is expected that that the expression has
     * already been evaluated via {@link execute(context)}.
     * @return The truth value
     * @throws IllegalStateException When {@link execute(context)}
     * was not invoked first
     */
    boolean isTruthValue() throws IllegalStateException;
}
