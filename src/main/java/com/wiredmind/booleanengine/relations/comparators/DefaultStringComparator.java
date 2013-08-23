package com.wiredmind.booleanengine.relations.comparators;

import java.io.Serializable;

/**
 * DefaultStringComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultStringComparator implements Comparator<String>, Serializable {

    @Override
    public int compare(String lhs, String rhs) throws Exception {
        return rhs.compareToIgnoreCase(lhs);
    }

    @Override
    public int compare(String lhs, CharSequence rhs) throws Exception {
        return compare(lhs, String.valueOf(rhs));
    }

    @Override
    public boolean contains(String obj, CharSequence cs) throws Exception {
        return obj.toLowerCase().contains(String.valueOf(cs).toLowerCase());
    }

    @Override
    public boolean matches(String obj, String regularExpression) throws Exception {
        return obj.matches(regularExpression);
    }
}