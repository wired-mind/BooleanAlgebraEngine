package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.relations.Relation;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.StringUtils;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default {@link ValidatorAlgebra} implementation.
 */
public class DefaultValidatorAlgebra implements ValidatorAlgebra {

    private final static Logger LOGGER = Logger.getLogger(DefaultValidatorAlgebra.class.getName());
    private final static ExpressionFactory FACTORY = new de.odysseus.el.ExpressionFactoryImpl();
    private Context commandContext;
    private final String expression;
    private final de.odysseus.el.util.SimpleContext expressionContext = new de.odysseus.el.util.SimpleContext();
    private boolean truthValue;
    private Map<String, Boolean> valuationMap;
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

    class Atom {

        private String name;
        private Relation relation;
        private boolean evaluated = false;
        private boolean value;

        public Atom(Rule rule) {
            this.name = rule.getRuleName();
            this.relation = EvaluatorFactory.create(
                    rule.getRelation(),
                    rule.getArguments());
        }

        public boolean isValue() {
            if (!evaluated) {
                try {
                    LOGGER.finest("atomEvaluation"); // used for unit testing
                    this.value = this.relation.isTruthValue(commandContext);
                    valuationMap.put(this.name, this.value);
                } catch (Exception ex) {
                    LOGGER.log(Level.FINE, ex.getMessage());
                } finally {
                    this.evaluated = true;
                }
            }
            return this.value;
        }
    }

    @Override
    public boolean execute(Context context) throws Exception {
        this.commandContext = context;
        this.valuationMap = new TreeMap<String, Boolean>();

        if (StringUtils.isNotEmpty(expression)) {
            valueExpression = FACTORY.createValueExpression(expressionContext, "${" + this.expression + "}", Boolean.class);
            truthValue = (Boolean) valueExpression.getValue(expressionContext);
            if (truthValue == true) {
                return CONTINUE_PROCESSING;
            }
        }
        return PROCESSING_COMPLETE;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }

    @Override
    public boolean isTruthValue() throws IllegalStateException {
        if (valueExpression == null) {
            throw new IllegalStateException(expression);
        }
        return truthValue;
    }

    @Override
    public Map<String, Boolean> getValuationMap() {
        return new TreeMap<String, Boolean>(valuationMap);
    }
}
