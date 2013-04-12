package com.wiredmind.booleanengine.logic;

/**
 * Thrown by a {@link FormulaBuilder} when an expression
 * is malformed.
 */
public class BadExpressionException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadExpressionException(Throwable cause) {
        super(cause);
    }

    public BadExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadExpressionException(String message) {
        super(message);
    }

    public BadExpressionException() {
    }
}
