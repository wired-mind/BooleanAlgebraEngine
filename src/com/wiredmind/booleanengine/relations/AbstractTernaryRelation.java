package com.wiredmind.booleanengine.core.relations;

import com.wiredmind.booleanengine.enums.PropertyTypeEnum;
import org.apache.commons.chain.Context;

/**
 * 
 * 
 */
public abstract class AbstractTernaryRelation<U, V, W> extends AbstractRelation {

    protected U key;
    protected V left;
    protected W right;
    protected PropertyTypeEnum propertyType;
    protected Object propertyValue;

    public AbstractTernaryRelation(U key, V left, W right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        propertyValue = cntxt.get(key);
        propertyType = PropertyTypeEnum.getPropertyType(propertyValue);
        return CONTINUE_PROCESSING;
    }
}
