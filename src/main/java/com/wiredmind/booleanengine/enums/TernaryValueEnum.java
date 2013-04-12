package com.wiredmind.booleanengine.enums;

/**
 * Represents a ternary value.
 */
public enum TernaryValueEnum {

    UNKNOWN(null, "unknown"),
    TRUE(true, "true"),
    FALSE(false, "false");
    private final Object value;
    private final String description;

    TernaryValueEnum(Object value, String description) {
        this.value = value;
        this.description = description;
    }

    public Object getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
