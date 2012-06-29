package com.wiredmind.booleanengine.core.relations;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a property is contained within a specified list of values.
 *
 * 
 */
public class In extends AbstractBinaryRelation<String, String> {

    public final static long serialVersionUID = 1L;
    private String[] vals;

    public In(String key, String val) {
        super(key, null);
        vals = val.split(",");
    }

    public In(String key, String... vals) {
        super(key, null);
        this.vals = vals;
    }

    @Override
    public boolean execute(Context cntxt) throws Exception {
        super.execute(cntxt);

        switch (propertyType) {
            case DATE:
                Date date = (Date) propertyValue;
                for (int i = 0; i < vals.length; i++) {
                    Date dateArg = DateUtils.parseDate(vals[i], DATE_PARSE_PATTERNS);
                    if (dateArg.compareTo(date) == 0) {
                        return CONTINUE_PROCESSING;
                    }
                }
                return PROCESSING_COMPLETE;
            case NUMBER:
                BigDecimal number = new BigDecimal(propertyValue.toString());
                for (int i = 0; i < vals.length; i++) {
                    BigDecimal numberArg = new BigDecimal(vals[i]);
                    if (numberArg.compareTo(number) == 0) {
                        return CONTINUE_PROCESSING;
                    }
                }
                return PROCESSING_COMPLETE;
            case STRING:
                String s = propertyValue.toString();
                for (int i = 0; i < vals.length; i++) {
                    if (vals[i].compareToIgnoreCase(s) == 0) {
                        return CONTINUE_PROCESSING;
                    }
                }
                return PROCESSING_COMPLETE;
            default:
                LoggerFactory.getLogger(this.getClass().getName()).warn("Cannot test {} for {}", this.getClass().getSimpleName(), propertyType.getDescription());
        }
        return PROCESSING_COMPLETE;
    }
}
