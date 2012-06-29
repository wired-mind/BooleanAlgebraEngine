package com.wiredmind.booleanengine.core.relations;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a property value is between two distinct values.
 * 
 * 
 */
public class Between extends AbstractTernaryRelation<String, String, String> {

    public final static long serialVersionUID = 1L;

    public Between(String key, String left, String right) {
        super(key, left, right);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        switch (propertyType) {
            case DATE:
                Date date = (Date) propertyValue;
                Date dateBefore = DateUtils.parseDate(left, DATE_PARSE_PATTERNS);
                Date dateAfter = DateUtils.parseDate(right, DATE_PARSE_PATTERNS);
                if (dateBefore.compareTo(date) <= 0 && dateAfter.compareTo(date) >= 0) {
                    return CONTINUE_PROCESSING;
                }
                break;
            case NUMBER:
                BigDecimal number = new BigDecimal(propertyValue.toString());
                BigDecimal min = new BigDecimal(left);
                BigDecimal max = new BigDecimal(right);
                if (min.compareTo(number) <= 0 && max.compareTo(number) >= 0) {
                    return CONTINUE_PROCESSING;
                }
                break;
            default:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
        }
        return PROCESSING_COMPLETE;
    }
}
