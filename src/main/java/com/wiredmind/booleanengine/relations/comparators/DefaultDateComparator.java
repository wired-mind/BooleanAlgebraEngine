package com.wiredmind.booleanengine.relations.comparators;

import org.apache.commons.lang.time.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * DefaultDateComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultDateComparator implements Comparator<Date>, Serializable {

    private static final String[] DATE_PARSE_PATTERNS = new String[]{"yyMMdd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ssZ", "dd/MM/yyyy'T'HH:mm:ssZ", "MM/dd/yyyy'T'HH:mm:ssZ"};

    @Override
    public int compare(Date lhs, Date rhs) throws Exception {
        return rhs.compareTo(lhs);
    }

    @Override
    public int compare(Date lhs, CharSequence rhs) throws Exception {
        String dateString = String.valueOf(rhs);

        String[] parts = dateString.split("T");
        if (parts.length == 1)
            dateString = String.format("%sT00:00:00-0000", parts[0]); // GMT/UTC time

        Date date = DateUtils.parseDate(String.valueOf(dateString), DATE_PARSE_PATTERNS);
        return compare(lhs, date);
    }

    @Override
    public boolean contains(Date obj, CharSequence cs) throws Exception {
        return compare(obj, cs) == 0;
    }

    @Override
    public boolean matches(Date obj, String regularExpression) throws Exception {
        return String.valueOf(obj).matches(regularExpression);
    }
}