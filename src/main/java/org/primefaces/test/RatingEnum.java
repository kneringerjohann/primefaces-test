package org.primefaces.test;

import lombok.Getter;

@Getter
public enum RatingEnum {

    STANDARD("s", "Standard"),
    BESTEST("d", "Bestest"),
    BETTER("u", "Better");

    private final String dbValue;
    private final String label;

    RatingEnum(String dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    public static RatingEnum fromDbValue(String dbValue) {
        for (RatingEnum e : values()) {
            if (e.dbValue.equals(dbValue)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbValue);
    }
}
