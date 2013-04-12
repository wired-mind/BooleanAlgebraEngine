package com.wiredmind.booleanengine.logic;

/**
 * A conjunction of two formulas.
 */
public class AndFormula extends Formula {

    private static final long serialVersionUID = 1L;

    public AndFormula(Formula left, Formula right) {
        this.op = BOP.AND;
        this.left = left;
        this.right = right;
        this.lit = null;
    }
}
