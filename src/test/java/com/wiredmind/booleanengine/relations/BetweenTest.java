package com.wiredmind.booleanengine.relations;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class BetweenTest {

    public BetweenTest() {
    }

    Context context = new ContextBase();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareNumbers() throws Exception {
        double number = 123.456;
        context.put("property", number);

        Between instance = new Between("property", "123", "124");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "123.456", "1000000");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "1", "123.456");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "1000", "1000");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareDates() throws Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("15/01/2009");
        context.put("property", date);

        Between instance = new Between("property", "14/01/2009", "16/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "15/01/2009", "16/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "14/01/2009", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "01/01/2020", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareCalendars() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 0, 15); // month value is 0-based
        context.put("property", calendar);

        Between instance = new Between("property", "14/01/2009", "16/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "15/01/2009", "16/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "14/01/2009", "15/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Between("property", "01/01/2020", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareStrings() throws Exception {
        String s = "xxx";
        context.put("property", s);

        Between instance = new Between("property", "xx", "xxxx");
        assertTrue(instance.isTruthValue(context));
    }
}
