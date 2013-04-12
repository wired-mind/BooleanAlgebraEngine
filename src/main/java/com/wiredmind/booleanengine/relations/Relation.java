package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * A property that assigns a truth value to combinations of n arguments,
 * according to whether or not the relation holds.
 * <p/>
 * <p>A relation extends a {@link Command}, which encapsulates a unit
 * of work to be performed. Its <code>execute()</code> method returns
 * <code>CONTINUE_PROCESSING</code> when the relation holds and
 * <code>PROCESSING_COMPLETE</code> when the relation does not hold. Its
 * truth value is set accordingly. Hence, relations can be assembled into
 * a {@link Chain}, which allows them to either complete the required
 * processing or delegate further processing to the next {@link Command}
 * in the {@link Chain}.</p>
 * <p/>
 * <p>Thus, a {@link Chain} of relations may be regarded as forming a
 * logical AND operation. (The first relation that does not hold effectively
 * short-circuits the operation.)</p>
 * <p/>
 * <p>{@link Relation} implementations typically retrieve information in the
 * {@link Context} instance that is passed as a parameter to the
 * <code>execute()</code> or <code>isTruthValue()</code> methods, using
 * particular keys into the <code>Map</code> that can be acquired via
 * <code>context.get()</code>.
 */
public interface Relation extends Command {

    /**
     * Evaluates the relation and returns its
     * truth value for the given {@link Context}
     * in one step. (Invokes <code>execute()</code>
     * and returns the result of <code>isTruthValue()</code>.)
     *
     * @param context The context to be processed
     * @return The truth value of the relation
     * @throws Exception Indicating abnormal termination
     */
    boolean isTruthValue(Context context) throws Exception;

    /**
     * Returns the truth value of the relation.
     * It is expected that that the relation has
     * already been evaluated via its <code>execute()</code>
     * method.
     *
     * @return The truth value of the relation
     */
    boolean isTruthValue();
}
