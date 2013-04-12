package com.wiredmind.booleanengine.core;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.Map;

/**
 * A qualification algebra extends a {@link Command}, which encapsulates a unit
 * of work to be performed. Its <code>execute()</code> method returns
 * <code>CONTINUE_PROCESSING</code> when the expression holds and
 * <code>PROCESSING_COMPLETE</code> when the expression does not hold. Its
 * truth value is set accordingly.
 * <p/>
 * <p>{@link ValidatorAlgebra} implementations typically retrieve information
 * in the {@link Context} instance that is passed as a parameter to the
 * <code>execute()</code> or <code>isTruthValue()</code> methods, using
 * particular keys into the <code>Map</code> that can be acquired via
 * <code>context.get()</code>.
 */
public interface ValidatorAlgebra extends Command {

    /**
     * Returns the algebra expression.
     *
     * @return a string representing the expression
     */
    String getExpression();

    /**
     * Evaluates the expression and returns its
     * truth value for the given {@link Context}
     * in one step. (Invokes <code>execute()</code>
     * and returns the result of <code>isTruthValue()</code>.)
     *
     * @param context The context to be processed
     * @return The truth value of the expression
     * @throws Exception Indicating abnormal termination
     */
    boolean isTruthValue(Context context) throws Exception;

    /**
     * Returns the truth value of the expression.
     * It is expected that that the expression has
     * already been evaluated via its <code>execute()</code>
     * method.
     *
     * @return The truth value of the expression
     * @throws IllegalStateException When <code>execute()</code>
     *                               was not invoked first
     */
    boolean isTruthValue() throws IllegalStateException;

    /**
     * Returns a map with the assignment of truth values
     * to each propositional variable in a qualification algebra.
     * It is expected that that the expression has
     * already been evaluated via its <code>execute()</code>
     * method.
     *
     * @return the assignment of truth values to variables in a map
     */
    Map<String, Boolean> getValuationMap();
}
