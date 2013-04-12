package com.wiredmind.booleanengine.logic;

import java.util.Set;
import java.util.TreeSet;

/**
 * A literal or a disjunction of literals.
 */
public class Clause extends Formula {

    private static final long serialVersionUID = 1L;

    protected Clause() {
    }

    protected Clause(Var var) {
        // By definition: A literal is a clause
        this.op = var.op;
        this.left = var.left == null ? null : new Formula(var.left);
        this.right = var.right == null ? null : new Formula(var.right);
        this.lit = var.lit == null ? null : new StringBuilder(var.lit).toString();
    }

    protected Clause(Clause left, Clause right) {
        // By definition: If P and Q are clauses, so is (P or Q)
        this.op = BOP.OR;
        this.left = left;
        this.right = right;
        this.lit = null;
    }

    public Clause(Formula formula) {
        if (formula.isClause()) {
            this.op = formula.op;
            this.left = formula.left == null ? null : new Formula(formula.left);
            this.right = formula.right == null ? null : new Formula(formula.right);
            this.lit = formula.lit == null ? null : new StringBuilder(formula.lit).toString();
        } else {
            throw new IllegalArgumentException(formula.toString() + " is not a clause");
        }
    }

    public Formula or(Clause clause) {
        return new Clause(this, clause);
    }

    public Set<String> getLiterals() {
        Set<String> literals = new TreeSet<String>();
        addToLiterals(this, literals);
        return literals;
    }

    private void addToLiterals(Formula f, Set<String> literals) {
        if (f.isAtomic()) {
            if (f.op == BOP.NOT) {
                literals.add(f.left.toString());
                return;
            }
            literals.add(f.toString());
            return;
        }
        if (f.op == BOP.OR) {
            addToLiterals(f.left, literals);
            addToLiterals(f.right, literals);
        }
    }
}
