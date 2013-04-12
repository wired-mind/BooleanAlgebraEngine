package com.wiredmind.booleanengine.logic;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.commons.lang.StringUtils;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * A Boolean expression. Supports conjunction (and), disjunction (or)
 * and denial (not). Note that material implication (a -> b) is
 * logically equivalent to (~a or b).
 */
public class Formula implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Formula.class);
    private static final long serialVersionUID = 1L;
    private static final Formula EMPTY = new Formula();
    private static final int MAXVAR = 1000; // maximum expected variables for SATsolver

    public enum BOP {

        LEAF, AND, OR, NOT
    }

    BOP op;
    Formula left;
    Formula right;
    String lit;
    /*
     * Populated in newInstanceforSAT
     */
    private final Set<Model> models = new HashSet<Model>();
    private final Map<Integer, String> variableMap = new TreeMap<Integer, String>();

    /**
     * Constructs a new empty formula.
     */
    public Formula() {
    }

    /**
     * Constructs a new formula that is a copy of the provided formula.
     *
     * @param other Formula to copy
     */
    public Formula(Formula other) {
        this.op = other.op;
        this.left = other.left == null ? null : new Formula(other.left);
        this.right = other.right == null ? null : new Formula(other.right);
        this.lit = other.lit == null ? null : other.lit;
    }

    /**
     * Returns a distinct instance of a formula that is a copy of the
     * provided formula and is also composed of a set of {@link Model}s that
     * satisfy the formula (if any).
     * <p>An empty formula generates an empty set of models.</p>
     *
     * @param formula The Formula
     * @return a new formula composed of a set of models that satisfy that formula.
     * @throws ContradictionException
     * @throws TimeoutException
     */
    public static Formula newInstanceforSAT(Formula formula) throws ContradictionException, TimeoutException {
        Formula f = new Formula(formula);
        if (!f.isEmpty()) {
            f.genSATModels();
        }
        return f;
    }

    private void genSATModels() throws ContradictionException, TimeoutException {
        ISolver solver = new ModelIterator(SolverFactory.newLight());
        solver.newVar(MAXVAR);
        List<IVecInt> clauses = this.toCNFDimacsClauses();
        solver.setExpectedNumberOfClauses(clauses.size());

        LOGGER.debug("Getting SAT models for Formula: {}", this.toString());
        LOGGER.debug("Formula CNF equivalent: {}", this.toCNF());
        LOGGER.debug("The CNF clauses are:");

        for (IVecInt literals : clauses) {
            LOGGER.debug(literals.toString());
            solver.addClause(literals);
        }

        LOGGER.debug("The following models satisfy the formula:");

        while (solver.isSatisfiable()) {
            Model model = new Model(solver.model());
            models.add(model);
            LOGGER.debug(model.toString());
        }
    }

    /**
     * Returns the clauses of the conjunctive
     * normal form (CNF) of this Formula where
     * the literals are represented by integers.
     * <p/>
     * <p>The mapping of integer values to their
     * original variable names can be obtained
     * from {@link #getVariableMap}.</p>
     *
     * @return A new list of clauses in the Dimacs
     *         way of representing literals
     */
    public List<IVecInt> toCNFDimacsClauses() {
        List<IVecInt> dimacsClauses = new ArrayList<IVecInt>();
        Integer mapKey = 0;
        for (Clause clause : this.toCNFClauses()) {
            Set<String> literals = clause.getLiterals();
            for (String literal : literals) {
                if (!variableMap.containsValue(literal)) {
                    mapKey++;
                    variableMap.put(mapKey, literal);
                }
            }
            IVecInt vec = new VecInt(literals.size());
            toDimacsArray(clause, this.getVariableMap(), vec);
            dimacsClauses.add(vec);
        }
        return dimacsClauses;
    }

    private void toDimacsArray(Formula f, BidiMap map, IVecInt vec) {
        // Precondition: Formula f is CNF clause
        // Precondition: BidiMap map contains all literals in clause
        if (f.isAtomic()) {
            if (f.op == BOP.NOT) {
                vec.push(0 - ((Integer) map.getKey(f.left.toString())));
                return;
            }
            vec.push((Integer) map.getKey(f.toString()));
            return;
        }
        if (f.op == BOP.OR) {
            toDimacsArray(f.left, map, vec);
            toDimacsArray(f.right, map, vec);
        }
    }

    /**
     * Returns a new formula that is the conjunction
     * if this formula and the provided formula.
     *
     * @param formula The Formula
     * @return a new formula
     */
    public Formula and(Formula formula) {
        return new AndFormula(this, formula);
    }

    /**
     * Returns a new formula that is the disjunction
     * if this formula and the provided formula.
     *
     * @param formula The Formula
     * @return a new formula
     */
    public Formula or(Formula formula) {
        return new OrFormula(this, formula);
    }

    /**
     * Returns a new formula that is the denial
     * if this formula.
     *
     * @return a new formula
     */
    public Formula not() {
        return new NotFormula(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Formula.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Formula other = (Formula) obj;
        if (this.op != other.op && (this.op == null || !this.op.equals(other.op))) {
            return false;
        }
        if (this.left != other.left && (this.left == null || !this.left.equals(other.left))) {
            return false;
        }
        return !(this.right != other.right && (this.right == null || !this.right.equals(other.right))) && !((this.lit == null) ? (other.lit != null) : !this.lit.equals(other.lit));
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

    /**
     * Returns a string representation of the formula. The exact
     * details of the representation are unspecified and subject to change, but
     * the following may be regarded as typical for the formula (a.not().or(b)).and(c):
     * <p/>
     * <p>"(!a or b) and c"</p>
     * <p/>
     * <p>Note that an empty formula returns an empty string.</p>
     */
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder b = new StringBuilder();
        if (op == BOP.LEAF) {
            b.append(lit);
        } else if (op == BOP.NOT) {
            if (left.op == BOP.LEAF || left.op == BOP.NOT) {
                b.append("!").append(left.toString());
            } else {
                b.append("!(").append(left.toString()).append(")");
            }
        } else {
            if (left != null) {
                if (left.isLeaf() || (left.getOp() == op) || (left.getOp() == BOP.NOT)) {
                    b.append(left.toString());
                } else {
                    b.append("(").append(left.toString()).append(")");
                }
            }
            switch (op) {
                case AND:
                    b.append(" and ");
                    break;
                case OR:
                    b.append(" or ");
                    break;
                default:
                    // error status
                    b.append(" ? ");
                    break;
            }
            if (right != null) {
                if (right.isLeaf() || (right.getOp() == op) || (right.getOp() == BOP.NOT)) {
                    b.append(right.toString());
                } else {
                    b.append("(").append(right.toString()).append(")");
                }
            }
        }
        return b.toString();
    }

    public Formula getLeft() {
        return new Formula(left);
    }

    public BOP getOp() {
        return op;
    }

    public Formula getRight() {
        return new Formula(right);
    }

    public Boolean isLeaf() {
        return (op == BOP.LEAF);
    }

    public Boolean isAtomic() {
        return (isLeaf() || (op == BOP.NOT && left.isLeaf()));
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    /**
     * Returns the clauses of the conjunctive
     * normal form (CNF) of this Formula.
     *
     * @return A new list of clauses
     */
    public List<Clause> toCNFClauses() {
        List<Clause> clauses = new ArrayList<Clause>();
        if (!this.isEmpty()) {
            getClauses(clauses, this.toCNF());
        }
        return clauses;
    }

    /**
     * Returns the integer/variable mapping, e.g.,
     * <code>{1->a, 2->b, 3->c}</code>, which is used to
     * interpret the models returned with {@link #newInstanceforSAT}.
     *
     * @return a bidirectional lookup map.
     */
    public BidiMap getVariableMap() {
        return new TreeBidiMap(variableMap);
    }

    /**
     * Returns a new Formula which represents the conjunctive
     * normal form (CNF) of this formula.
     *
     * @return A new Formula in CNF which is logically equivalent
     *         to this formula
     */
    public final Formula toCNF() {
        if (isEmpty()) {
            return new Formula();
        }
        if (isLeaf()) {
            return new Formula(this);
        }

        if (op == BOP.NOT) {
            if (left.isLeaf()) { // !a
                return new Formula(this);
            } else {  // !(...)
                Formula expr = left.driveInNegation();
                return expr.toCNF();
            }
        }
        Formula cnfLeft = null;
        Formula cnfRight = null;
        if (left != null) {
            cnfLeft = left.toCNF();
        }
        if (right != null) {
            cnfRight = right.toCNF();
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

                return new AndFormula(newLeft.toCNF(), newRight.toCNF());
            } else if ((cnfRight != null && cnfRight.op == BOP.AND)
                    && (cnfLeft == null || cnfLeft.isAtomic() || cnfLeft.op == BOP.OR)) {
                Formula newLeft = new OrFormula(cnfLeft, cnfRight.right);
                Formula newRight = new OrFormula(cnfLeft, cnfRight.left);

                return new AndFormula(newLeft.toCNF(), newRight.toCNF());
            } else if ((cnfLeft != null && cnfLeft.op == BOP.AND)
                    && (cnfRight != null && cnfRight.op == BOP.AND)) {
                Formula newLeft = new AndFormula(
                        new OrFormula(cnfLeft.left, cnfRight.left),
                        new OrFormula(cnfLeft.right, cnfRight.left));

                Formula newRight = new AndFormula(
                        new OrFormula(cnfLeft.left, cnfRight.right),
                        new OrFormula(cnfLeft.right, cnfRight.right));

                return new AndFormula(newLeft.toCNF(), newRight.toCNF());
            }
        }
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
        return isAtomic() || right.isClause() && op == BOP.OR && left.isClause();
    }

    public boolean isPhrase() {
        return isAtomic() || right.isPhrase() && op == BOP.AND && left.isPhrase();
    }

    private void getClauses(List<Clause> clauses, Formula f) {
        // Precondition: Formula f in CNF
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

    public Set<Model> getModels() {
        return new HashSet<Model>(models);
    }

    public boolean isSatisfiable() {
        return !models.isEmpty();
    }

    /**
     * Determines whether a proposed {@link Model} (consisting of an vec
     * of literals) possibly matches at least one of the models in the formula's
     * model set. For example, the proposed model [1, -2] possibly matches the
     * first model in the set {[1, -2, 3], [1, 2, -3]} until the last literal
     * (whether it is 3 or -3) becomes known.
     * <p/>
     * <p>Provides a means of testing candidacy of proposed models.</p>
     *
     * @param proposed the proposed model
     * @return false only if a match cannot be found, otherwise returns true.
     */
    public boolean isPossiblySatisfiedBy(Model proposed) {
        LOGGER.debug("Proposed model is: {}", proposed.toString());
        for (Model model : models) {
            if (proposed.isSubsetOf(model)) {
                LOGGER.debug("{} possibly matches the model {}", proposed.toString(), proposed.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a proposed {@link Model} (consisting of an vec
     * of literals) exactly matches one of the models in the formula's set.
     *
     * @param proposed the proposed model
     * @return true only if a match is found, otherwise returns false.
     */
    public boolean isSatisfiedBy(Model proposed) {
        LOGGER.debug("Proposed model is: {}", proposed.toString());
        for (Model model : models) {
            if (proposed.equals(model)) {
                LOGGER.debug("{} exactly matches the model {}", proposed.toString(), model.toString());
                return true;
            }
        }
        return false;
    }
}
