package com.wiredmind.booleanengine.relations;

/**
 * Provides properties and methods common to ternary relations.
 */
public abstract class AbstractTernaryRelation<K, L, R> extends AbstractRelation {

    protected final K key;
    protected final L lhs;
    protected final R rhs;

    public AbstractTernaryRelation(K key, L lhs, R rhs) {
        this.key = key;
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
