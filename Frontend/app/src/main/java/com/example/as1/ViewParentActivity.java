package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.as1.Controller.UserController;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class ViewParentActivity extends AppCompatActivity {

    TextView parentText;
    UserController controller = new UserController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parent);
        parentText = findViewById(R.id.parent_data);
        User user = User.getUser(ViewParentActivity.this);
        controller.getParent(this, URL.URL_GET_USER_INFO, user.getId(), parentText);

    }
}