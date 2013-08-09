package com.wiredmind.booleanengine.relations;

import com.wiredmind.booleanengine.relations.comparators.DefaultCalendarComparator;
import com.wiredmind.booleanengine.relations.comparators.DefaultDateComparator;
import com.wiredmind.booleanengine.relations.comparators.DefaultNumberComparator;
import com.wiredmind.booleanengine.relations.comparators.DefaultStringComparator;
import org.apache.commons.chain.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all relations.
 */
public abstract class AbstractRelation implements Relation, Serializable {

    private static Map<Class<?>, Comparator<?>> defaultComparators = new HashMap<Class<?>, Comparator<?>>();
    protected Comparator<Object> comparator;
    protected Object propertyValue;
    protected boolean truthValue;

    static {
        // Load default Comparator instances
        defaultComparators.put(java.lang.Number.class, new DefaultNumberComparator());
        defaultComparators.put(java.util.Calendar.class, new DefaultCalendarComparator());
        defaultComparators.put(java.lang.String.class, new DefaultStringComparator());
        defaultComparators.put(java.util.Date.class, new DefaultDateComparator());
        defaultComparators.put(java.lang.Boolean.class, new DefaultNumberComparator());
    }

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

        propertyValue = context.get(key);

        comparator = getComparator();
    }

    private Comparator getComparator() {
        // Look for plugin Comparator for propertyValue
        Comparator<?> pluginComparator = PluginFactory.getPluginComparator(propertyValue);

        if (pluginComparator != null)
            return pluginComparator;

        // Return default Comparator or null
        for (Class<?> classType : defaultComparators.keySet()) {
            if (classType.isInstance(propertyValue)) {
                return defaultComparators.get(classType);
            }
        }
        return null;
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
