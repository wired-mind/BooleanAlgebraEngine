package com.wiredmind.booleanengine.relations.comparators;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertTrue;

/**
 * DefaultDateComparatorTest
 * Created by Craig Earley on 9/24/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultDateComparatorTest {

    @Test
    public void testCompare() throws Exception {

        DefaultDateComparator comparator = new DefaultDateComparator();

        DateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        gmtFormat.setTimeZone(gmtTime);
        Date date = gmtFormat.parse("2013-09-01T00:00:00-0000");

        // Assume GMT/UTC
        assertTrue(comparator.compare(date, "2013-09-01") == 0);
    }

    @Test
    public void testCompare2() throws Exception {

        DefaultDateComparator comparator = new DefaultDateComparator();

        DateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = gmtFormat.parse("2013-09-01T00:00:00-0400");

        // Specific tz
        assertTrue(comparator.compare(date, "2013-09-01T00:00:00-0400") == 0);
    }
}
