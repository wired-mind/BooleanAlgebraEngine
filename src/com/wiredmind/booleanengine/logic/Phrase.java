package com.wiredmind.booleanengine.logic;

import com.wiredmind.booleanengine.logic.Formula.BOP;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 * A literal or a conjunction of literals.
 *
 * 
 */
public class Phrase extends Formula {

    protected Phrase(Var var) {
        // By definition: A literal is a phrase
        this.op = var.op;
        this.left = var.left == null ? null : new Formula(var.left);
        this.right = var.right == null ? null : new Formula(var.right);
        this.lit = var.lit == null ? null : new StringBuilder(var.lit).toString();
    }

    protected Phrase(Phrase left, Phrase right) {
        // By definition: If P and Q are phrases, so is (P and Q)
        this.op = BOP.AND;
        this.left = left;
        this.right = right;
        this.lit = null;
    }

    protected Phrase(Formula formula) {
        if (formula.isPhrase()) {
            this.op = formula.op;
            this.left = formula.left == null ? null : new Formula(formula.left);
            this.right = formula.right == null ? null : new Formula(formula.right);
            this.lit = formula.lit == null ? null : new StringBuilder(formula.lit).toString();
        } else {
            throw new IllegalArgumentException(formula.toString() + " is not a phrase");
        }
    }

    public Formula and(Phrase phrase) {
        return new Phrase(this, phrase);
    }

    // <editor-fold defaultstate="collapsed" desc="Dimacs conversion methods">
    public static Phrase fromDimacsArray(int[] arr) {
        if (arr.length == 0) {
            return null;
        }

        Phrase f = new Phrase(arr[0] < 0 ? new Var(arr[0]).not() : new Var(arr[0]));
        if (arr.length > 1) {
            for (int i = 1; i < arr.length; i++) {
                Phrase left = f;
                Phrase right = new Phrase(arr[i] < 0 ? new Var(Integer.toString(arr[i]).substring(1)).not() : new Var(arr[i]));
                f = (Phrase) left.and(right);
            }
        }
        return f;
    }

    public int[] toDimacsArray() {
        List<Integer> literals = new ArrayList<Integer>();
        addToList(this, literals);
        return ArrayUtils.toPrimitive(literals.toArray(new Integer[literals.size()]));
    }

    private void addToList(Formula f, List<Integer> literals) {
        if (f.isAtomic()) {
            if (f.op == BOP.NOT) {
                literals.add(0 - Integer.parseInt(f.left.toString()));
                return;
            }
            literals.add(Integer.parseInt(f.toString()));
            return;
        }
        if (f.op == BOP.AND) {
            addToList(f.left, literals);
            addToList(f.right, literals);
        }
    }// </editor-fold>
}
