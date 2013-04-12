package com.wiredmind.booleanengine.relations;

/**
 * Provides properties and methods common to binary relations.
 */
abstract class AbstractBinaryRelation<K, V> extends AbstractRelation {

    final K key;
    final V val;

    AbstractBinaryRelation(K key, V val) {
        this.key = key;
        this.val = val;
    }
}
