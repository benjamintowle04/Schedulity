package com.example.as1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as1.Models.Appointment;
import com.example.as1.Models.HourEvent;
import com.example.as1.R;
import com.example.as1.Utils.CalendarUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent> {
    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents) {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HourEvent hourEvent = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);
        }

        setHour(convertView, hourEvent.getStartTime());
        setAppointments(convertView, hourEvent.getAppointments());

        // set click listener on convertView
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Selected Time: " + CalendarUtils.formattedShortTime(hourEvent.getStartTime()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(WeeklyViewActivity.this, AppointmentEditActivity.class);
//                intent.putExtra("appointment", hourEvent);
//                startActivity(intent);
            }
        });

        return convertView;
    }


    private void setHour(View convertView, LocalTime time) {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setAppointments(View convertView, ArrayList<Appointment> appointments) {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        if (appointments.size() == 0) {
            hideAppointment(event1);
            hideAppointment(event2);
            hideAppointment(event3);
        } else if (appointments.size() == 1) {
            setAppointment(event1, appointments.get(0));
            hideAppointment(event2);
            hideAppointment(event3);
        } else if (appointments.size() == 2) {
            setAppointment(event1, appointments.get(0));
            setAppointment(event2, appointments.get(1));
            hideAppointment(event3);
        } else if (appointments.size() == 3) {
            setAppointment(event1, appointments.get(0));
            setAppointment(event2, appointments.get(1));
            setAppointment(event3, appointments.get(2));
        } else {
            setAppointment(event1, appointments.get(0));
            setAppointment(event2, appointments.get(1));
            event3.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(appointments.size() - 2);
            eventsNotShown += " More Events";
            event3.setText(eventsNotShown);
        }
    }

    private void setAppointment(TextView textView, Appointment event) {
        if (event.getName() != null) {
            textView.setText(event.getName());
        }
        textView.setVisibility(View.VISIBLE);
    }

    private void hideAppointment(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

}




