package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.as1.Controller.UserController;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class ViewAdvisorActivity extends AppCompatActivity {

    TextView advisorText;
    UserController controller = new UserController();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advisor);
        advisorText = findViewById(R.id.advisor_data);
        User user = User.getUser(ViewAdvisorActivity.this);
        controller.getAdvisor(this, URL.URL_GET_USER_INFO, user.getId(), advisorText);
    }
}