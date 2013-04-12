package com.wiredmind.booleanengine.relations;

/**
 * Provides properties and methods common to binary relations.
 */
public abstract class AbstractBinaryRelation<K, V> extends AbstractRelation {

    protected final K key;
    protected final V val;

    public AbstractBinaryRelation(K key, V val) {
        this.key = key;
        this.val = val;
    }
}
