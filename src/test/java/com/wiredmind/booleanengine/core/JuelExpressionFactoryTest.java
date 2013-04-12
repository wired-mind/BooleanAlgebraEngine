package com.wiredmind.booleanengine.core;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class JuelExpressionFactoryTest {

    public final static ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ParseException {
    }

    @Test
    public void testStateRegistration() {
        de.odysseus.el.util.SimpleContext context = new de.odysseus.el.util.SimpleContext();

        context.setVariable("A", factory.createValueExpression(false, Boolean.class));
        context.setVariable("B", factory.createValueExpression(true, Boolean.class));
        context.setVariable("C", factory.createValueExpression(true, Boolean.class));

        ValueExpression e = factory.createValueExpression(context, "${(A or B) and C}", Boolean.class);

        Boolean result = (Boolean) e.getValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testBehaviourRegistration() throws NoSuchMethodException {
        de.odysseus.el.util.SimpleContext context = new de.odysseus.el.util.SimpleContext();

        // map function math:max(int, int) to java.lang.Math.max(int, int)
        context.setFunction("math", "max", Math.class.getMethod("max", int.class, int.class));
        // map variable foo to 0
        context.setVariable("foo", factory.createValueExpression(0, int.class));

        // parse our expression
        ValueExpression e = factory.createValueExpression(context, "${math:max(foo,bar)}", int.class);
        // set value for top-level property "bar" to 1
        factory.createValueExpression(context, "${bar}", int.class).setValue(context, 1);
        // get value for our expression
        System.out.println(e.getValue(context)); // --> 1


        assertTrue(e.getValue(context).equals(1));
    }
}
