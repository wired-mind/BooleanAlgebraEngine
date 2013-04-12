package com.wiredmind.booleanengine.core;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Assigns a truth value to a variable in a
 * {@link com.wiredmind.booleanengine.core.ValidatorAlgebra}.
 */
public class VariableValuationTask implements Callable<Integer>, Cell<Integer>, Serializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(VariableValuationTask.class);
    public final static long serialVersionUID = 1L;
    private Command evaluator;
    private Context cntxt;
    private Integer variable;
    private Integer value;

    public VariableValuationTask(Command evaluator, Context cntxt, Integer variable) {
        this.evaluator = evaluator;
        this.cntxt = cntxt;
        this.variable = variable;
    }

    @Override
    public Integer call() throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            LOGGER.debug("Task interrupted - executaion cancelled");
            throw new InterruptedException();
        }

        boolean result = evaluator.execute(cntxt);
        /*
         * Note: CONTINUE_PROCESSING is interpreted as 'true'
         * and PROCESSING_COMPLETE is interpreted as 'false'
         */
        if (result == Command.CONTINUE_PROCESSING) {
            setValue(variable); // True: Return the value of the variable
        } else {
            setValue(-variable); // False: Return its negation (0 - value)
        }
        LOGGER.debug("Truth value for variable '{}' is {}", variable, result == Command.CONTINUE_PROCESSING ? true : false);
        return value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
}
