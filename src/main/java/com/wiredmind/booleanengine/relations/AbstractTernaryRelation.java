package com.wiredmind.booleanengine.relations;

/**
 * Provides properties and methods common to ternary relations.
 */
abstract class AbstractTernaryRelation<K, L, R> extends AbstractRelation {

    final K key;
    final L lhs;
    final R rhs;

    AbstractTernaryRelation(K key, L lhs, R rhs) {
        this.key = key;
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
