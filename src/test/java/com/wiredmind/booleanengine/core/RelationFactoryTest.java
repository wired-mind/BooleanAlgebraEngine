package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.relations.plugins.PercentUpdate;
import com.wiredmind.booleanengine.relations.plugins.SimpleUpdate;
import com.wiredmind.booleanengine.relations.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * RelationFactoryTest
 * Created by Craig Earley on 8/16/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class RelationFactoryTest {

    public RelationFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCreate() throws Exception {

        Relation relation = RelationFactory.create("between", "4 3 5");
        assertEquals(relation.getClass(), Between.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("eq", "4 4");
        assertEquals(relation.getClass(), Eq.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("false", "");
        assertEquals(relation.getClass(), False.class);
        assertTrue(relation.isTruthValue(null) == false);

        relation = RelationFactory.create("ge", "4 4");
        assertEquals(relation.getClass(), Ge.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("gt", "4 3");
        assertEquals(relation.getClass(), Gt.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("in", "4 4, 5");
        assertEquals(relation.getClass(), In.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("le", "4 4");
        assertEquals(relation.getClass(), Le.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("like", "444 4");
        assertEquals(relation.getClass(), Like.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("lt", "4 5");
        assertEquals(relation.getClass(), Lt.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("match", "2013 .*13.*");
        assertEquals(relation.getClass(), Match.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("true", "");
        assertEquals(relation.getClass(), True.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("percentAward", "5");
        assertEquals(relation.getClass(), PercentUpdate.class);
        assertTrue(relation.isTruthValue(null));

        relation = RelationFactory.create("award", "10.00");
        assertEquals(relation.getClass(), SimpleUpdate.class);
        assertTrue(relation.isTruthValue(null));
    }
}
