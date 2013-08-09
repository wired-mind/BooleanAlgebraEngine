package com.wiredmind.booleanengine.relations.comparators;

import com.wiredmind.booleanengine.relations.Comparator;

/**
 * DefaultStringComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultStringComparator implements Comparator<String> {

    @Override
    public int compare(String lhs, CharSequence rhs) throws Exception {
        return String.valueOf(rhs).compareToIgnoreCase(lhs);
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