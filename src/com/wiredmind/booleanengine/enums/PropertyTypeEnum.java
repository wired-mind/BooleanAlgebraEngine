package com.wiredmind.booleanengine.enums;

/**
 * Represents general property types.
 *
 * 
 */
public enum PropertyTypeEnum {

    UNKNOWN(null, "Unknown property type"),
    DATE("Date", "Date type"),
    NUMBER("Number", "Number type"),
    STRING("String", "String type");
    private final String code;
    private final String description;

    PropertyTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the general property type.
     * @param val
     * @return
     */
    public static PropertyTypeEnum getPropertyType(Object val) {
        PropertyTypeEnum typeEnum = PropertyTypeEnum.UNKNOWN;

        if (val instanceof java.lang.Number) {
            typeEnum = PropertyTypeEnum.NUMBER;
        } else if (val instanceof java.util.Date) {
            typeEnum = PropertyTypeEnum.DATE;
        } else if (val instanceof java.lang.String) {
            typeEnum = PropertyTypeEnum.STRING;
        }
        return typeEnum;
    }
}
