package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.actions.PercentUpdate;
import com.wiredmind.booleanengine.actions.PercentUpdate;
import com.wiredmind.booleanengine.actions.SimpleUpdate;
import com.wiredmind.booleanengine.actions.SimpleUpdate;
import com.wiredmind.booleanengine.relations.*;
import org.apache.commons.lang.StringUtils;

/**
 * Creates evaluator command objects.
 */
public class EvaluatorFactory {

    /**
     * Creates command objects that represent finitary relations
     * between k individuals.
     *
     * @param relation  The name of the relation, e.g. GT, GE, etc.<p/>
     * @param arguments A space-separated string of arguments: e.g. "Amount 40.00"
     *                  <p>(By convention, the first argument is a variable name. If it is
     *                  preceeded by an aggregate function, as in sum:Amount, then the named
     *                  function will be applied to the variable.)</p>
     * @return A Relation
     */
    public static Relation create(String relation, String arguments) {
        String name = relation.toLowerCase().trim();

        if (StringUtils.isEmpty(arguments)) { // zero-place relation
            return createZeroPlaceRelation(name);
        }

        String[] args = StringUtils.strip(arguments).split("\\s+", 2);
        if (args.length == 1) { // an action (one-place or unary relation)
            return createUnaryRelation(name, args[0]);
        }

        if (name.equals("between")) { // ternary relation
            String[] newArgs = args[1].split("\\s+", 2);
            return createTernaryRelation(name, args[0], newArgs[0], newArgs[1]);
        }

        return createBinaryRelation(name, args[0], args[1]); // binary relation
    }

    private static Relation createZeroPlaceRelation(String name) {
        // Zero-place relations - since there is only one 0-tuple,
        // the empty tuple ( ), there are only two zero-place relations:
        if (name.equals("true")) {
            return new True();
        }
        if (name.equals("false")) {
            return new False();
        }
        return new False(); // default
    }

    private static Relation createUnaryRelation(String name, String val) {
        // Actions (Regarded as one-place or unary relations):
        if (name.equals("award")) {
            return new SimpleUpdate(val);
        }
        if (name.equals("percentaward")) {
            return new PercentUpdate(val);
        }
        return new False(); // default
    }

    private static Relation createBinaryRelation(String name, String key, String val) {
        // Binary relations:
        if (name.equals("eq")) {
            return new Eq(key, val);
        } else if (name.equals("ge")) {
            return new Ge(key, val);
        } else if (name.equals("gt")) {
            return new Gt(key, val);
        } else if (name.equals("in")) {
            return new In(key, val);
        } else if (name.equals("le")) {
            return new Le(key, val);
        } else if (name.equals("lt")) {
            return new Lt(key, val);
        } else if (name.equals("like")) {
            return new Like(key, val);
        } else if (name.equals("match")) {
            return new Match(key, val);
        }
        return new False();
    }

    private static Relation createTernaryRelation(String name, String key, String lhs, String rhs) {
        // Ternary relations:
        if (name.equals("between")) {
            return new Between(key, lhs, rhs);
        }
        return new False();
    }
}
