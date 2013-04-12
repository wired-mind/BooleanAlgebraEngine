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
public class GtTest {

    public GtTest() {
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

        Gt instance = new Gt("property", "123");
        assertTrue(instance.isTruthValue(context));

        instance = new Gt("property", "123.456");
        assertFalse(instance.isTruthValue(context));

        instance = new Gt("property", "1000");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareDates() throws ParseException, Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("15/01/2009");
        context.put("property", date);

        Gt instance = new Gt("property", "14/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Gt("property", "15/01/2009");
        assertFalse(instance.isTruthValue(context));

        instance = new Gt("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareCalendars() throws ParseException, Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 0, 15); // month value is 0-based
        context.put("property", calendar);

        Gt instance = new Gt("property", "14/01/2009");
        assertTrue(instance.isTruthValue(context));

        instance = new Gt("property", "15/01/2009");
        assertFalse(instance.isTruthValue(context));

        instance = new Gt("property", "01/01/2020");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanCompareStrings() throws Exception {
        String s = "xxxx";
        context.put("property", s);

        Gt instance = new Gt("property", "xxx");
        assertTrue(instance.isTruthValue(context));
    }
}
