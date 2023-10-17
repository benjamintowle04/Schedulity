package com.example.as1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.R;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    User user;
    List<Appointment> appointments;

    public AppointmentAdapter(@NonNull Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
        user = User.getUser(context);
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointment_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.AppointmentCellTV);
        String tempTime = appointments.get(position).getStartTime();
        String tempEndTime = appointments.get(position).getEndTime();
        String eventTitle = appointments.get(position).getName() + " " + tempTime + " - " + tempEndTime;
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}
