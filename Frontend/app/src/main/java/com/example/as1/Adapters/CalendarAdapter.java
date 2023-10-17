package com.example.as1.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Enum.TypeOfAppointment;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.Parser.LocalDateAndTimeParser;
import com.example.as1.R;
import com.example.as1.Utils.CalendarUtils;
import com.example.as1.ViewHolder.CalendarViewHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private OnItemListener onItemListener;
    private User user;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        user = User.getUser(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if (date.equals(CalendarUtils.selectedDate))
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        if (date.getMonth().equals(CalendarUtils.selectedDate.getMonth()))
            holder.dayOfMonth.setTextColor(Color.BLACK);
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);

        // Check if an appointment exists for this day
        if (user != null && user.appointmentList != null && user.appointmentList.size() > 0) {
            boolean hasCourse = checkIfAppointmentExistsAndCourse(date, user.appointmentList);
            // Set coloredRectangle visibility
            holder.coloredRectangleGold.setVisibility(hasCourse ? View.VISIBLE : View.INVISIBLE);

            // Check if an appointment exists for this day
            boolean hasAppointment = checkIfAppointmentExistsAndAppointment(date, user.appointmentList);
            // Set coloredRectangle visibility
            holder.coloredRectangleCardinal.setVisibility(hasAppointment ? View.VISIBLE : View.INVISIBLE);


            // Check if an appointment exists for this day
            boolean hasSleeptime = checkIfAppointmentExistsAndSleeptime(date, user.appointmentList);
            // Set coloredRectangle visibility
            holder.coloredRectangleBlue.setVisibility(hasSleeptime ? View.VISIBLE : View.INVISIBLE);
        }

    }


    private boolean checkIfAppointmentExistsAndCourse(LocalDate date, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            LocalDate localDate = LocalDateAndTimeParser.StringToDate(appointment.getDate());
            if (localDate.equals(date) && appointment.getType() == TypeOfAppointment.COURSE) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfAppointmentExistsAndSleeptime(LocalDate date, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            LocalDate localDate = LocalDateAndTimeParser.StringToDate(appointment.getDate());
            if (localDate.equals(date) && appointment.getType() == TypeOfAppointment.SLEEPTIME) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfAppointmentExistsAndAppointment(LocalDate date, List<Appointment> appointmentList) {
        for (Appointment appointment : appointmentList) {
            LocalDate localDate = LocalDateAndTimeParser.StringToDate(appointment.getDate());
            if (localDate.equals(date) && appointment.getType() == TypeOfAppointment.APPOINTMENT) {
                return true;
            }
        }
        return false;
    }

    public void updateDays(ArrayList<LocalDate> days) {
        this.days.clear();
        this.days.addAll(days);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View cellView;
        private OnItemListener onItemListener;
        int year, month;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener,
                          @NonNull ViewGroup parent) {
            super(itemView);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext()); //Might not work, if now change the cellView to the textViw within the cell
            cellView = inflater.inflate(R.layout.calendar_cell, parent, false);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            onItemListener.onItemClick(getAdapterPosition(),
                    LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            getAdapterPosition() + 1));
        }
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);

        void onItemLongClick(int position, LocalDate date);

    }

    public void setOnItemCLickListener(OnItemListener listener) {
        this.onItemListener = listener;
    }
}