package com.example.as1.Enum;

public enum AccountType {
    NORMAL_USER(1),
    PARENT_USER(2),
    ADVISER(3);

    private final int value;

    AccountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
