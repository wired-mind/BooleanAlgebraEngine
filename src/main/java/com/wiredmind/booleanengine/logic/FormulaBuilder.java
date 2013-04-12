package com.wiredmind.booleanengine.logic;

import de.odysseus.el.tree.Node;
import de.odysseus.el.tree.Tree;
import de.odysseus.el.tree.TreeStore;
import de.odysseus.el.tree.impl.Builder;
import de.odysseus.el.tree.impl.Cache;
import de.odysseus.el.tree.impl.ast.*;
import org.apache.commons.lang.StringUtils;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import java.util.Stack;

/**
 * A builder that creates a {@link Formula}
 * from a string expression.
 */
public class FormulaBuilder {

    private final static Cache ASTCACHE = new Cache(1000); // default TreeCache implementation
    private boolean hasPredicate = false;
    private Formula formula;
    private Formula predicate;
    private Formula consequent;
    private Formula alternative;
    private final String expression;

    /**
     * Creates a FormulaBuilder from an expression.
     *
     * @param expression a string expression, e.g., "(a and b) or (c and d)"
     */
    public FormulaBuilder(String expression) {
        this.expression = expression;
    }

    /**
     * Builds a new {@link Formula} composed of a set of models
     * that satisfy that formula. If the expression passed to the
     * constructor is null or empty then an empty formula is returned.
     *
     * @return a new {@link Formula} composed of a set of models that satisfy that formula.
     * @throws BadExpressionException
     * @throws ContradictionException
     * @throws TimeoutException
     */
    public Formula build() throws BadExpressionException, ContradictionException, TimeoutException {
        formula = new Formula();

        if (StringUtils.isEmpty(expression)) {
            return formula;
        }

        try {
            // Get the expression's AST
            TreeStore store = new TreeStore(new Builder(), ASTCACHE);
            Tree tree = store.get("${" + expression + "}"); // e.g. "(a and b) or (c and d)"

            // Use the AST to build the Boolean Formula
            formula = buildFormula(tree.getRoot());
        } catch (Exception ex) {
            throw new BadExpressionException(ex);
        }
        return Formula.newInstanceforSAT(formula);
    }

    private Formula buildFormula(Node node) {
        return buildFormula(node, new Stack<Node>());
    }

    private Formula buildFormula(Node node, Stack<Node> predecessors) {
        Formula f = null;

        predecessors.push(node);

        if (node instanceof AstEval) {
            f = buildFormula(node.getChild(0), predecessors);
        } else if (node instanceof AstUnary) {
            AstUnary.Operator op = ((AstUnary) node).getOperator();
            if (op == AstUnary.NEG) {
                f = new NotFormula(buildFormula(node.getChild(0), predecessors));
            } else if (op == AstUnary.NOT) {
                f = new NotFormula(buildFormula(node.getChild(0), predecessors));
            } else {
                throw new IllegalArgumentException(op.toString());
            }
        } else if (node instanceof AstBinary) {
            AstBinary.Operator op = ((AstBinary) node).getOperator();
            if (op == AstBinary.AND) {
                f = new AndFormula(
                        buildFormula(node.getChild(0), predecessors),
                        buildFormula(node.getChild(1), predecessors));
            } else if (op == AstBinary.OR) {
                f = new OrFormula(
                        buildFormula(node.getChild(0), predecessors),
                        buildFormula(node.getChild(1), predecessors));
            } else {
                throw new IllegalArgumentException(op.toString());
            }
        } else if (node instanceof AstChoice) {
            if (hasPredicate) {
                throw new IllegalArgumentException("Expression cannot have nested predicates");
            }
            f = buildFormula(node.getChild(0), predecessors);
            predicate = f;
            hasPredicate = true;
            consequent = buildFormula(node.getChild(1), predecessors);
            alternative = buildFormula(node.getChild(2), predecessors);
        } else if (node instanceof AstIdentifier) {
            String literal = node.toString();
            f = new Var(literal);
        } else if (node instanceof AstNested) {
            f = buildFormula(node.getChild(0), predecessors);
        } else {
            throw new IllegalArgumentException(node.getClass().getSimpleName());
        }
        predecessors.pop();

        return f;
    }

    public boolean hasPredicate() {
        return hasPredicate;
    }

    public String getExpression() {
        return expression;
    }

    public Formula getPredicate() {
        return new Formula(predicate);
    }

    public Formula getConsequent() {
        return new Formula(consequent);
    }

    public Formula getAlternative() {
        return new Formula(alternative);
    }
}
