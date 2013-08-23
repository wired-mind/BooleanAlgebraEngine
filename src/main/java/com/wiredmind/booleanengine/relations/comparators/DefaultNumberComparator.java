package com.wiredmind.booleanengine.relations.comparators;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DefaultNumberComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultNumberComparator implements Comparator<Number>, Serializable {

    @Override
    public int compare(Number lhs, Number rhs) throws Exception {
        BigDecimal l = BigDecimal.valueOf(lhs.doubleValue());
        BigDecimal r = BigDecimal.valueOf(rhs.doubleValue());
        return r.compareTo(l);
    }

    @Override
    public int compare(Number lhs, CharSequence rhs) throws Exception {
        BigDecimal l = BigDecimal.valueOf(lhs.doubleValue());
        BigDecimal r = new BigDecimal(String.valueOf(rhs));
        return r.compareTo(l);
    }

    @Override
    public boolean contains(Number obj, CharSequence cs) throws Exception {
        String numberStr = (BigDecimal.valueOf(obj.doubleValue())).toPlainString();
        return StringUtils.contains(numberStr.toLowerCase(), String.valueOf(cs).toLowerCase());
    }

    @Override
    public boolean matches(Number obj, String regularExpression) throws Exception {
        String numberStr = (BigDecimal.valueOf(obj.doubleValue())).toPlainString();
        return numberStr.matches(regularExpression);
    }
}