package com.wiredmind.booleanengine.logic;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class PhraseTest {

    public PhraseTest() {
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
        Phrase phrase = new Phrase(a);
        assertTrue("a".equals(phrase.toString()));

        a = new Var(1);
        phrase = new Phrase(a);
        assertTrue("1".equals(phrase.toString()));

        a = new Var(true);
        phrase = new Phrase(a);
        assertTrue("true".equals(phrase.toString()));
    }

    @Test
    public void testCreateFromNegationOfLiteral() {
        Var a = new Var("a");
        Phrase phrase = new Phrase(a.not());
        assertTrue("!a".equals(phrase.toString()));

        a = new Var(1);
        phrase = new Phrase(a.not());
        assertTrue("!1".equals(phrase.toString()));

        a = new Var(true);
        phrase = new Phrase(a.not());
        assertTrue("!true".equals(phrase.toString()));
    }

    @Test
    public void testCreateFromConjunctionOfLiterals() {
        Var a = new Var("a");
        Var b = new Var("b");

        Phrase phrase = new Phrase(a.and(b));
        assertTrue("a and b".equals(phrase.toString()));
    }

    @Test
    public void testAnd() {
        Phrase a = new Phrase(new Var("a"));
        Phrase sub = new Phrase(new Var("b").and(new Var("c")));

        // Disjunction of two clauses should result in a new phrase
        Formula f = a.and(sub);
        assertTrue("a and b and c".equals(f.toString()));
        assertTrue(f instanceof Phrase);
    }
}
