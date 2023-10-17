package com.example.as1.Enum;

public enum LoggedInStates {
    NOT_LOGGED_IN(0),
    LOGGED_IN(1);


    private final int value;

    LoggedInStates(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
