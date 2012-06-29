package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.domain.Rule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Default {@link com.wiredmind.booleanengine.core.ValidatorAlgebra} implementation.
 *
 * 
 */
public class DefaultValidatorAlgebra extends AbstractValidatorAlgebra {

    private final static Logger LOGGER = Logger.getLogger(DefaultValidatorAlgebra.class.getName());
    private final static ExpressionFactory FACTORY = new de.odysseus.el.ExpressionFactoryImpl();
    private Context commandContext;
    private String expression;
    private de.odysseus.el.util.SimpleContext expressionContext = new de.odysseus.el.util.SimpleContext();
    private boolean truthValue;
    private ValueExpression valueExpression;

    public DefaultValidatorAlgebra(String expression, List<Rule> rules) {
        this.expression = expression;
        for (int i = 0; i < rules.size(); i++) {
            Atom atom = new Atom(rules.get(i));
            expressionContext.setVariable("s" + i, FACTORY.createValueExpression(atom, Atom.class));
            expressionContext.setVariable(rules.get(i).getRuleName(), FACTORY.createValueExpression(expressionContext, "#{s" + i + ".value}", Boolean.class));
        }
    }

    public DefaultValidatorAlgebra(String expression, Collection<Rule> rules) {
        this(expression, new ArrayList<Rule>(rules));
    }

    // <editor-fold defaultstate="collapsed" desc="Inner class - Atom (atomic formula)">
    class Atom {

        private Command relation;
        private boolean evaluated = false;
        private boolean value;

        public Atom(Rule rule) {
            this.relation = EvaluatorFactory.create(
                    rule.getRelation(),
                    rule.getArguments());
        }

        public boolean isValue() {
            if (!evaluated) {
                try {
                    LOGGER.finest("atomEvaluation"); // used for unit testing

                    if (this.relation.execute(commandContext) == Command.CONTINUE_PROCESSING) {
                        this.value = true;
                    } else {
                        this.value = false;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.FINE, ex.getMessage());
                } finally {
                    this.evaluated = true;
                }
            }
            return this.value;
        }
    }// </editor-fold>

    @Override
    public boolean execute(Context context) throws Exception {
        this.commandContext = context;

        valueExpression = FACTORY.createValueExpression(expressionContext, "${" + this.expression + "}", Boolean.class);

        truthValue = (Boolean) valueExpression.getValue(expressionContext);
        return DefaultValidatorAlgebra.CONTINUE_PROCESSING;
    }

    /**
     * Evaluates the expression and returns its
     * truth value for the given context in one step.
     * @param context The context to be processed
     * @return The truth value
     * @throws Exception Indicating abnormal termination
     */
    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }

    /**
     * Returns the truth value of the expression.
     * It is expected that that the expression has
     * already been evaluated via {@link execute(context)}.
     * @return The truth value
     * @throws IllegalStateException When {@link execute(context)}
     * was not invoked first
     */
    @Override
    public boolean isTruthValue() throws IllegalStateException {
        if (valueExpression == null) {
            throw new IllegalStateException(expression);
        }
        return truthValue;
    }
}
