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
public class MatchTest {

    public MatchTest() {
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
    public void testCanMatchNumbers() throws Exception {
        context.put("number", 555123456);

        Match instance = new Match("number", "555.*");
        assertTrue(instance.isTruthValue(context));

        instance = new Match("number", ".*123.*");
        assertTrue(instance.isTruthValue(context));

        instance = new Match("number", "9.*");
        assertFalse(instance.isTruthValue(context));

        instance = new Match("number", null);
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanMatchDates() throws Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("15/01/2009");
        context.put("property", date);

        Match instance = new Match("property", ".*2009.*");
        assertTrue(instance.isTruthValue(context));

        instance = new Match("property", ".*2020.*");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanMatchCalendars() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 0, 15); // month value is 0-based
        context.put("property", calendar);

        Match instance = new Match("property", ".*2009.*");
        assertTrue(instance.isTruthValue(context));

        instance = new Match("property", ".*2020.*");
        assertFalse(instance.isTruthValue(context));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanMatchStrings() throws Exception {
        String s = "aaaaa";
        context.put("property", s);

        Match instance = new Match("property", "a*");
        assertTrue(instance.isTruthValue(context));
    }
}
