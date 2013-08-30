package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.actions.Action;
import com.wiredmind.booleanengine.relations.comparators.Comparator;
import com.wiredmind.booleanengine.relations.plugins.PercentUpdate;
import com.wiredmind.booleanengine.relations.plugins.SimpleUpdate;
import com.wiredmind.booleanengine.relations.plugins.TestComparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * PluginFactoryTest
 * Created by Craig Earley on 8/22/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class PluginFactoryTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCanRegisterAction() throws Exception {

        PluginFactory.RegisterAction("myAction", PercentUpdate.class);

        Action action = PluginFactory.getAction("myAction", "2");
        assertEquals(action.getClass(), PercentUpdate.class);
    }

    @Test
    public void testCanRegisterComparator() throws Exception {

        PluginFactory.RegisterComparator(String.class, TestComparator.class);

        Comparator comparator = PluginFactory.getComparator("string");

        assertEquals(comparator.getClass(), TestComparator.class);
    }

    @Test
    public void testGetAction() throws Exception {

        Action action = PluginFactory.getAction("percentAward", "2");
        assertEquals(action.getClass(), PercentUpdate.class);

        action = PluginFactory.getAction("award", "5.00");
        assertEquals(action.getClass(), SimpleUpdate.class);
    }

    @Test
    public void testGetPluginComparator() throws Exception {

        Comparator comparator = PluginFactory.getComparator("string");

        assertEquals(comparator.getClass(), TestComparator.class);
    }
}
