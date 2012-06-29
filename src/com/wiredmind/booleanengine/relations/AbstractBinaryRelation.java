package com.wiredmind.booleanengine.core.relations;

import com.wiredmind.booleanengine.enums.PropertyTypeEnum;
import org.apache.commons.chain.Context;

/**
 * 
 * 
 */
public abstract class AbstractBinaryRelation<U, V> extends AbstractRelation {

    protected U key;
    protected V val;
    protected PropertyTypeEnum propertyType;
    protected Object propertyValue;

    protected AbstractBinaryRelation() {
    }

    public AbstractBinaryRelation(U key, V val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        propertyValue = cntxt.get(key);
        propertyType = PropertyTypeEnum.getPropertyType(propertyValue);
        return CONTINUE_PROCESSING;
    }
}
