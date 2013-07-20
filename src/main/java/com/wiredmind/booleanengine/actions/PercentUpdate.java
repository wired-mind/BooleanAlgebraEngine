package com.wiredmind.booleanengine.actions;

import org.apache.commons.chain.Context;

import java.math.BigDecimal;

/**
 * Update the Award amount by the specified amount as a percentage
 * of the transaction amount.
 */
public class PercentUpdate extends UpdateBase<Object, String> {

    public final static long serialVersionUID = 1L;

    public PercentUpdate(Object key, String amount) {
        super(key, amount);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {

        if (null == applicabilityMember || isApplicable) {

            BigDecimal percentage = new BigDecimal(val).movePointLeft(2);
            BigDecimal transactionAmount = new BigDecimal(cntxt.get("Amount").toString());

            cntxt.put(key, new BigDecimal(transactionAmount.multiply(percentage).doubleValue()).toString());
        }
        truthValue = true;
        return CONTINUE_PROCESSING;
    }

    @Override
    public boolean isTruthValue(Context context) throws Exception {
        execute(context);
        return truthValue;
    }
}
