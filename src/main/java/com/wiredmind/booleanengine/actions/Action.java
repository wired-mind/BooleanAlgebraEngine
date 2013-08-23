package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.relations.Relation;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Action.java
 * <p/>
 * Interface to Action commands. An Action is regarded
 * as a one-place (unary) relation since it can be viewed
 * as an individual having the property, true or false,
 * <i>after</i> some action or unit of work is performed.
 * <p/>
 * It is recommended that an Action implements Serializable.
 * <p/>
 * Created by Craig Earley on 8/23/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public interface Action extends Relation {

    public Action When(String relation, String arguments);

    public Action When(Command command);

    boolean performAction(Context cntxt) throws Exception;
}
