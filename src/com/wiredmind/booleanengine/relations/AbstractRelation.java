package com.wiredmind.booleanengine.core.relations;

import java.io.Serializable;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 *  
 * 
 */
public abstract class AbstractRelation implements Command, Serializable {

    public static String[] DATE_PARSE_PATTERNS = new String[]{"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};

    @Override
    public abstract boolean execute(Context cntxt) throws Exception;
}
