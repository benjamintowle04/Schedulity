package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.as1.Controller.UserController;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class UserActivity extends AppCompatActivity {
    Button backBtn, logoutButton, changePasswordBtn, changeEmailBtn, changeUsernameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        backBtn = findViewById(R.id.backBtn);
        logoutButton = findViewById(R.id.logout_button);
        changePasswordBtn = findViewById(R.id.change_password_Btn);
        changeEmailBtn = findViewById(R.id.change_email_Btn);
        changeUsernameBtn = findViewById(R.id.change_userName_Btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.baseline_person_24);
        getSupportActionBar().setTitle("  User Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getUser(UserActivity.this);
                user.setType(LoggedInStates.NOT_LOGGED_IN);
                User.saveUser(UserActivity.this, user);
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChangePasswordPopup();
            }
        });
        changeUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePopup(R.layout.activity_change_username_popup);
            }
        });
        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePopup(R.layout.activity_change_email_popup);
            }
        });
    }

    private void CreatePopup(int popupView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(popupView, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

        Button cancelBtn, doneBtn;
        EditText informationEdt;

        cancelBtn = popUpView.findViewById(R.id.CancelBtn);
        doneBtn = popUpView.findViewById(R.id.DoneBtn);
        informationEdt = popUpView.findViewById(R.id.information_Edt);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked on the close btn \n\n");
                popupWindow.dismiss();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = informationEdt.getText().toString().trim();
                if (username.equals("")) {
                    Toast.makeText(UserActivity.this, "Please enter a information", Toast.LENGTH_LONG).show();
                } else {
                    User user = User.getUser(UserActivity.this);
                    if (user == null) {
                        Toast.makeText(UserActivity.this, "User is NULL!", Toast.LENGTH_LONG).show();

                        return;
                    }
                    user.setUsername(username);
                    UserController controller = new UserController();
                    controller.sendPostChangeUser(UserActivity.this, user,
                            URL.URL_POST_USER_UPDATE + "username/" + user.getId());
                    Toast.makeText(UserActivity.this, "Changed User Information", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();

                }
            }
        });


        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void CreateChangePasswordPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.activity_change_password_popup, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

        Button cancelBtn, doneBtn;
        EditText oldPwEdt, newPwEdt, newRepeatPwEdt;


        cancelBtn = popUpView.findViewById(R.id.CancelBtn);
        doneBtn = popUpView.findViewById(R.id.DoneBtn);

        oldPwEdt = popUpView.findViewById(R.id.old_passwordEdt);
        newPwEdt = popUpView.findViewById(R.id.newPassword_Edt);
        newRepeatPwEdt = popUpView.findViewById(R.id.repeate_password_Edt);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked on the close btn \n\n");
                popupWindow.dismiss();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getUser(UserActivity.this);

                String oldPW = oldPwEdt.getText().toString().trim();
                String newPW = newPwEdt.getText().toString().trim();
                String repNewPW = newRepeatPwEdt.getText().toString().trim();

                if (oldPW.equals(null) || newPW.equals(null) || repNewPW.equals(null)) {
                    Toast.makeText(UserActivity.this, "Pleas fill out all fields", Toast.LENGTH_LONG).show();
                } else if (user == null || !user.getPassword().equals(oldPW)) {
                    Toast.makeText(UserActivity.this, "Current password is not correct", Toast.LENGTH_LONG).show();
                } else if (!user.getPassword().equals(oldPW)) {
                    Toast.makeText(UserActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                } else {
                    user.setPassword(newPW);
                    UserController controller = new UserController();
                    controller.sendPostChangeUser(UserActivity.this, user, URL.URL_POST_USER_UPDATE + "password/" + user.getId());

                    Toast.makeText(UserActivity.this, "Password changed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();

                }

            }
        });


        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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