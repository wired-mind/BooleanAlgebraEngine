package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.core.actions.CompleteProcessing;
import com.wiredmind.booleanengine.core.actions.ContinueProcessing;
import com.wiredmind.booleanengine.core.actions.PercentAwardUpdate;
import com.wiredmind.booleanengine.core.actions.SimpleAwardUpdate;
import com.wiredmind.booleanengine.core.relations.Between;
import com.wiredmind.booleanengine.core.relations.Eq;
import com.wiredmind.booleanengine.core.relations.Ge;
import com.wiredmind.booleanengine.core.relations.Gt;
import com.wiredmind.booleanengine.core.relations.In;
import com.wiredmind.booleanengine.core.relations.Le;
import com.wiredmind.booleanengine.core.relations.Like;
import com.wiredmind.booleanengine.core.relations.Lt;
import com.wiredmind.booleanengine.core.relations.Match;
import com.wiredmind.booleanengine.core.relations.SumOfPurchasesGE;
import org.apache.commons.chain.Command;
import org.apache.commons.lang.StringUtils;

/**
 * Creates evaluator command objects.
 *
 * 
 */
public class EvaluatorFactory {

    /**
     * Creates command objects that represent finitary relations
     * between k individuals.
     *
     * @param relation The name of the relation, e.g. GT, GE, etc.<p/>
     * @param individuals A space-separated string of individuals: e.g. "Amount 40.00"
     * <p>(By convention, the first individual is a variable.)</p>
     * @return A Command object
     */
    public static Command create(String relation, String individuals) {
        String name = relation.toLowerCase().trim();

        if (StringUtils.isEmpty(individuals)) {
            // zero-place relation
            return createEvaluator(name);
        } else {
            String[] args = StringUtils.strip(individuals).split("\\s+", 2);
            if (args.length == 1) {
                // an action
                return createEvaluator(name, args[0]);
            } else if (name.equals("between")) {
                // ternary relation
                String[] newArgs = args[1].split("\\s+", 2);
                return createEvaluator(name, args[0], newArgs[0], newArgs[1]);
            } else {
                // binary relation
                return createEvaluator(name, args[0], args[1]);
            }
        }
    }

    private static Command createEvaluator(String name) {
        // Zero-place relations - since there is only one 0-tuple,
        // the empty tuple ( ), there are only two zero-place relations:
        if (name.equals("true")) {
            return new ContinueProcessing();
        }
        if (name.equals("false")) {
            return new CompleteProcessing();
        }
        return new CompleteProcessing(); // default
    }

    private static Command createEvaluator(String name, String u) {
        // Actions:
        if (name.equals("award")) {
            return new SimpleAwardUpdate(u);
        }
        if (name.equals("percentaward")) {
            return new PercentAwardUpdate(u);
        }
        return new CompleteProcessing(); // default
    }

    private static Command createEvaluator(String name, String u, String v) {
        // Binary relations:
        if (name.equals("eq")) {
            return new Eq(u, v);
        } else if (name.equals("ge")) {
            return new Ge(u, v);
        } else if (name.equals("gt")) {
            return new Gt(u, v);
        } else if (name.equals("in")) {
            return new In(u, v);
        } else if (name.equals("le")) {
            return new Le(u, v);
        } else if (name.equals("lt")) {
            return new Lt(u, v);
        } else if (name.equals("like")) {
            return new Like(u, v);
        } else if (name.equals("match")) {
            return new Match(u, v);
        } else if (name.equals("sopge")) {
            return new SumOfPurchasesGE(u, v);
        }
        return new CompleteProcessing();
    }

    private static Command createEvaluator(String name, String u, String v, String w) {
        // Ternary relations:
        if (name.equals("between")) {
            return new Between(u, v, w);
        }
        return new CompleteProcessing();
    }
}
