package com.wiredmind.booleanengine.enums;

/**
 * Represents general property types.
 */
public enum PropertyTypeEnum {

    UNKNOWN("Unknown property type"),
    CALENDAR("Calendar type"),
    DATE("Date type"),
    NUMBER("Number type"),
    STRING("String type");
    private final String description;

    PropertyTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the general property type.
     *
     * @param val
     * @return
     */
    public static PropertyTypeEnum getPropertyType(Object val) {
        PropertyTypeEnum typeEnum = PropertyTypeEnum.UNKNOWN;

        if (val instanceof java.lang.Number) {
            typeEnum = PropertyTypeEnum.NUMBER;
        } else if (val instanceof java.util.Calendar) {
            typeEnum = PropertyTypeEnum.CALENDAR;
        } else if (val instanceof java.lang.String) {
            typeEnum = PropertyTypeEnum.STRING;
        } else if (val instanceof java.util.Date) {
            typeEnum = PropertyTypeEnum.DATE;
        } else if (val instanceof java.lang.Boolean) {
            typeEnum = PropertyTypeEnum.NUMBER;
        }
        return typeEnum;
    }
}
