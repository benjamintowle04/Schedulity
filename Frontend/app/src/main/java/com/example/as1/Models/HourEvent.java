package com.example.as1.Models;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent
{
    LocalTime startTime;
    LocalTime endTime;
    public ArrayList<Appointment> appointments;

    public HourEvent(LocalTime time, ArrayList<Appointment> appointments)
    {
        this.startTime = time;
        this.appointments = appointments;
    }

    public LocalTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalTime startTime)
    {
        this.startTime = startTime;
    }    public LocalTime getEndTime()
{
    return endTime;
}

    public void setEndTime(LocalTime endTime)
    {
        this.endTime = endTime;
    }

    public ArrayList<Appointment> getAppointments()
    {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments)
    {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointments)
    {
        this.appointments.add(appointments);
    }
}