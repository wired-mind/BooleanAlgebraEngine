package com.wiredmind.booleanengine.logic;

/**
 * A variable (literal) represents an atomic formula.
 *
 * 
 */
public class Var extends Formula {

    private Var() {
        this.op = BOP.LEAF;
        this.left = null;
        this.right = null;
    }

    public Var(String literal) {
        this();
        this.lit = literal;
    }

    public Var(int literal) {
        this();
        this.lit = Integer.toString(literal);
    }

    public Var(boolean literal) {
        this();
        this.lit = Boolean.toString(literal);
    }
}
