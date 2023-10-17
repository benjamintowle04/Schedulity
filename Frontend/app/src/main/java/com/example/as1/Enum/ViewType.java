package com.example.as1.Enum;

public enum ViewType {
    Weekly(0),
    Monthly(1),
    Daily(2);


    private final int value;

    ViewType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
