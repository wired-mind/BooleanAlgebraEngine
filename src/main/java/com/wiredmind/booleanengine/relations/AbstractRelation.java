package com.wiredmind.booleanengine.relations;

import com.wiredmind.booleanengine.core.PluginFactory;
import com.wiredmind.booleanengine.relations.comparators.Comparator;
import org.apache.commons.chain.Context;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Base class for all relations.
 */
public abstract class AbstractRelation implements Relation, Serializable {

    private final static Logger LOGGER = Logger.getLogger(AbstractRelation.class.getName());
    protected Comparator<Object> comparator;
    protected Object propertyValue;
    protected boolean truthValue;

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
    protected void setPropertyValue(Context context, Object key) {

        if (context != null) {
            propertyValue = context.get(key);
        } else {
            LOGGER.warning("null context");
            propertyValue = key;
        }

        comparator = PluginFactory.getComparator(propertyValue);
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
        return comparator.compare(propertyValue, val);
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
        return comparator.contains(propertyValue, val);
    }
}
