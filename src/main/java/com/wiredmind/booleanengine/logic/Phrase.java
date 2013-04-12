package com.wiredmind.booleanengine.logic;

/**
 * A literal or a conjunction of literals.
 */
public class Phrase extends Formula {

    private static final long serialVersionUID = 1L;

    public Phrase(Var var) {
        // By definition: A literal is a phrase
        this.op = var.op;
        this.left = var.left == null ? null : new Formula(var.left);
        this.right = var.right == null ? null : new Formula(var.right);
        this.lit = var.lit == null ? null : var.lit;
    }

    public Phrase(Phrase left, Phrase right) {
        // By definition: If P and Q are phrases, so is (P and Q)
        this.op = BOP.AND;
        this.left = left;
        this.right = right;
        this.lit = null;
    }

    public Phrase(Formula formula) {
        if (formula.isPhrase()) {
            this.op = formula.op;
            this.left = formula.left == null ? null : new Formula(formula.left);
            this.right = formula.right == null ? null : new Formula(formula.right);
            this.lit = formula.lit == null ? null : formula.lit;
        } else {
            throw new IllegalArgumentException(formula.toString() + " is not a phrase");
        }
    }

    public Formula and(Phrase phrase) {
        return new Phrase(this, phrase);
    }
}
