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
public class EqTest {

    Context context = new ContextBase();

    public EqTest() {
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
        double number = 123;
        context.put("property", number);

        Eq instance = new Eq("property", "123.00");
        assertTrue(instance.isTruthValue(context));

        instance = new Eq("property", "123.456");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareDates() throws ParseException, Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("15/01/2009");
        context.put("property", date);

        Eq instance = new Eq("property", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Eq("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareCalendars() throws ParseException, Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 0, 15); // month value is 0-based
        context.put("property", calendar);

        Eq instance = new Eq("property", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Eq("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareStrings() throws Exception {
        String s = "This is a TEST";
        context.put("property", s);

        Eq instance = new Eq("property", "this is a test");
        assertTrue(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareBooleans() throws Exception {
        boolean b = true;
        context.put("property", b);

        Eq instance = new Eq("property", "true");
        assertTrue(instance.isTruthValue(context));

        instance = new Eq("property", "false");
        assertFalse(instance.isTruthValue(context));

        b = false;
        context.put("property", b);

        instance = new Eq("property", "false");
        assertTrue(instance.isTruthValue(context));

        instance = new Eq("property", "true");
        assertFalse(instance.isTruthValue(context));
    }
}
