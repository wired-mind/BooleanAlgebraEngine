package com.wiredmind.booleanengine.logic;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.BidiMap;

/**
 * A Boolean formula. Supports conjunction (and), alternation (or)
 * and denial (not). Note that material implication (a -> b) is
 * logically equivalent to (~a or b).
 * 
 * 
 */
public class Formula {

    public enum BOP {

        LEAF, AND, OR, NOT
    };
    protected BOP op;
    protected Formula left;
    protected Formula right;
    protected String lit;
    private List<Clause> clauses = new ArrayList<Clause>(); // cnf clauses
    private Formula conjunctiveNormalForm = null;
    private BidiMap variableMap;

    protected Formula() {
    }

    protected Formula(Formula other) {
        this.op = other.op;
        this.left = other.left == null ? null : new Formula(other.left);
        this.right = other.right == null ? null : new Formula(other.right);
        this.lit = other.lit == null ? null : new StringBuilder(other.lit).toString();
    }

    // <editor-fold defaultstate="collapsed" desc="A simple façade for constructing instances, e.g. a.and((b).or(c))">
    public Formula and(Formula formula) {
        return new AndFormula(this, formula);
    }

    public Formula or(Formula formula) {
        return new OrFormula(this, formula);
    }

    public Formula not() {
        return new NotFormula(this);
    }// </editor-fold>

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p><b>Does not imply logical equivalence when comparing two
     * Formulas</b> but only tests whether the formulas are identical.
     * In other words, (Formula a).equals(Formula b) implies
     * (Formula a).toString().equals((Formula b).toString())</p>
     * 
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Formula other = (Formula) obj;
        if (this.op != other.op && (this.op == null || !this.op.equals(other.op))) {
            return false;
        }
        if (this.left != other.left && (this.left == null || !this.left.equals(other.left))) {
            return false;
        }
        if (this.right != other.right && (this.right == null || !this.right.equals(other.right))) {
            return false;
        }
        if ((this.lit == null) ? (other.lit != null) : !this.lit.equals(other.lit)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.op != null ? this.op.hashCode() : 0);
        hash = 13 * hash + (this.left != null ? this.left.hashCode() : 0);
        hash = 13 * hash + (this.right != null ? this.right.hashCode() : 0);
        hash = 13 * hash + (this.lit != null ? this.lit.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String strBoolExpr = "";
        if (op == BOP.LEAF) {
            strBoolExpr += lit;
        } else if (op == BOP.NOT) {
            if (left.op == BOP.LEAF || left.op == BOP.NOT) {
                strBoolExpr += "!" + left.toString();
            } else {
                strBoolExpr += "!(" + left.toString() + ")";
            }
        } else {
            if (left != null) {
                if (left.isLeaf() || (left.getOp() == op) || (left.getOp() == BOP.NOT)) {
                    strBoolExpr += left.toString();
                } else {
                    strBoolExpr += "(" + left.toString() + ")";
                }
            }
            switch (op) {
                case AND:
                    strBoolExpr += " and ";
                    break;
                case OR:
                    strBoolExpr += " or ";
                    break;
                default:
                    // error status
                    strBoolExpr += " ? ";
                    break;
            }
            if (right != null) {
                if (right.isLeaf() || (right.getOp() == op) || (right.getOp() == BOP.NOT)) {
                    strBoolExpr += right.toString();
                } else {
                    strBoolExpr += "(" + right.toString() + ")";
                }
            }
        }
        return strBoolExpr;
    }

    public Formula getLeft() {
        return left;
    }

    public BOP getOp() {
        return op;
    }

    public Formula getRight() {
        return right;
    }

    Boolean isLeaf() {
        return (op == BOP.LEAF);
    }

    Boolean isAtomic() {
        return (isLeaf() || (op == BOP.NOT && left.isLeaf()));
    }

    /**
     * Return the clauses of the conjunctive
     * normal form (CNF) of this Formula.
     * 
     * @return A new list of clauses
     */
    public List<Clause> toCNFClauses() {
        if (clauses.size() == 0) {
            getClauses(clauses, this.toCNF());
        }
        return clauses;
    }

