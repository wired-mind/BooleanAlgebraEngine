package com.wiredmind.booleanengine.actions;

import com.wiredmind.booleanengine.domain.Award;
import org.apache.commons.chain.Context;

import java.io.Serializable;

/**
 * Update the Award amount by the specified amount as a simple value.
 */
public class SimpleUpdate extends UpdateBase implements Serializable {

    public SimpleUpdate(String amount) {
        super(amount);
    }

    public SimpleUpdate(String amount, String description) {
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

            award.setAmount(this.amount.doubleValue());
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
