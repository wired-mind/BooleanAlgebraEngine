package com.wiredmind.booleanengine.logic;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Craig Earley
 */
public class ModelTest {

    public ModelTest() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_literalAndItsNegation() {
        Model instance = new Model(new int[]{-1, 2, -2}); // cannot contain a literal and its negation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_zero() {
        Model instance = new Model(new int[]{0, -1, 2,}); // cannot have a literal value of 0
    }

    @Test
    public void testAdd() {
        int literal = 4;
        Model instance = new Model(new int[]{-1, 2, -3});
        instance.add(literal);

        assertArrayEquals(instance.toArray(), new Integer[]{-1, 2, -3, 4});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAddALiteralsNegation() {
        int literal = 2;
        Model instance = new Model(new int[]{-1, -2, -3}); // cannot contain a literal and its negation
        instance.add(literal);
    }

    @Test
    public void testContains() {
        int literal = 4;
        Model instance = new Model(new int[]{-1, 2, -3});
        boolean expResult = false;
        boolean result = instance.contains(literal);
        assertEquals(expResult, result);

        expResult = true;
        result = instance.contains(-3);
        assertEquals(expResult, result);
    }

    @Test
    public void testDifferenceOf() {
        Model A = new Model(new int[]{-1, 2});
        Model B = new Model(new int[]{3, 4, 5});
        Model expResult = new Model(new int[]{-1, 2});

        Model result = A.differenceOf(B);
        assertEquals(expResult, result);

        A = new Model(new int[]{1, -2, 3});
        B = new Model(new int[]{4, 5, 3});
        expResult = new Model(new int[]{1, -2});

        result = A.differenceOf(B);
        assertEquals(expResult, result);

        A = new Model(new int[]{-1, 2});
        B = new Model(new int[]{2, -1});
        expResult = new Model(new int[]{});

        result = A.differenceOf(B);
        assertEquals(expResult, result);

        A = new Model(new int[]{1, -2, 3, 4});
        B = new Model(new int[]{1, 3});
        expResult = new Model(new int[]{-2, 4});

        result = A.differenceOf(B);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSubsetOf() {
        Model model = new Model(new int[]{-1, 2, -3});

        assertTrue(model.isSubsetOf(new Model(new int[]{-1, 2, -3})));
        assertTrue(model.isSubsetOf(new Model(new int[]{2, -3, -1})));
        assertTrue(model.isSubsetOf(new Model(new int[]{-1, 2, -3, 4})));

        assertFalse(model.isSubsetOf(new Model(new int[]{})));
        assertFalse(model.isSubsetOf(new Model(new int[]{-1, 2})));
    }

    @Test
    public void testIsSupersetOf() {
        Model model = new Model(new int[]{-1, 2, -3});

        assertTrue(model.isSupersetOf(new Model(new int[]{})));
        assertTrue(model.isSupersetOf(new Model(new int[]{-1})));
        assertTrue(model.isSupersetOf(new Model(new int[]{2})));
        assertTrue(model.isSupersetOf(new Model(new int[]{-1, 2})));
        assertTrue(model.isSupersetOf(new Model(new int[]{2, -3})));
        assertTrue(model.isSupersetOf(new Model(new int[]{-1, 2})));
        assertTrue(model.isSupersetOf(new Model(new int[]{2, -1})));
        assertTrue(model.isSupersetOf(new Model(new int[]{-1, 2, -3})));
        assertTrue(model.isSupersetOf(new Model(new int[]{2, -3, -1})));

        assertFalse(model.isSupersetOf(new Model(new int[]{1})));
        assertFalse(model.isSupersetOf(new Model(new int[]{5})));
        assertFalse(model.isSupersetOf(new Model(new int[]{1, 2})));
        assertFalse(model.isSupersetOf(new Model(new int[]{-1, 2, -3, 4})));

    }

    @Test
    public void testEquals() {
        Model model = new Model(new int[]{-1, 2, -3});

        assertTrue(model.equals(new Model(new int[]{-1, 2, -3})));
        assertTrue(model.equals(new Model(new int[]{2, -3, -1})));
        assertTrue(model.equals(new Model(new int[]{-1, 2, -3, -3, 2, -1})));

        assertFalse(model.equals(new Model(new int[]{-1, 2, -3, 4})));
        assertFalse(model.equals(new Model(new int[]{})));
        assertFalse(model.equals(new Model(new int[]{-1, 2})));
    }

    @Test
    public void testSize() {
        Model A = new Model(new int[]{-1, 2, -3});
        Model B = new Model(new int[]{-1, 2, 2, 2, -3, -3, -3, -3});
        assertEquals(A.size(), B.size());
    }

    @Test
    public void testIsEmpty() {
        Model model = new Model(new int[]{});
        assertTrue(model.isEmpty());

        model = new Model(new int[]{-1, 2, -3});
        assertFalse(model.isEmpty());
    }

    @Test
    public void testToArray() {
        Model model = new Model(new int[]{-1, 2, -3});
        Integer[] arr = model.toArray(new Integer[3]);
        assertTrue(model.isSubsetOf(new Model(arr)));
        assertTrue(model.isSupersetOf(new Model(arr)));
    }

    @Test
    public void testToString() {
        Model instance = new Model(new int[]{-1, 2, 3});
        String expected = "[-1, 2, 3]";
        assertEquals(expected, instance.toString());
    }
}