    /**
     * Return a new Formula in conjunctive normal form (CNF)
     * that is logically equivalent to this formula.
     *
     * @return A new Formula in CNF
     */
    public Formula toCNF() {
        if (conjunctiveNormalForm == null) {
            conjunctiveNormalForm = this.toCNFImpl();
        }
        return conjunctiveNormalForm;
    }

    public BidiMap getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(BidiMap variableMap) {
        this.variableMap = variableMap;
    }

    private Formula toCNFImpl() {
        if (isLeaf()) {
            return new Formula(this);
        }

        if (op == BOP.NOT) {
            if (left.isLeaf()) { // !a
                return new Formula(this);
            } else {  // !(...)
                Formula expr = left.driveInNegation();
                return expr.toCNFImpl();
            }
        }
        Formula cnfLeft = null;
        Formula cnfRight = null;
        if (left != null) {
            cnfLeft = left.toCNFImpl();
        }
        if (right != null) {
            cnfRight = right.toCNFImpl();
        }

        if (op == BOP.AND) {
            return new AndFormula(cnfLeft, cnfRight);
        }

        if (op == BOP.OR) {
            if ((cnfLeft == null || cnfLeft.isAtomic() || cnfLeft.op == BOP.OR)
                    && (cnfRight == null || cnfRight.isAtomic() || cnfRight.op == BOP.OR)) {
                return new OrFormula(cnfLeft, cnfRight);
            } else if ((cnfLeft != null && cnfLeft.op == BOP.AND)
                    && (cnfRight == null || cnfRight.isAtomic() || cnfRight.op == BOP.OR)) {
                Formula newLeft = new OrFormula(cnfLeft.left, cnfRight);
                Formula newRight = new OrFormula(cnfLeft.right, cnfRight);

                return new AndFormula(newLeft.toCNFImpl(), newRight.toCNFImpl());
            } else if ((cnfRight != null && cnfRight.op == BOP.AND)
                    && (cnfLeft == null || cnfLeft.isAtomic() || cnfLeft.op == BOP.OR)) {
                Formula newLeft = new OrFormula(cnfLeft, cnfRight.right);
                Formula newRight = new OrFormula(cnfLeft, cnfRight.left);

                return new AndFormula(newLeft.toCNFImpl(), newRight.toCNFImpl());
            } else if ((cnfLeft != null && cnfLeft.op == BOP.AND)
                    && (cnfRight != null && cnfRight.op == BOP.AND)) {
                Formula newLeft = new AndFormula(
                        new OrFormula(cnfLeft.left, cnfRight.left),
                        new OrFormula(cnfLeft.right, cnfRight.left));

                Formula newRight = new AndFormula(
                        new OrFormula(cnfLeft.left, cnfRight.right),
                        new OrFormula(cnfLeft.right, cnfRight.right));

                return new AndFormula(newLeft.toCNFImpl(), newRight.toCNFImpl());
            }
        }

        // error status, should NOT reach here
        System.out.println("Error Status");
        return null;
    }

    private Formula driveInNegation() {
        if (op == BOP.NOT) {
            //NOTE: there may be many consecutive NOT, for example: !!!A
            if (left.op == BOP.NOT) {
                return left.left.driveInNegation();
            } else {
                return new Formula(left);
            }
        } else if (op == BOP.AND) {
            return new OrFormula(left.driveInNegation(), right.driveInNegation());
        } else if (op == BOP.OR) {
            return new AndFormula(left.driveInNegation(), right.driveInNegation());
        } else {
            return new NotFormula(this);
        }
    }

    public boolean isClause() {
        if (isAtomic()) {
            return true;
        }
        return right.isClause() && op == BOP.OR && left.isClause();
    }

    public boolean isPhrase() {
        if (isAtomic()) {
            return true;
        }
        return right.isPhrase() && op == BOP.AND && left.isPhrase();
    }

    private void getClauses(List<Clause> clauses, Formula f) {
        if (f.isClause()) {
            clauses.add(new Clause(f));
            return;
        }

        if (f.left != null) {
            if (f.left.isClause()) {
                clauses.add(new Clause(f.left));
            } else {
                getClauses(clauses, f.left);
            }
        }
        if (f.right != null) {
            if (f.right.isClause()) {
                clauses.add(new Clause(f.right));
            } else {
                getClauses(clauses, f.right);
            }
        }
    }
}

