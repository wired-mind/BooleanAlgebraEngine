package com.wiredmind.booleanengine.core.relations;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a property value equals a particular value.
 * 
 * 
 */
public class Eq extends AbstractBinaryRelation<String, String> {

    public final static long serialVersionUID = 1L;

    public Eq(String key, String val) {
        super(key, val);
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        switch (propertyType) {
            case DATE:
                Date dateArg = DateUtils.parseDate(val, DATE_PARSE_PATTERNS);
                if (dateArg.compareTo((Date) propertyValue) == 0) {
                    return CONTINUE_PROCESSING;
                }
                break;
            case NUMBER:
                BigDecimal number = new BigDecimal(propertyValue.toString());
                BigDecimal numberArg = new BigDecimal(val);
                if (numberArg.compareTo(number) == 0) {
                    return CONTINUE_PROCESSING;
                }
                break;
            case STRING:
                if (val.compareToIgnoreCase(propertyValue.toString()) == 0) {
                    return CONTINUE_PROCESSING;
                }
                break;
            default:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
        }
        return PROCESSING_COMPLETE;
    }
}
