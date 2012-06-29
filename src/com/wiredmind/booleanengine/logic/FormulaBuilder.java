package com.wiredmind.booleanengine.logic;

import de.odysseus.el.tree.Node;
import de.odysseus.el.tree.impl.ast.AstBinary;
import de.odysseus.el.tree.impl.ast.AstChoice;
import de.odysseus.el.tree.impl.ast.AstEval;
import de.odysseus.el.tree.impl.ast.AstIdentifier;
import de.odysseus.el.tree.impl.ast.AstNested;
import de.odysseus.el.tree.impl.ast.AstUnary;
import java.util.Stack;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;

/**
 * A {@link Formula} builder.
 * 
 * 
 */
public class FormulaBuilder {

    public enum FORMAT {

        NORMAL, SUBSCRIPT;
    };
    private boolean hasPredicate = false;
    private Formula formula; // TODO: Add to thread-safe cache
    private Formula predicate;
    private Formula consequent;
    private Formula alternative;
    private BidiMap variableMap = new TreeBidiMap();
    private int mapKey;
    private FORMAT format = FORMAT.NORMAL;

    /**
     * Builds a new {@link Formula} from
     * an AST {@link de.odysseus.el.tree.Node}.
     * @param treeNode an AST {@link de.odysseus.el.tree.Node}
     */
    public FormulaBuilder(Node treeNode) {
        formula = buildFormula(treeNode);
    }

    /**
     * Builds a new {@link Formula} from an AST
     * {@link de.odysseus.el.tree.Node}. When <tt>FORMAT.SUBSCRIPT</tt>
     * is specified, then all of the variables in the AST, e.g. {a, b, c},
     * are expressed using a single variable notation with integral
     * subscripts, e.g. {x1, x2, x3} except that only the subscripts
     * themselves are preserved in the resulting formula; 1, 2, 3.
     * The original variables are preserved in a map, 1->a, 2->b, 3->c,
     * which may be obtained from the FormulaBuilder’s variableMap property.
     * @param treeNode an AST {@link de.odysseus.el.tree.Node}
     * @param format formula FORMAT to produce
     */
    public FormulaBuilder(Node treeNode, FORMAT format) {
        this.format = format;
        formula = buildFormula(treeNode);
        if (format == FORMAT.SUBSCRIPT) {
            formula.setVariableMap(variableMap);
        }
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
            if (format == FORMAT.SUBSCRIPT) {
                if (!variableMap.containsValue(literal)) {
                    mapKey++;
                    variableMap.put(mapKey, literal);
                }
                f = new Var(variableMap.getKey(literal).toString());
            } else {
                f = new Var(literal);
            }
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

    public Formula getFormula() {
        return formula;
    }

    public Formula getPredicate() {
        return predicate;
    }

    public Formula getConsequent() {
        return consequent;
    }

    public Formula getAlternative() {
        return alternative;
    }
}
