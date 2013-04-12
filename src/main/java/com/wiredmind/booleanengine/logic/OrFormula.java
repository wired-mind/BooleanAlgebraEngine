package com.wiredmind.booleanengine.logic;

/**
 * A disjunction of two formulas.
 */
public class OrFormula extends Formula {

    private static final long serialVersionUID = 1L;

    public OrFormula(Formula left, Formula right) {
        this.op = BOP.OR;
        this.left = left;
        this.right = right;
        this.lit = null;
    }
}
