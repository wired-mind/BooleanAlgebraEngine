package com.wiredmind.booleanengine.relations.comparators;

import com.wiredmind.booleanengine.relations.Comparator;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;

/**
 * DefaultCalendarComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultCalendarComparator implements Comparator<Calendar> {
    private static final String[] DATE_PARSE_PATTERNS = new String[]{"yyMMdd", "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};
    private static final Calendar calendar = Calendar.getInstance(); // TODO: Is this thread-safe?

    @Override
    public int compare(Calendar lhs, CharSequence rhs) throws Exception {
        calendar.setTime(DateUtils.parseDate(String.valueOf(rhs), DATE_PARSE_PATTERNS));
        return calendar.compareTo(DateUtils.truncate(lhs, Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean contains(Calendar obj, CharSequence cs) throws Exception {
        return compare(obj, cs) == 0;
    }

    @Override
    public boolean matches(Calendar obj, String regularExpression) throws Exception {
        return String.valueOf(obj).matches(regularExpression);
    }
}
