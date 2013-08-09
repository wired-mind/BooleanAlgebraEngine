package com.wiredmind.booleanengine.relations;

/**
 * TestComparator.java
 * <p/>
 * A Simple java.lang.String Comparator.
 * <p/>
 * Created by Craig Earley on 8/8/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class TestComparator implements Comparator<String> {
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
