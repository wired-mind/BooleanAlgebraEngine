package com.wiredmind.booleanengine.logic;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class ClauseTest {

    public ClauseTest() {
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
    public void testCreateFromLiteral() {
        Var a = new Var("a");
        Clause clause = new Clause(a);
        assertTrue("a".equals(clause.toString()));

        a = new Var(1);
        clause = new Clause(a);
        assertTrue("1".equals(clause.toString()));

        a = new Var(true);
        clause = new Clause(a);
        assertTrue("true".equals(clause.toString()));
    }

    @Test
    public void testCreateFromNegationOfLiteral() {
        Var a = new Var("a");
        Clause clause = new Clause(a.not());
        assertTrue("!a".equals(clause.toString()));

        a = new Var(1);
        clause = new Clause(a.not());
        assertTrue("!1".equals(clause.toString()));

        a = new Var(true);
        clause = new Clause(a.not());
        assertTrue("!true".equals(clause.toString()));
    }

    @Test
    public void testCreateFromDisjunctionOfLiterals() {
        Var a = new Var("a");
        Var b = new Var("b");

        Clause clause = new Clause(a.or(b));
        assertTrue("a or b".equals(clause.toString()));

        clause = new Clause(a.or(b.not()));
        assertTrue("a or !b".equals(clause.toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenFormulaIsNotAClause() {
        Var a = new Var("a");
        Var b = new Var("b");
        new Clause(a.and(b));
    }

    @Test
    public void testOr() {
        Clause a = new Clause(new Var("a"));
        Clause sub = new Clause(new Var("b").or(new Var("c")));

        // Disjunction of two clauses should result in a new clause
        Formula f = a.or(sub);
        assertTrue("a or b or c".equals(f.toString()));
        assertTrue(f instanceof Clause);
    }

    @Test
    public void testGetLiterals() {
        Formula a = new Var("a");
        Formula b = new Var("b");
        Formula c = new Var("c");

        Clause clause = new Clause(b.or(a.not()).or(c).or(a));
        assertTrue("b or !a or c or a".equals(clause.toString()));

        Set<String> literals = clause.getLiterals();
        assertTrue(literals.size() == 3);
        assertTrue(literals.contains("a"));
        assertTrue(literals.contains("b"));
        assertTrue(literals.contains("c"));
    }
}
