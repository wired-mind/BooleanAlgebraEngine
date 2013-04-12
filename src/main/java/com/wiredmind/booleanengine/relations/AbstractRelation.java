package com.wiredmind.booleanengine.relations;

import com.wiredmind.booleanengine.enums.PropertyTypeEnum;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Base class for all relations.
 */
public abstract class AbstractRelation implements Relation, Serializable {

    private static final String[] DATE_PARSE_PATTERNS = new String[]{"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};
    private static final Calendar calendar = Calendar.getInstance(); // TODO: Is this thread-safe?
    PropertyTypeEnum propertyType;
    Object propertyValue;
    boolean truthValue;

    @Override
    public abstract boolean execute(Context context) throws Exception;

    @Override
    public boolean isTruthValue() {
        return truthValue;
    }

    /**
     * Sets the <code>propertyValue</code> and its
     * <code>propertyType</code> value from the context
     * using the provided key.
     *
     * @param context The context
     * @param key     The attribute of the context
     */
    void setPropertyValue(Context context, Object key) {

        propertyValue = context.get(key);

        propertyType = PropertyTypeEnum.getPropertyType(propertyValue);
    }

    /**
     * Compares the property value with a particular value.
     *
     * @param val the particular value
     * @return a negative integer, zero, or a positive integer
     *         as the property value is greater than, equal to, or less
     *         than the value represented by the argument, ignoring case
     *         considerations for strings.
     * @throws Exception
     */
    int comparePropertyTo(String val) throws Exception {
        switch (propertyType) {
            case CALENDAR:
                calendar.setTime(DateUtils.parseDate(val, DATE_PARSE_PATTERNS));
                return calendar.compareTo(DateUtils.truncate((Calendar) propertyValue, Calendar.DAY_OF_MONTH));
            case NUMBER:
                BigDecimal number = new BigDecimal(propertyValue.toString());
                BigDecimal numberArg = new BigDecimal(val);
                return numberArg.compareTo(number);
            case STRING:
                return val.compareToIgnoreCase(propertyValue.toString());
            case DATE:
                Date dateArg = DateUtils.parseDate(val, DATE_PARSE_PATTERNS);
                return dateArg.compareTo((Date) propertyValue);
            default:
                throw new IllegalArgumentException(propertyType.getDescription());
        }
    }

    /**
     * Checks if the property value contains a particular value, handling
     * <code>null</code>. For numbers and strings a substring match is
     * performed, ignoring case considerations. A date (or calendar) property
     * value contains a particular value only when <code>comparePropertyTo(val)
     * == 0</code>.
     *
     * @param val the particular value
     * @return true if the property value contains the particular value,
     *         false if not or <code>null</code> string input
     * @throws Exception
     */
    boolean propertyContains(String val) throws Exception {
        if (val == null) {
            return false;
        }

        switch (propertyType) {
            case NUMBER:
                String numberStr = (new BigDecimal(propertyValue.toString())).toPlainString();
                return StringUtils.contains(numberStr.toLowerCase(), val.toLowerCase());
            case STRING:
                String s = propertyValue.toString();
                return StringUtils.contains(s.toLowerCase(), val.toLowerCase());
            case CALENDAR:
                return comparePropertyTo(val) == 0;
            case DATE:
                return comparePropertyTo(val) == 0;
            default:
                throw new IllegalArgumentException(propertyType.getDescription());
        }
    }
}
