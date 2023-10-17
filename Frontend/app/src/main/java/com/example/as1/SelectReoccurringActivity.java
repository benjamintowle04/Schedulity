package com.example.as1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.as1.Models.Reoccurring;
import com.example.as1.Utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectReoccurringActivity extends AppCompatActivity {

    private Button submitButton;
    private CheckBox checkBoxDaily, checkBoxWeekly, checkBoxMonthly, checkBoxYearly;
    private TextView startDate, endDate;
    int year, month, day;
    private LinearLayout selectDateDialogue, selectDayDialogue;
    private Boolean isOkay;

    Button mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton, sundayButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reoccurring);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        submitButton = findViewById(R.id.submitButton);
        checkBoxDaily = findViewById(R.id.checkBoxDaily);
        checkBoxWeekly = findViewById(R.id.checkBoxWeekly);
        checkBoxMonthly = findViewById(R.id.checkBoxMonthly);
        checkBoxYearly = findViewById(R.id.checkBoxYearly);

        startDate = findViewById(R.id.StartDateTV);
        endDate = findViewById(R.id.EndDateTV);
        selectDateDialogue = findViewById(R.id.SelectDateDialoge);
        selectDayDialogue = findViewById(R.id.SelectDayDialoge);

        mondayButton = findViewById(R.id.MondayButton);
        mondayButton.setTag(false);
        tuesdayButton = findViewById(R.id.TuesdayButton);
        tuesdayButton.setTag(false);
        wednesdayButton = findViewById(R.id.WednesdayButton);
        wednesdayButton.setTag(false);
        thursdayButton = findViewById(R.id.ThursdayButton);
        thursdayButton.setTag(false);
        fridayButton = findViewById(R.id.FridayButton);
        fridayButton.setTag(false);
        saturdayButton = findViewById(R.id.SaturdayButton);
        saturdayButton.setTag(false);
        sundayButton = findViewById(R.id.SundayButton);
        sundayButton.setTag(false);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.baseline_schedule_24);
        getSupportActionBar().setTitle("  Select Reoccurring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button mondayButton = findViewById(R.id.MondayButton);
        Button tuesdayButton = findViewById(R.id.TuesdayButton);
        Button wednesdayButton = findViewById(R.id.WednesdayButton);
        Button thursdayButton = findViewById(R.id.ThursdayButton);
        Button fridayButton = findViewById(R.id.FridayButton);
        Button saturdayButton = findViewById(R.id.SaturdayButton);
        Button sundayButton = findViewById(R.id.SundayButton);

        mondayButton.setOnClickListener(onClickListener);
        tuesdayButton.setOnClickListener(onClickListener);
        wednesdayButton.setOnClickListener(onClickListener);
        thursdayButton.setOnClickListener(onClickListener);
        fridayButton.setOnClickListener(onClickListener);
        saturdayButton.setOnClickListener(onClickListener);
        sundayButton.setOnClickListener(onClickListener);

        checkBoxDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectDayDialogue.setVisibility(View.VISIBLE);
                } else {
                    selectDayDialogue.setVisibility(View.GONE);
                }
            }
        });
        checkBoxWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectDateDialogue.setVisibility(View.VISIBLE);
                } else {
                    selectDateDialogue.setVisibility(View.GONE);
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOkay = true;
                Reoccurring reoccurring = new Reoccurring();

                reoccurring.isReoccurring = checkBoxDaily.isChecked() || checkBoxWeekly.isChecked() || checkBoxMonthly.isChecked() || checkBoxYearly.isChecked();
                reoccurring.isDaily = checkBoxDaily.isChecked();
                reoccurring.isWeekly = checkBoxWeekly.isChecked();
                reoccurring.isMonthly = checkBoxMonthly.isChecked();
                reoccurring.isYearly = checkBoxYearly.isChecked();

                if(checkBoxDaily.isChecked()){
                    // Use an array of buttons for looping
                    Button[] dayButtons = new Button[]{mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton, sundayButton};
                    DayOfWeek[] dayOfWeeks = new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

                    List<DayOfWeek> selectedDays = new ArrayList<>();
                    for (int i = 0; i < dayButtons.length; i++) {
                        boolean isButtonClicked = (Boolean) dayButtons[i].getTag();
                        if (isButtonClicked) {
                            selectedDays.add(dayOfWeeks[i]);
                        }
                    }

                    if (selectedDays.size() == 0) {
                        Toast.makeText(SelectReoccurringActivity.this, "Pls select Weekdays", Toast.LENGTH_SHORT).show();
                        isOkay = false;
                    }
                    reoccurring.dayOfWeekList = selectedDays;
                }

                // Set start and end dates using the displayed dates in the TextViews, assuming the date format is "dd.MM.yyyy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                if (checkBoxWeekly.isChecked()) {
                    String temp = startDate.getText().toString();
                    if (startDate.getText().toString() == "" || startDate.getText().toString().equals("Start Date")) {
                        Toast.makeText(SelectReoccurringActivity.this, "Pls select Start Date", Toast.LENGTH_SHORT).show();
                        isOkay = false;
                    } else {
                        reoccurring.startDate = LocalDate.parse(startDate.getText().toString(), formatter);
                    }

                    if (endDate.getText().toString().equals("End Date")) {
                        Toast.makeText(SelectReoccurringActivity.this, "Pls select End Date", Toast.LENGTH_SHORT).show();
                        isOkay = false;
                    } else {
                        reoccurring.endDate = LocalDate.parse(endDate.getText().toString(), formatter);
                    }
                }

                if (isOkay) {
                    Intent intent = new Intent(SelectReoccurringActivity.this, AppointmentEditActivity.class);
                    intent.putExtra("reoccurring", reoccurring);
                    startActivity(intent);
                }
            }
        });


        DatePickerDialog startDatePickerDialog = new DatePickerDialog(SelectReoccurringActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                startDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime()));
            }
        }, year, month, day);

        DatePickerDialog endDatePickerDialog = new DatePickerDialog(SelectReoccurringActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                endDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime()));
            }
        }, year, month, day);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MondayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.TuesdayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.WednesdayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.ThursdayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.FridayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.SaturdayButton:
                    toggleButtonColor((Button) v);
                    break;
                case R.id.SundayButton:
                    toggleButtonColor((Button) v);
                    break;
            }
        }
    };

    private void toggleButtonColor(Button button) {
        boolean isButtonClicked = (Boolean) button.getTag();
        if (isButtonClicked) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.cardinal));
            button.setTag(false);
        } else {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.gold));
            button.setTag(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDatePickerDialogStart(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                month = month + 1; //because jan is 0
                String date = makeDateString(day, month, year);
                startDate.setText("Date: " + date);
                CalendarUtils.selectedDate = LocalDate.of(year, month, day);
            }
        };

        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        if (year == 0)
            year = cal.get(android.icu.util.Calendar.YEAR);
        if (month == 0)
            month = cal.get(android.icu.util.Calendar.MONTH);
        if (day == 0)
            day = cal.get(android.icu.util.Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, style, onDateSetListener, year, month, day);
        datePickerDialog.setTitle("Select Date of Appointment");
        datePickerDialog.show();
    }

    public void showDatePickerDialogEnd(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                month = month + 1; //because jan is 0
                String date = makeDateString(day, month, year);
                endDate.setText("Date: " + date);
                CalendarUtils.selectedDate = LocalDate.of(year, month, day);
            }
        };

        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        if (year == 0)
            year = cal.get(android.icu.util.Calendar.YEAR);
        if (month == 0)
            month = cal.get(android.icu.util.Calendar.MONTH);
        if (day == 0)
            day = cal.get(android.icu.util.Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, style, onDateSetListener, year, month, day);
        datePickerDialog.setTitle("Select Date of Appointment");
        datePickerDialog.show();
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

}