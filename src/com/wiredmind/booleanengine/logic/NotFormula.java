package com.wiredmind.booleanengine.logic;

/**
 * The denial of a formula.
 *
 * 
 */
public class NotFormula extends Formula {

    public NotFormula(Formula formula) {
        this.op = BOP.NOT;
        this.left = formula;
        this.right = null;
        this.lit = null;
    }
}
