package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Controller.AdvisorController;
import com.example.as1.Controller.FriendsController;
import com.example.as1.Controller.ParentController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.Friend;
import com.example.as1.Models.User;
import com.example.as1.Utils.CalendarUtils;
import com.example.as1.Utils.URL;

import java.time.LocalDate;
import java.util.ArrayList;


public class FriendsDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    CalendarView calendarView;
    TextView username;
    Integer id;
    ImageButton addFriendsImageBtn, sendMessageBtn;
    AdvisorController aController = new AdvisorController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_detail);

        username = findViewById(R.id.UsernameDetailText);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendMessageBtn = findViewById(R.id.searchView);
        addFriendsImageBtn = findViewById(R.id.AddFriendsImageBtn);
        calendarView = findViewById(R.id.calendarView);
        User user = User.getUser(FriendsDetailActivity.this);



        if (user.getAccountType() == AccountType.PARENT_USER) {
            getSupportActionBar().setIcon(R.drawable.baseline_child_care_24);
            getSupportActionBar().setTitle("  Kid Details");
            sendMessageBtn.setVisibility(View.GONE);
            addFriendsImageBtn.setForegroundGravity(Gravity.CENTER);
            calendarView.setVisibility(View.INVISIBLE);

        }
        else if (user.getAccountType() == AccountType.ADVISER) {
            getSupportActionBar().setIcon(R.drawable.baseline_blind_24);
            getSupportActionBar().setTitle("  Student Details");
            sendMessageBtn.setVisibility(View.GONE);
            addFriendsImageBtn.setForegroundGravity(Gravity.CENTER);
            calendarView.setVisibility(View.INVISIBLE);
        }

        else {
            getSupportActionBar().setIcon(R.drawable.baseline_group_24);
            getSupportActionBar().setTitle("  Friend Details");
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        username.setText(intent.getStringExtra("friendName"));
        id = intent.getIntExtra("id", Integer.MAX_VALUE);
        if (user.getFriendsList() != null) {
            for (int i = 0; i < user.getFriendsList().size(); i++) {
                if (user.getFriendsList().get(i).getUsername().equals(intent.getStringExtra("friendName"))) {
                    calendarView.setVisibility(View.VISIBLE);
                    addFriendsImageBtn.setVisibility(View.GONE);
                }
            }
        }

        //Find this friend in the user's friends list
        if (user.getFriendsList() != null) {
            for (int i = 0; i < user.getFriendsList().size(); i++) {
                if (user.getFriendsList().get(i).getUsername().equals(username.getText().toString())) {
                    User.selectedFriend = user.getFriendsList().get(i);
                }
            }
        }

        if (id == Integer.MAX_VALUE) {
            return;
        }


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Hier können Sie den ausgewählten Tag bearbeiten
                Log.d("CalendarView", "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year);
                CalendarUtils.selectedDate = LocalDate.of(year, (month + 1), dayOfMonth);
                Intent intent = new Intent(FriendsDetailActivity.this, DailyViewActivity.class);
                intent.putExtra("activity", "fromFriends");
                intent.putExtra("friendName", username.getText().toString());
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


        addFriendsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FriendsAdd", " ADD FRIEND !!!! ");

                FriendsController controller = new FriendsController();

                if (user.getFriendsList() == null || !(user.getAccountType() == AccountType.PARENT_USER && user.getFriendsList().size() == 1)) {
                    String url = URL.URL_POST_ADD_FRIEND.replace("{id}", User.getUser(FriendsDetailActivity.this).getId().toString());
                    if (user.getAccountType() == AccountType.ADVISER) {
                        url = URL.URL_ADD_STUDENT;
                        aController.sendPostAddStudent(FriendsDetailActivity.this, url, user.getId(), id);
                    }
                    else if (user.getAccountType() == AccountType.PARENT_USER) {
                        ParentController parentController = new ParentController();
                        parentController.requestToParent(FriendsDetailActivity.this, URL.URL_ADD_REQUEST_KID, user.getId(), id);
                    }
                    else {
                        controller.sendPostRequest(FriendsDetailActivity.this, id, url, false);
                    }

                    if (user.getFriendsList() != null && user.getFriendsList().size() != 0) {
                        User.selectedFriend = user.getFriendsList().get(user.getFriendsList().size() - 1);
                    }
                    User.saveUser(FriendsDetailActivity.this, user);
                }
                else {
                    String message = "You already have a child";
                    Toast toast = Toast.makeText(FriendsDetailActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toIntent = new Intent(FriendsDetailActivity.this,
                        WebSocketConnectActivity.class);

                toIntent.putExtra("friendName", username.getText().toString());

                startActivity(toIntent);
            }
        });

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
}