package com.wiredmind.booleanengine.relations.plugins;

import com.wiredmind.booleanengine.actions.ActionBase;
import org.apache.commons.chain.Context;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Update the Award amount by the specified amount as a percentage
 * of the transaction amount.
 */
public class PercentUpdate extends ActionBase<String> implements Serializable {

    public final static long serialVersionUID = 1L;

    public PercentUpdate(String amount) {
        super(amount);
    }

    @Override
    public boolean performAction(Context cntxt) throws Exception {
        BigDecimal percentage = new BigDecimal(val).movePointLeft(2);
        BigDecimal transactionAmount = cntxt != null ? new BigDecimal(cntxt.get("Amount").toString()) : new BigDecimal(0);

        if (cntxt != null)
            cntxt.put("award", new BigDecimal(transactionAmount.multiply(percentage).doubleValue()).toString());

        return true;
    }
}
