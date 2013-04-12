package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.domain.Award;
import org.apache.commons.chain.Context;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Update the Award amount by the specified amount as a percentage
 * of the transaction amount.
 */
public class PercentUpdate extends UpdateBase implements Serializable {

    public PercentUpdate(String amount) {
        super(amount);
    }

    public PercentUpdate(String amount, String description) {
        super(amount, description);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        if (null == applicabilityMember || isApplicable) {
            Award award = (Award) cntxt.get(Award.AWARD_KEY);
            if (null == award) {
                return PROCESSING_COMPLETE;
            }
            BigDecimal transactionAmount = new BigDecimal(cntxt.get("Amount").toString());
            BigDecimal percentage = this.amount.movePointLeft(2);

            award.setAmount(transactionAmount.multiply(percentage).doubleValue());
            award.setDescription(this.description);
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
