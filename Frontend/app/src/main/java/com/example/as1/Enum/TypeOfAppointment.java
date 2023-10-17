package com.example.as1.Enum;

public enum TypeOfAppointment {
    COURSE(0),
    SLEEPTIME(1),
    APPOINTMENT(2);

    private final int value;

    TypeOfAppointment(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
