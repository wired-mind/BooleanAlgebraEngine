package com.wiredmind.booleanengine.logic;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Craig Earley
 */
public class FormulaTest {

    public FormulaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testAnd() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.and(b));
        assertTrue("a and b".equals(f.toString()));
    }

    @Test
    public void testOr() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.or(b));
        assertTrue("a or b".equals(f.toString()));
    }

    @Test
    public void testNot() {
        Var a = new Var("a");

        Formula f = (a.not());
        assertTrue("!a".equals(f.toString()));
    }

    @Test
    public void testToString() {
        Formula emptyFormula = new Formula();
        assertTrue(emptyFormula.toString().equals(StringUtils.EMPTY));

        Var a = new Var("a");
        Var b = new Var("b");
        Var c = new Var("c");
        Var d = new Var("d");

        Formula f = (a.and(b)).or(c.and(d));
        assertTrue("(a and b) or (c and d)".equals(f.toString()));

        f = (a.not().or(b)).and(c);
        assertTrue("(!a or b) and c".equals(f.toString()));
    }

    @Test
    public void testToCNF() {
        Var a = new Var("a");
        Var b = new Var("b");
        Var c = new Var("c");
        Var d = new Var("d");

        Formula f = (a.and(b)).or(c.and(d));
        assertTrue("(a or c) and (b or c) and (a or d) and (b or d)".equals(f.toCNF().toString()));

        f = (a.and(b)).or(c).or(d);
        assertTrue("(a or c or d) and (b or c or d)".equals(f.toCNF().toString()));

        f = ((a.and(b)).or(c).or(d)).not();
        assertTrue("(!a or !b) and !c and !d".equals(f.toCNF().toString()));

        f = (a).not().not().not().not().not().not();
        assertTrue("a".equals(f.toCNF().toString()));

        f = (a).not().not().not().not().not();
        assertTrue("!a".equals(f.toCNF().toString()));

        f = ((a).and(b)).or((a).not().and(c)).or(((b).not()).and((c).not()));
        assertTrue("(a or !a or !b) and (b or !a or !b) and (a or c or !b) and (b or c or !b) and (a or !a or !c) and (b or !a or !c) and (a or c or !c) and (b or c or !c)".equals(f.toCNF().toString()));
    }

    @Test
    public void testToCNF_empty() {
        Formula f = new Formula();
        assertTrue(f.toCNF().isEmpty());
    }

    @Test
    public void testGetLeft() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.and(b));
        assertTrue(f.getLeft().equals(a));
    }

    @Test
    public void testGetOp() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.and(b));
        assertTrue(f.getOp().equals(Formula.BOP.AND));
    }

    @Test
    public void testGetRight() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.and(b));
        assertTrue(f.getRight().equals(b));
    }

    @Test
    public void testIsLeaf() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = (a.and(b));
        assertFalse(f.isLeaf());
        assertTrue(f.getLeft().isLeaf() == true && f.getRight().isLeaf() == true);
    }

    @Test
    public void testIsAtomic() {
        Var a = new Var("a");
        Var b = new Var("b");

        assertTrue(a.isAtomic()); // a literal is atomic
        assertTrue(a.not().isAtomic()); // a literal and its negation is atomic

        Formula f = (a.and(b));
        assertFalse(f.isAtomic());
        assertTrue(f.getLeft().isAtomic() == true && f.getRight().isAtomic() == true);
    }

    @Test
    public void testEquals() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = a.and(b);
        Formula g = a.and(b);

        assertFalse(f == g); // two distinct instances
        assertTrue(f.equals(g)); // objects equal

        Formula h = b.and(a);
        assertFalse(f.equals(h)); // logically equivalent but objects not equal
    }

    @Test
    public void testHashCode() {
        Var a = new Var("a");
        Var b = new Var("b");

        Formula f = a.and(b);
        Formula g = a.and(b);

        assertTrue(f.hashCode() == g.hashCode()); // two distinct instances

        Formula h = b.and(a);
        assertFalse(f.hashCode() == h.hashCode()); // logically equivalent but different hash codes
    }

    @Test
    public void testIsClause() {
        Var a = new Var("a");
        Var b = new Var("b");
        Var c = new Var("c");

        assertTrue(a.isClause());
        assertTrue(b.isClause());
        assertTrue(c.isClause());

        // if a and b are both clauses, then so is (a or b)
        Formula f = a.or(b);
        assertTrue(f.isClause());

        // if f is a literals and c is a literals, then so is (f or c)
        assertTrue(f.or(c).isClause());
        assertTrue(c.or(f).isClause());

        Formula g = a.and(b);
        assertFalse(g.isClause());

        assertFalse(g.or(c).isClause());
        assertFalse(c.or(g).isClause());
    }

    @Test
    public void testToCNFClauses() {
        Var a = new Var("a");
        Var b = new Var("b");
        Var c = new Var("c");
        Var d = new Var("d");

        Formula f = a.or(b).or(c).or(d);
        assertTrue("a or b or c or d".equals(f.toCNF().toString()));
        List<Clause> clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 1);
        assertTrue("a or b or c or d".equals(clauses.get(0).toString()));

        f = (a.and(b)).or(c.and(d));
        assertTrue("(a or c) and (b or c) and (a or d) and (b or d)".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 4);
        assertTrue(clauses.contains(new Clause(a.or(c))));
        assertTrue(clauses.contains(new Clause(b.or(c))));
        assertTrue(clauses.contains(new Clause(a.or(d))));
        assertTrue(clauses.contains(new Clause(b.or(d))));

        f = (a.and(b)).or(c).or(d);
        assertTrue("(a or c or d) and (b or c or d)".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 2);
        assertTrue(clauses.contains(new Clause(a.or(c).or(d))));
        assertTrue(clauses.contains(new Clause(b.or(c).or(d))));

        f = ((a.and(b)).or(c).or(d)).not();
        assertTrue("(!a or !b) and !c and !d".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 3);
        assertTrue(clauses.contains(new Clause((a.not()).or(b.not()))));
        assertTrue(clauses.contains(new Clause(c.not())));
        assertTrue(clauses.contains(new Clause(d.not())));

        f = (a).not().not().not().not().not().not();
        assertTrue("a".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 1);
        assertTrue(clauses.contains(new Clause(a)));

        f = (a).not().not().not().not().not();
        assertTrue("!a".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 1);
        assertTrue(clauses.contains(new Clause(a.not())));

        f = ((a).and(b)).or((a).not().and(c)).or(((b).not()).and((c).not()));
        assertTrue("(a or !a or !b) and (b or !a or !b) and (a or c or !b) and (b or c or !b) and (a or !a or !c) and (b or !a or !c) and (a or c or !c) and (b or c or !c)".equals(f.toCNF().toString()));
        clauses = f.toCNFClauses();
        assertTrue(clauses.size() == 8);
        assertTrue(clauses.contains(new Clause(a.or(a.not()).or(b.not()))));
        assertTrue(clauses.contains(new Clause(b.or(a.not()).or(b.not()))));
        assertTrue(clauses.contains(new Clause(a.or(c).or(b.not()))));
        assertTrue(clauses.contains(new Clause(b.or(c).or(b.not()))));
        assertTrue(clauses.contains(new Clause(a.or(a.not()).or(c.not()))));
        assertTrue(clauses.contains(new Clause(b.or(a.not()).or(c.not()))));
        assertTrue(clauses.contains(new Clause(a.or(c).or(c.not()))));
        assertTrue(clauses.contains(new Clause(b.or(c).or(c.not()))));
    }

    @Test
    public void testToCNFClauses_empty() {
        Formula f = new Formula();
        assertTrue(f.toCNFClauses().isEmpty() == true);
    }

    @Test
    public void testGetVariableMap() throws BadExpressionException, ContradictionException, TimeoutException {
        FormulaBuilder formulaBuilder = new FormulaBuilder("(a and b) or (a and c)");

        BidiMap map = formulaBuilder.build().getVariableMap();

        assertTrue(map.size() == 3);
        assertTrue("a".equals(map.get(1)));
        assertTrue("b".equals(map.get(2)));
        assertTrue("c".equals(map.get(3)));
    }

    @Test
    public void testIsPhrase() {
        Var a = new Var("a");
        Var b = new Var("b");
        Var c = new Var("c");

        assertTrue(a.isPhrase());
        assertTrue(b.isPhrase());
        assertTrue(c.isPhrase());

        // if a and b are both phrases, then so is (a and b)
        Formula f = a.and(b);
        assertTrue(f.isPhrase());

        // if f is a phrase and c is a phrase, then so is (f and c)
        assertTrue(f.and(c).isPhrase());
        assertTrue(c.and(f).isPhrase());

        Formula g = a.or(b);
        assertFalse(g.isPhrase());

        assertFalse(g.and(c).isPhrase());
        assertFalse(c.and(g).isPhrase());
    }

    @Test
    public void testIsEmpty() {
        Formula f = new Formula();
        assertTrue(f.isEmpty());
    }

    @Test
    public void testNewInstanceforSAT() throws Exception {
        Formula instance = Formula.newInstanceforSAT(new Var("a").and(new Var("b")));
        assertNotNull(instance);
    }

    @Test
    public void testNewInstanceforSAT_empty() throws Exception {
        Formula instance = Formula.newInstanceforSAT(new Formula());
        assertNotNull(instance);
        assertTrue(instance.getModels().isEmpty());
    }

    @Test
    public void testGetModels() throws ContradictionException, TimeoutException {
        Formula instance = Formula.newInstanceforSAT(new Var("a").and(new Var("b")));

        Set<Model> expResult = new HashSet<Model>();
        expResult.add(new Model(new int[]{1, 2}));

        Set<Model> result = instance.getModels();
        assertEquals(expResult, result);

    }

    @Test
    public void testGetModels_empty() throws ContradictionException, TimeoutException {
        Formula instance = Formula.newInstanceforSAT(new Formula());

        Set<Model> expResult = new HashSet<Model>();

        Set<Model> result = instance.getModels();
        assertEquals(expResult, result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = org.sat4j.specs.ContradictionException.class)
    public void testGetModels_contradiction() throws ContradictionException, TimeoutException {
        Formula f = new Var("a");
        Formula g = f.and(f.not());
        Formula instance = Formula.newInstanceforSAT(g);
    }

    @Test
    public void testIsSatisfiable() throws ContradictionException, TimeoutException {
        Formula instance = Formula.newInstanceforSAT(new Var("a").and(new Var("b")));
        assertTrue(instance.isSatisfiable());
    }

    @Test
    public void testIsSatisfiable_empty() throws ContradictionException, TimeoutException {
        Formula instance = Formula.newInstanceforSAT(new Formula());
        assertFalse(instance.isSatisfiable());
    }

    @Test
    public void testIsPossiblySatisfiedBy() throws ContradictionException, TimeoutException {
        Var P = new Var("P");
        Var Q = new Var("Q");
        Var R = new Var("R");
        Formula f = Formula.newInstanceforSAT((P.or(Q)).and(P.not().or(R))); // (P or Q) and (~P or R)

        // Possible assignments
        // ~P ~Q ~R
        // ~P ~Q  R
        // ~P  Q ~R (solution)
        // ~P  Q  R (solution
        //  P ~Q ~R
        //  P ~Q  R (solution)
        //  P  Q ~R
        //  P  Q  R (solution)

        BidiMap map = f.getVariableMap();
        assertTrue(map.get(1).equals("P"));
        assertTrue(map.get(2).equals("Q"));
        assertTrue(map.get(3).equals("R"));

        // The following partial models should pass
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{-1})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{1})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{-1, 2})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{1, 3})));

        // These partial models should not
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{-2, -3})));

        // Complete models, by definition, should satisfy (if solutions)
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{-1, -2, -3})));
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{-1, -2, 3})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{-1, 2, -3})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{-1, 2, 3})));
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{1, -2, -3})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{1, -2, 3})));
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{1, 2, -3})));
        assertTrue(f.isPossiblySatisfiedBy(new Model(new int[]{1, 2, 3})));
    }

    @Test
    public void testIsPossiblySatisfiedBy_empty() throws ContradictionException, TimeoutException {
        Formula f = Formula.newInstanceforSAT(new Formula()); // emptyFormula

        assertTrue(f.getModels().isEmpty());
        // emptyFormula formula is not possibly satisfiable - not even by an emptyFormula model
        assertFalse(f.isPossiblySatisfiedBy(new Model(new int[]{})));
    }

    @Test
    public void testIsSatisfiedBy() throws ContradictionException, TimeoutException {
        Var P = new Var("P");
        Var Q = new Var("Q");
        Var R = new Var("R");
        Formula f = Formula.newInstanceforSAT((P.or(Q)).and(P.not().or(R))); // (P or Q) and (~P or R)

        // Possible assignments
        // ~P ~Q ~R
        // ~P ~Q  R
        // ~P  Q ~R (solution)
        // ~P  Q  R (solution
        //  P ~Q ~R
        //  P ~Q  R (solution)
        //  P  Q ~R
        //  P  Q  R (solution)

        BidiMap map = f.getVariableMap();
        assertTrue(map.get(1).equals("P"));
        assertTrue(map.get(2).equals("Q"));
        assertTrue(map.get(3).equals("R"));

        // Only complete models, if solutions, should pass
        assertFalse(f.isSatisfiedBy(new Model(new int[]{-1, -2, -3})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{-1, -2, 3})));
        assertTrue(f.isSatisfiedBy(new Model(new int[]{-1, 2, -3})));
        assertTrue(f.isSatisfiedBy(new Model(new int[]{-1, 2, 3})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{1, -2, -3})));
        assertTrue(f.isSatisfiedBy(new Model(new int[]{1, -2, 3})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{1, 2, -3})));
        assertTrue(f.isSatisfiedBy(new Model(new int[]{1, 2, 3})));

        // Partial models, passing isPossiblySatisfiedBy(), should not pass here
        assertFalse(f.isSatisfiedBy(new Model(new int[]{})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{-1})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{1})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{-1, 2})));
        assertFalse(f.isSatisfiedBy(new Model(new int[]{1, 3})));
    }

    @Test
    public void testIsSatisfiedBy_empty() throws ContradictionException, TimeoutException {
        Formula f = Formula.newInstanceforSAT(new Formula()); // emptyFormula

        assertTrue(f.getModels().isEmpty());
        // emptyFormula formula is not satisfiable - not even by an emptyFormula model
        assertFalse(f.isSatisfiedBy(new Model(new int[]{})));
    }

    @Test
    public void testToCNFDimacsClauses() {
        Formula a = new Var("a");
        Formula b = new Var("b");
        Formula c = new Var("c");

        Formula f = new Formula(a.not());
        assertTrue("!a".equals(f.toString()));

        List<IVecInt> clauses = f.toCNFDimacsClauses();
        assertTrue(clauses.size() == 1);
        IVecInt literals = clauses.get(0);

        // expect {-1} array
        assertTrue(literals.get(0) == -1);

        f = new Formula(b.or(a.not()).or(c));
        assertTrue("b or !a or c".equals(f.toString()));

        clauses = f.toCNFDimacsClauses();
        assertTrue(clauses.size() == 1);
        literals = clauses.get(0);

        // expect {2, -1, 3} array
        assertTrue(literals.get(0) == 2);
        assertTrue(literals.get(1) == -1);
        assertTrue(literals.get(2) == 3);
    }
}
