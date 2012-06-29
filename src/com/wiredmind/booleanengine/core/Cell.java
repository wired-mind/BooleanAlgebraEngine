package com.wiredmind.booleanengine.core;

/**
 * Represents the valuation of a variable in a
 * {@link com.wiredmind.booleanengine.core.ValidatorAlgebra}.
 * 
 * 
 */
public interface Cell<V> {

    V getValue();

    void setValue(V value);
}
