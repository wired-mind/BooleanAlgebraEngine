package com.wiredmind.booleanengine.relations.comparators;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * DefaultBooleanComparator.java
 * <p/>
 * Created by Craig Earley on 9/6/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultBooleanComparator implements Comparator<Boolean>, Serializable {

    @Override
    public int compare(Boolean lhs, Boolean rhs) throws Exception {
        return rhs.compareTo(lhs);
    }

    @Override
    public int compare(Boolean lhs, CharSequence rhs) throws Exception {
        boolean r = Boolean.parseBoolean(String.valueOf(rhs));
        return this.compare(lhs, r);
    }

    @Override
    public boolean contains(Boolean obj, CharSequence cs) throws Exception {
        String booleanStr = obj.toString(); // "true" or "false"
        return StringUtils.contains(booleanStr, String.valueOf(cs).toLowerCase());
    }

    @Override
    public boolean matches(Boolean obj, String regularExpression) throws Exception {
        String booleanStr = obj.toString(); // "true" or "false"
        return booleanStr.matches(regularExpression);
    }
}
