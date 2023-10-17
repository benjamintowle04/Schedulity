package com.example.as1;

import static com.example.as1.Utils.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.as1.Adapters.HourAdapter;
import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Controller.FriendsController;
import com.example.as1.Enum.ViewType;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.HourEvent;
import com.example.as1.Models.User;
import com.example.as1.Utils.CalendarUtils;
import com.example.as1.Utils.URL;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyViewActivity extends AppCompatActivity {

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    Button appt_or_setWeeklyView;
    FriendsController controller;
    Button createAppt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = User.getUser(this);
        NavigationView navigationView;
        if (user.getAccountType() == AccountType.PARENT_USER) {
            setContentView(R.layout.activity_daily_view_parent);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Parent");
        }
        else if (user.getAccountType() == AccountType.ADVISER) {
            setContentView(R.layout.activity_daily_view_advisor);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Advisor");
        }
        else {
            setContentView(R.layout.activity_daily_view);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Student");
        }
        appt_or_setWeeklyView = findViewById(R.id.create_appointment_btn);
        controller = new FriendsController();
        Intent fromIntent = getIntent();


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    case R.id.nav_home:
                        Intent intent_home = new Intent(DailyViewActivity.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.nav_user:
                        Intent intent_user = new Intent(DailyViewActivity.this, UserActivity.class);
                        startActivity(intent_user);
                        break;
                    case R.id.journey:
                        Intent intent_journey = new Intent(DailyViewActivity.this, StudyHoursActivity.class);
                        startActivity(intent_journey);
                        break;
                    case R.id.friends:
                        Intent intent_friends = new Intent(DailyViewActivity.this, FriendsActivity.class);
                        startActivity(intent_friends);
                        break;
                    case R.id.settings:
                        Toast.makeText(DailyViewActivity.this, "Settings is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_enterSchedule:
                        Intent intent_login = new Intent(DailyViewActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(DailyViewActivity.this, "Logout is clicked", Toast.LENGTH_SHORT).show();
                        User.removeUser(DailyViewActivity.this);
                        Intent intent_logout = new Intent(DailyViewActivity.this, LoginActivity.class);
                        startActivity(intent_logout);
                        break;
                    case R.id.help:
                        Toast.makeText(DailyViewActivity.this, "Help is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(DailyViewActivity.this, "Share is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(DailyViewActivity.this, "Rate us is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                User user = User.getUser(DailyViewActivity.this);
                switch (item.getItemId()) {
                    case R.id.MonthlyView:
                        Toast.makeText(DailyViewActivity.this, "Monthly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Monthly;
                        Intent intentM = new Intent(DailyViewActivity.this, MainActivity.class);
                        startActivity(intentM);
                        break;
                    case R.id.WeeklyView:
                        Toast.makeText(DailyViewActivity.this, "Weekly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Weekly;
                        user.viewType = ViewType.Monthly;
                        Intent intentW = new Intent(DailyViewActivity.this, WeeklyViewActivity.class);
                        startActivity(intentW);

                        break;
                    case R.id.DailyView:
                        Toast.makeText(DailyViewActivity.this, "Daily View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Daily;
                        user.viewType = ViewType.Monthly;
                        Intent intentD = new Intent(DailyViewActivity.this, DailyViewActivity.class);
                        startActivity(intentD);

                        break;
                    default:
                        return false;
                }
                user.saveUser(DailyViewActivity.this);
                return true;
            }
        });

        initWidgets();

        if (user.getAccountType() != AccountType.NORMAL_USER) {
            appt_or_setWeeklyView.setVisibility(View.GONE);
        }

        if (fromIntent.getStringExtra("activity") != null &&
                fromIntent.getStringExtra("activity").equals("fromFriends")) {

            appt_or_setWeeklyView.setText("Back to profile page");
            int id = fromIntent.getIntExtra("id", Integer.MAX_VALUE);
            String friendName = fromIntent.getStringExtra("friendName");
            appt_or_setWeeklyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DailyViewActivity.this, FriendsDetailActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("friendName", friendName);
                    startActivity(intent);
                }
            });

            controller.getFriendDayRequest(DailyViewActivity.this, URL.URL_GET_DAY_TEST + "/1/4/20/F", hourListView);
        }

    }

    private void initWidgets() {
        monthDayText = findViewById(R.id.monthDayTV);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        User user = User.getUser(DailyViewActivity.this);

        //if not logged in intent to
        if (user == null || user.getType() == LoggedInStates.NOT_LOGGED_IN) {
            Intent intent = new Intent(DailyViewActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        setDayView();
    }

    private void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = null;

        if (selectedDate.getDayOfWeek() != null) {
            dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            dayOfWeekTV.setText(dayOfWeek);
            setHourAdapter();
        }

    }

    private void setHourAdapter() {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        User user = User.getUser(DailyViewActivity.this);

        for (int hour = 0; hour < 24; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Appointment> appointments = Appointment.appointmentsForDateAndTime(user.appointmentList, selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, appointments);


            list.add(hourEvent);
        }

        return list;
    }

    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, AppointmentEditActivity.class));
    }

}