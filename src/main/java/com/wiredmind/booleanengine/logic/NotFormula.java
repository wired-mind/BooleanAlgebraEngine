package com.wiredmind.booleanengine.logic;

/**
 * The denial of a formula.
 */
public class NotFormula extends Formula {

    private static final long serialVersionUID = 1L;

    public NotFormula(Formula formula) {
        this.op = BOP.NOT;
        this.left = formula;
        this.right = null;
        this.lit = null;
    }
}
