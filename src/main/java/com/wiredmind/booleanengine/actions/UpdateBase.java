package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.core.EvaluatorFactory;
import com.wiredmind.booleanengine.relations.Relation;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Base class for family of "Update" commands.
 * May be regarded as a one-place (unary) relation
 * since it can be viewed as an individual having
 * the property, true or false, <i>after</i> some
 * action or unit of work is performed.
 */
public abstract class UpdateBase implements Relation, Serializable {

    public UpdateBase(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public UpdateBase(String amount, String description) {
        this.amount = new BigDecimal(amount);
        this.description = description;
    }

    protected BigDecimal amount;
    protected String description;
    protected boolean truthValue;

    /**
     * Specify the applicability of this command from
     * the arguments provided.
     *
     * @param relation  The name of the relation, e.g. 'Between', 'EQ', 'GE', etc.
     * @param arguments The individuals or values used for the validation
     * @return A reference to this instance
     */
    public UpdateBase When(String relation, String arguments) {
        this.applicabilityMember = EvaluatorFactory.create(relation, arguments);
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
    public UpdateBase When(Command command) {
        this.applicabilityMember = command;
        return this;
    }

    protected Command applicabilityMember;
    protected boolean isApplicable = false;

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
        return CONTINUE_PROCESSING;
    }

    @Override
    public boolean isTruthValue() {
        return truthValue;
    }
}
