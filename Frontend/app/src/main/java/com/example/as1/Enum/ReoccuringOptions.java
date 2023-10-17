package com.example.as1.Enum;

public enum ReoccuringOptions {
    NOREOCCURING(0),
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);


    private final int value;

    ReoccuringOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReoccuringOptions fromValue(int value) {
        for (ReoccuringOptions option : ReoccuringOptions.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Not Alloew: " + value);
    }
}
