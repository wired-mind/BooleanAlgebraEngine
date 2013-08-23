package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.actions.Action;
import com.wiredmind.booleanengine.relations.comparators.*;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * PluginFactory.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class PluginFactory {

    private final static Logger LOGGER = Logger.getLogger(PluginFactory.class.getName());
    private static XMLConfiguration config = new XMLConfiguration();
    private static HashMap<String, Class<?>> pluginActions = new HashMap<String, Class<?>>();
    private static HashMap<Class<?>, Class<?>> pluginComparators = new HashMap<Class<?>, Class<?>>();
    private static Map<Class<?>, Comparator<?>> defaultComparators = new HashMap<Class<?>, Comparator<?>>();

    static {
        // Load default Comparator instances
        defaultComparators.put(java.lang.Number.class, new DefaultNumberComparator());
        defaultComparators.put(java.util.Calendar.class, new DefaultCalendarComparator());
        defaultComparators.put(java.lang.String.class, new DefaultStringComparator());
        defaultComparators.put(java.util.Date.class, new DefaultDateComparator());
        defaultComparators.put(java.lang.Boolean.class, new DefaultNumberComparator());

        try {
            config.setDelimiterParsingDisabled(true);
            config.setAttributeSplittingDisabled(true);
            config.load("BooleanAlgebraEngine.cfg.xml");

            for (HierarchicalConfiguration plugin : config.configurationsAt("plugins.actions.action")) {
                pluginActions.put(plugin.getString("name"),
                        Class.forName(plugin.getString("class")));
            }

            for (HierarchicalConfiguration plugin : config.configurationsAt("plugins.comparators.comparator")) {
                pluginComparators.put(Class.forName(plugin.getString("type")),
                        Class.forName(plugin.getString("class")));
            }
        } catch (Exception e) {
            LOGGER.severe(new ExceptionInInitializerError(e).toString());
        }
    }

    public static Action getAction(String name, String val) {
        // Return plugin Action or null
        for (String key : pluginActions.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                try {
                    Class<?> clazz = pluginActions.get(key);
                    Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class);
                    return (Action) declaredConstructor.newInstance(val);
                } catch (Exception e) {
                    throw new RuntimeException("factory unable to construct instance of "
                            + pluginActions.get(key).getSimpleName());
                }
            }
        }
        LOGGER.warning(String.format("No suitable Action found for %s", name));
        return null;
    }

    public static Comparator getComparator(Object obj) {
        // Return plugin Comparator if found
        for (Class<?> classType : pluginComparators.keySet()) {
            if (classType.isInstance(obj)) {
                try {
                    Class<?> clazz = pluginComparators.get(classType);
                    Constructor declaredConstructor = clazz.getDeclaredConstructor();
                    return (Comparator) declaredConstructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("factory unable to construct instance of "
                            + pluginComparators.get(classType).getSimpleName());
                }
            }
        }
        // Return default Comparator or null
        for (Class<?> classType : defaultComparators.keySet()) {
            if (classType.isInstance(obj)) {
                return defaultComparators.get(classType);
            }
        }
        LOGGER.warning(String.format("No suitable Comparator found for %s", obj.getClass().getSimpleName()));
        return null;
    }
}
