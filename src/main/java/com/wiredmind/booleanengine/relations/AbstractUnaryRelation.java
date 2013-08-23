package com.wiredmind.booleanengine.relations;

/**
 * AbstractUnaryRelation.java
 * <p/>
 * Provides properties and methods common to unary (one-place) relations.
 * <p/>
 * Created by Craig Earley on 8/16/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public abstract class AbstractUnaryRelation<V> extends AbstractRelation {

    protected final V val;

    public AbstractUnaryRelation(V val) {
        this.val = val;
    }
}
