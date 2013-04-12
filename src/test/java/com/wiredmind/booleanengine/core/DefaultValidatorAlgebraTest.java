package com.wiredmind.booleanengine.core;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Craig Earley
 */
public class DefaultValidatorAlgebraTest {

    private final Logger LOGGER = Logger.getLogger(DefaultValidatorAlgebra.class.getName());
    private int atomEvaluationCounter;
    Context context = new ContextBase();
    Set<Rule> ValidatorRules = new HashSet<Rule>();

    public DefaultValidatorAlgebraTest() {
        LOGGER.setLevel(Level.FINEST);
        LOGGER.addHandler(new ValidatorAlgebraLoggingHandler());
    }

    class ValidatorAlgebraLoggingHandler extends Handler {

        @Override
        public void publish(LogRecord record) {
            if ("atomEvaluation".equals(record.getMessage())) {
                atomEvaluationCounter++;
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws ParseException {
        context.put("merchant", "Starbucks");
        context.put("Product", "venti skinny latte");
        context.put("Amount", 4.50);

        ValidatorRules.add((new ValidatorRule("A", null, null, "EQ", "merchant Starbucks")));
        ValidatorRules.add((new ValidatorRule("B", null, null, "LIKE", "Product latte")));
        ValidatorRules.add((new ValidatorRule("C", null, null, "LT", "Amount 20.00")));
        ValidatorRules.add((new ValidatorRule("D", null, null, "GE", "Amount 20.00")));
        ValidatorRules.add((new ValidatorRule("E", null, null, "True", null)));
        ValidatorRules.add((new ValidatorRule("F", null, null, "False", null)));

        atomEvaluationCounter = 0;
    }

    @Test
    public void testReservedWords() throws Exception {

        //  and     eq      true
        //  or      ne      false
        //  not

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A and B", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A eq true", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A or B", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A ne false", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A and not B", ValidatorRules);
        assertFalse(algebra.isTruthValue(context));
    }

    @Test
    public void testOperators() throws Exception {

        // ()
        // !        (not)
        // == !=    (eq ne)
        // &&       (and)
        // ||       (or)
        // ? :

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("((A && B) || C) and !D", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A == true", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A or B", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A != false", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("A ? B : C", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));
    }

    @Test
    public void testPrecedenceOfEvaluation() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A or (B and not C)", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));

        algebra = new DefaultValidatorAlgebra("(A or B) and not C", ValidatorRules);
        assertFalse(algebra.isTruthValue(context));
    }

    @Test
    public void testDisjunctiveShortCircuitLogic() {

        // A and B and C are True, no need to evaluate E
        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("(A and B and C) or E", ValidatorRules);
        try {
            algebra.execute(context);
            assertEquals(3, atomEvaluationCounter);
            assertTrue(algebra.isTruthValue());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testConjunctiveShortCircuitLogic() {

        // A is True but B is False, no need to evaluate C
        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A and !B and C", ValidatorRules);
        try {
            algebra.execute(context);
            assertEquals(2, atomEvaluationCounter);
            assertFalse(algebra.isTruthValue());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testOneTimeEvaluationOfStatementVariables() {

        // A and B (both True) are each evaluated once - even though they appear multiple times
        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A and B and A and B", ValidatorRules);
        try {
            algebra.execute(context);
            assertEquals(2, atomEvaluationCounter);
            assertTrue(algebra.isTruthValue());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testEmptyExpressionWithValidatorRules() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("", ValidatorRules);
        assertFalse(algebra.isTruthValue(context));
    }

    @Test
    public void testEmptyExpressionWithoutValidatorRules() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("", new ArrayList<Rule>());
        assertFalse(algebra.isTruthValue(context));
    }

    @Test()
    public void testMissingSomeValidatorRulesButCanSolve() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A or X", ValidatorRules);
        assertTrue(algebra.isTruthValue(context));
    }

    @Test(expected = javax.el.PropertyNotFoundException.class)
    public void testMissingSomeValidatorRulesAndCannotSolve() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A and (X and not Y)", ValidatorRules);
        assertFalse(algebra.isTruthValue(context));
    }

    @Test(expected = javax.el.PropertyNotFoundException.class)
    public void testMissingAllValidatorRules() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("U and !V and W", ValidatorRules);
        assertFalse(algebra.isTruthValue(context));
    }

    @Test
    public void testGetValuationMap() throws Exception {

        ValidatorAlgebra algebra = new DefaultValidatorAlgebra("A and !B and C", ValidatorRules);

        assertFalse(algebra.isTruthValue(context));

        Map<String, Boolean> valuationMap = algebra.getValuationMap();

        assertTrue(valuationMap.size() == 2);
        assertTrue(valuationMap.get("A") == true);
        assertTrue(valuationMap.get("B") == true);
        // Note effect of short-circuit logic: valuationMap does not contain key "C"

        System.out.println(valuationMap.toString());
    }
}

