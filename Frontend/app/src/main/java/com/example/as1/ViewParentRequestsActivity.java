package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.as1.Controller.ParentController;
import com.example.as1.Controller.UserController;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class ViewParentRequestsActivity extends AppCompatActivity {
    Button accept, decline, back;
    TextView requestStatus;
    ParentController controller = new ParentController();
    UserController userController = new UserController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parent_requests);
        accept = findViewById(R.id.accept_btn);
        decline = findViewById(R.id.decline_btn);
        back = findViewById(R.id.back_btn_parentReq);
        requestStatus = findViewById(R.id.request_info_textview);

        User user = User.getUser(ViewParentRequestsActivity.this);
        int id = user.getId();

        userController.getUserInfo(ViewParentRequestsActivity.this, URL.URL_GET_USER_INFO, id, requestStatus, accept, decline);



    }
}