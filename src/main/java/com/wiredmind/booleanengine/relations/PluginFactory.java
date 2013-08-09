package com.wiredmind.booleanengine.relations;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.HashMap;

/**
 * PluginFactory.java
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class PluginFactory {

    private static XMLConfiguration config = new XMLConfiguration();
    private static HashMap<Class<?>, Class<?>> comparators = new HashMap<Class<?>, Class<?>>();

    static {

        try {
            config.setDelimiterParsingDisabled(true);
            config.setAttributeSplittingDisabled(true);
            config.load("BooleanAlgebraEngine.cfg.xml");

            for (HierarchicalConfiguration plugin : config.configurationsAt("plugins.comparator")) {
                comparators.put(Class.forName(plugin.getString("type")),
                        Class.forName(plugin.getString("class")));
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Comparator getPluginComparator(Object obj) {
        for (Class<?> classType : comparators.keySet()) {
            if (classType.isInstance(obj)) {
                try {
                    return (Comparator) comparators.get(classType).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("factory unable to construct instance of "
                            + comparators.get(classType).getSimpleName());
                }
            }
        }
        return null;
    }
}
