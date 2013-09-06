package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class LikeTest {

    Context context = new ContextBase();

    public LikeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareNumbers() throws Exception {
        context.put("number", 555123456);

        Like instance = new Like("number", "123456");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("number", "123");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("number", "555");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("number", "9");
        assertFalse(instance.isTruthValue(context));

        instance = new Like("number", "122.999999");
        assertFalse(instance.isTruthValue(context));

        instance = new Like("number", null);
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareDates() throws Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("15/01/2009");
        context.put("property", date);

        Like instance = new Like("property", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareCalendars() throws ParseException, Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 0, 15); // month value is 0-based
        context.put("property", calendar);

        Like instance = new Like("property", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareStrings() throws Exception {
        context.put("desc", "Apple Sauce");

        Like instance = new Like("desc", "Apple");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("desc", "Orange");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareBooleans() throws Exception {
        context.put("boolean", true);

        Like instance = new Like("boolean", "true");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("boolean", "True");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("boolean", "TRUE");
        assertTrue(instance.isTruthValue(context));

        instance = new Like("boolean", "1");
        assertFalse(instance.isTruthValue(context));

        instance = new Like("boolean", "False");
        assertFalse(instance.isTruthValue(context));

        instance = new Like("boolean", null);
        assertFalse(instance.isTruthValue(context));
    }
}
