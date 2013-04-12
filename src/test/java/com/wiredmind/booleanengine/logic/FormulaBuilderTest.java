package com.wiredmind.booleanengine.logic;

import org.apache.commons.collections.BidiMap;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Craig Earley
 */
public class FormulaBuilderTest {

    public FormulaBuilderTest() {
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
    public void testGetVariableMap() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("(a and b) or (a and c)");
        Formula f = formulaBuilder.build();

        BidiMap map = f.getVariableMap();

        assertTrue("a".equals(map.get(1)));
        assertTrue("b".equals(map.get(2)));
        assertTrue("c".equals(map.get(3)));

        // Ensure string representation of formula is unaffected
        assertTrue(f.toString().equals("(a and b) or (a and c)"));
    }

    @Test
    public void testHasPredicate() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("(a and b) or (c and d)");
        formulaBuilder.build();

        assertFalse(formulaBuilder.hasPredicate());

        formulaBuilder = new FormulaBuilder("a ? b : c");
        formulaBuilder.build();

        assertTrue(formulaBuilder.hasPredicate());
    }

    @Test
    public void testGetPredicate() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("(a or d) ? b : c");
        formulaBuilder.build();

        assertTrue(formulaBuilder.getPredicate().equals(new Var("a").or(new Var("d"))));
    }

    @Test
    public void testGetConsequent() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("a ? (b and d) : c");
        formulaBuilder.build();

        assertTrue(formulaBuilder.getConsequent().equals(new Var("b").and(new Var("d"))));
    }

    @Test
    public void testGetAlternative() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("a ? b : c and d");
        formulaBuilder.build();

        assertTrue(formulaBuilder.getAlternative().equals(new Var("c").and(new Var("d"))));
    }

    @Test
    public void testGetExpression() {
        String expression = "(a and b) or (c and d)";
        FormulaBuilder formulaBuilder = new FormulaBuilder(expression);

        assertEquals(formulaBuilder.getExpression(), expression);
    }

    @Test
    public void testBuild() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("(a and b) or (c and d)");
        Formula f = formulaBuilder.build();

        // Formula g built manually
        Formula a = new Var("a");
        Formula b = new Var("b");
        Formula c = new Var("c");
        Formula d = new Var("d");
        Formula g = (a.and(b)).or(c.and(d));

        // Assert that Formula objects are equal
        assertTrue(f.equals(g));
    }

    @Test
    public void testBuild_EmptyExpression() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("");
        Formula f = formulaBuilder.build();

        assertTrue(f.isEmpty());
    }

    @Test(expected = BadExpressionException.class)
    public void testBuild_BadExpression() throws Exception {
        FormulaBuilder formulaBuilder = new FormulaBuilder("a foobar b");
        formulaBuilder.build();
    }
}
