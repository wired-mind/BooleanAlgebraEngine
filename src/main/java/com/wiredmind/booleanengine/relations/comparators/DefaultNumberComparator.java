package com.wiredmind.booleanengine.relations.comparators;

import com.wiredmind.booleanengine.relations.Comparator;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * DefaultNumberComparator.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class DefaultNumberComparator implements Comparator<Number> {

    @Override
    public int compare(Number lhs, CharSequence rhs) throws Exception {
        BigDecimal number = BigDecimal.valueOf(lhs.doubleValue());
        BigDecimal numberArg = new BigDecimal(String.valueOf(rhs));
        return numberArg.compareTo(number);
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