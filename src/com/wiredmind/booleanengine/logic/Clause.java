package com.wiredmind.booleanengine.logic;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 * A literal or a disjunction of literals.
 * 
 * 
 */
public class Clause extends Formula {

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

    protected Clause(Formula formula) {
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

    // <editor-fold defaultstate="collapsed" desc="Dimacs conversion methods">
    public static Clause fromDimacsArray(int[] arr) {
        if (arr.length == 0) {
            return null;
        }

        Clause f = new Clause(arr[0] < 0 ? new Var(arr[0]).not() : new Var(arr[0]));
        if (arr.length > 1) {
            for (int i = 1; i < arr.length; i++) {
                Clause left = f;
                Clause right = new Clause(arr[i] < 0 ? new Var(Integer.toString(arr[i]).substring(1)).not() : new Var(arr[i]));
                f = (Clause) left.or(right);
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
        if (f.op == BOP.OR) {
            addToList(f.left, literals);
            addToList(f.right, literals);
        }
    }// </editor-fold>
}
