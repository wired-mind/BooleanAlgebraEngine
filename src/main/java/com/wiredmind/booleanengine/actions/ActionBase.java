package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.core.RelationFactory;
import com.wiredmind.booleanengine.relations.AbstractUnaryRelation;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Base class for family of Action commands.
 *
 * Custom Action commands should extend this
 * class rather than implement the Relation
 * interface themselves.
 */
public abstract class ActionBase<T> extends AbstractUnaryRelation<T> implements Action {

    Command applicabilityMember;
    boolean isApplicable = false;

    public ActionBase(T val) {
        super(val);
    }

    /**
     * Specify the applicability of this command from
     * the arguments provided.
     *
     * @param relation  The name of the relation, e.g. 'Between', 'EQ', 'GE', etc.
     * @param arguments The individuals or values used for the validation
     * @return A reference to this instance
     */
    @Override
    public Action When(String relation, String arguments) {
        this.applicabilityMember = RelationFactory.create(relation, arguments);
        return this;
    }

    /**
     * Specify the applicability of this command using
     * another Command object. The Command may also be a Chain.
     * The applicability member will be executed (first) when this
     * command is executed. If the condition specified in the Command is
     * applicable then this command will continue processing, otherwise
     * it will delegate remaining processing to the next Command in a Chain
     * containing this Command by returning CONTINUE_PROCESSING.
     *
     * @param command - Any Command or Chain of commands
     * @return this instance
     */
    @Override
    public Action When(Command command) {
        this.applicabilityMember = command;
        return this;
    }

    /**
     * Subclasses should override this method but also
     * invoke it to initialize the isApplicable member.
     *
     * @return CONTINUE_PROCESSING unless an exception is thrown
     * @throws Exception
     */
    @Override
    public boolean execute(Context cntxt) throws Exception {
        if (null != applicabilityMember) {
            isApplicable = !applicabilityMember.execute(cntxt);
        }

        if (null == applicabilityMember || isApplicable) {
            truthValue = performAction(cntxt);
        }
        return CONTINUE_PROCESSING;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }
}
