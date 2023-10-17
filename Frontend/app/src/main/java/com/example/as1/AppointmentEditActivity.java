package com.example.as1;

import static com.example.as1.Utils.URL.URL_POST_ADD_APPOINTMENT;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Controller.AppointmentController;
import com.example.as1.Enum.ReoccuringOptions;
import com.example.as1.Enum.TypeOfAppointment;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.Reoccurring;
import com.example.as1.Models.User;
import com.example.as1.Parser.LocalDateAndTimeParser;
import com.example.as1.Utils.CalendarUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppointmentEditActivity extends AppCompatActivity {
    int minutes = 0, hours = 0;
    int minutesEnd = 0, hoursEnd = 0;
    int year = 0, month = 0, day = 0;
    private EditText eventNameET, notesET, locationEt;
    private TextView eventDateTV, startTimeTV, endTimeTV, reoccurringTV;
    private RadioGroup radioGroup;
    private RadioButton notreoccurringRB, reoccurringRB;
    Boolean isReoccurring;
    Integer id;
    Appointment selectedAppointment;
    Reoccurring reoccurring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_edit);
        initWidgets();
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        startTimeTV = findViewById(R.id.startTimeTV);
        endTimeTV = findViewById(R.id.endTimeTV);
        locationEt = findViewById(R.id.LocationET);
        notesET = findViewById(R.id.notesET);
        reoccurringTV = findViewById(R.id.reoccurringTV);

        radioGroup = findViewById(R.id.reoccurringGroup);
        reoccurringRB = findViewById(R.id.reoccurring);
        notreoccurringRB = findViewById(R.id.notreoccurring);
    }

    public void saveAppointmentAction(View view) {
        String eventName = eventNameET.getText().toString();
        User user = User.getUser(this);
        Integer id = user.appointmentList.size();
        if (user == null) {
            // Handle error - could not retrieve user
            return;
        }
        if (eventName.isEmpty()) {
            eventName = "New Event " + (user.getAppointmentList().size() + 1);
        }

        Appointment appointmentToUpdate = null;
        if (id != null) {
            appointmentToUpdate = user.getAppointmentById(id);
        }

        if (appointmentToUpdate != null) {
            appointmentToUpdate.setName(eventName);
            appointmentToUpdate.setEndTime(endTimeTV.getText().toString());
            appointmentToUpdate.setStartTime(startTimeTV.getText().toString());
            appointmentToUpdate.setLocation(locationEt.getText().toString());
            appointmentToUpdate.setNotes(notesET.getText().toString());
            if (reoccurring != null) {
                appointmentToUpdate.setRecurring(true);
            }

            if (year != 0 && month != 0 && day != 0) {
                appointmentToUpdate.setDate(LocalDate.of(year, month, day).toString());
            }

            AppointmentController controller = new AppointmentController();
            String url = URL_POST_ADD_APPOINTMENT.replace("{id}", User.getUser(AppointmentEditActivity.this).getId().toString());
            controller.createAppointment(AppointmentEditActivity.this, appointmentToUpdate, url);
        } else {
            Appointment newAppointment = new Appointment(eventName,
                    id,
                    LocalDate.of(year, month, day),
                    LocalTime.of(hours, minutes),
                    LocalTime.of(hoursEnd, minutesEnd));
            newAppointment.setNotes(notesET.getText().toString());
            newAppointment.setLocation(locationEt.getText().toString());
            newAppointment.setType(TypeOfAppointment.APPOINTMENT);
            user.addAppointmentToList(newAppointment);

            AppointmentController controller = new AppointmentController();
            String url = URL_POST_ADD_APPOINTMENT.replace("{id}", User.getUser(AppointmentEditActivity.this).getId().toString());
            controller.createAppointment(AppointmentEditActivity.this, newAppointment, url);
        }

        user.saveUser(this);

        finish();
    }

    public void datePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                month = month + 1; //because jan is 0
                String date = makeDateString(day, month, year);
                eventDateTV.setText("Date: " + date);
                CalendarUtils.selectedDate = LocalDate.of(year, month, day);
            }
        };

        Calendar cal = Calendar.getInstance();
        if (year == 0)
            year = cal.get(Calendar.YEAR);
        if (month == 0)
            month = cal.get(Calendar.MONTH);
        if (day == 0)
            day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, style, onDateSetListener, year, month, day);
        datePickerDialog.setTitle("Select Date of Appointment");
        datePickerDialog.show();
    }

    public void TimePickerStart(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinutes) {
                hours = selectedHours;
                minutes = selectedMinutes;
                startTimeTV.setText(String.format(Locale.getDefault(), "Time: %02d:%02d", hours, minutes));
//                time = LocalTime.of(hours, minutes);
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        if (hours == 0)
            hours = LocalTime.now().getHour();
        if (minutes == 0)
            minutes = LocalTime.now().getMinute();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hours, minutes, true);
        timePickerDialog.setTitle("Select Time of Appointment");
        timePickerDialog.show();
    }

    public void TimePickerEnd(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinutes) {
                hoursEnd = selectedHours;
                minutesEnd = selectedMinutes;
                endTimeTV.setText(String.format(Locale.getDefault(), "Time: %02d:%02d", hoursEnd, minutesEnd));
//                time = LocalTime.of(hours, minutes);
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        if (hoursEnd == 0)
            hoursEnd = LocalTime.now().getHour();
        if (minutesEnd == 0)
            minutesEnd = LocalTime.now().getMinute();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hoursEnd, minutesEnd, true);
        timePickerDialog.setTitle("Select Time of Appointment");
        timePickerDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("appointment")) {
            selectedAppointment = (Appointment) intent.getSerializableExtra("appointment");
            eventNameET.setText(selectedAppointment.getName());
            locationEt.setText((selectedAppointment.getLocation()));
            notesET.setText(selectedAppointment.getNotes());
            id = selectedAppointment.getId();

            String dateString = selectedAppointment.getDate();
            String startTimeString = selectedAppointment.getStartTime();
            String endTimeString = selectedAppointment.getEndTime();

            if (dateString != null) {
                LocalDate date = LocalDateAndTimeParser.StringToDate(dateString);
                day = date.getDayOfMonth();
                month = date.getMonthValue();
                year = date.getYear();
            }
            if (startTimeString != null) {
                LocalTime startTime = LocalDateAndTimeParser.StringToTime(startTimeString);
                hours = startTime.getHour();
                minutes = startTime.getMinute();
            }
            if (endTimeString != null) {
                LocalTime endTime = LocalDateAndTimeParser.StringToTime(endTimeString);
                hoursEnd = endTime.getHour();
                minutesEnd = endTime.getMinute();
            }
            isReoccurring = selectedAppointment.isRecurring();

            eventDateTV.setText("Date: " + dateString);
            startTimeTV.setText("Time: " + startTimeString);
            endTimeTV.setText("Time: " + endTimeString);

            reoccurringTV.setText(ReoccuringOptions.fromValue(0).toString());

        } else {
            day = CalendarUtils.selectedDate.getDayOfMonth();
            month = CalendarUtils.selectedDate.getMonthValue();
            year = CalendarUtils.selectedDate.getYear();
            hours = LocalTime.now().getHour();
            minutes = LocalTime.now().getMinute();
            hoursEnd = LocalTime.now().getHour();
            hoursEnd += 1;
            minutesEnd = LocalTime.now().getMinute();

            String dateString = makeDateString(day, month, year);
            eventDateTV.setText("Date: " + dateString);
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            startTimeTV.setText("Time: " + currentTime.format(formatter));
            endTimeTV.setText("Time: " + currentTime.plusHours(1).format(formatter));
            reoccurringTV.setText(ReoccuringOptions.fromValue(0).toString());
        }

        if (intent != null && intent.hasExtra("selectedValue")) {
            ReoccuringOptions selectedValue = ReoccuringOptions.fromValue(intent.getIntExtra("selectedValue", 0));
            reoccurringTV.setText(selectedValue.toString());
            isReoccurring = true;
        } else if (intent != null && intent.hasExtra("reoccurring")) {
            reoccurring = (Reoccurring) intent.getSerializableExtra("reoccurring");

            if (selectedAppointment == null) {
                selectedAppointment = new Appointment("New Event",
                        id,
                        LocalDate.of(year, month, day),
                        LocalTime.of(hours, minutes),
                        LocalTime.of(hoursEnd, minutesEnd));
            }
            selectedAppointment.setRecurring(true);
            //do further reoccurring things
        }

        if (selectedAppointment != null && selectedAppointment.isRecurring()) {
            radioGroup.setVisibility(View.VISIBLE);
        } else {
            radioGroup.setVisibility(View.INVISIBLE);
        }

    }

    public void Reoccurring(View view) {
        startActivity(new Intent(this, SelectReoccurringActivity.class));
    }

    public void deleteAppointmentAction(View view) {
        User user = User.getUser(this);
        user.removeAppointmentById(selectedAppointment.getId());
        user.saveUser(this);
        onBackPressed();
    }
}