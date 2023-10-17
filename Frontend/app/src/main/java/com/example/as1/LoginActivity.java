package com.example.as1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Controller.UserController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText passwordText, usernameText, emailText;
    TextView registerText, parentText, advisorText, studentText, headerText;
    User user = null;
    int accountType = 1;
    String url = URL.URL_POST_USER_LOGIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButtion);
        registerText = findViewById(R.id.RegisterText);
        passwordText = findViewById(R.id.passwordTextEdit);
        usernameText = findViewById(R.id.usernametext);
        emailText = findViewById(R.id.emailEdittext);
        advisorText = findViewById(R.id.advisorTextView);
        studentText = findViewById(R.id.studentTextView);
        parentText = findViewById(R.id.parentTextView);
        headerText = findViewById(R.id.login_text_view);



        user = User.getUser(LoginActivity.this);
        if (user != null) {

            usernameText.setText(user.getUsername());
            emailText.setText(user.getEmail());

        }

        studentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountType == 1) {
                    String message = "Already Logging in as a student by default";
                    Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }

                headerText.setText("Student Login");
                accountType = 1;
            }
        });

        parentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("Parent Login");
                accountType = 2;
            }
        });

        advisorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("Advisor Login");
                accountType = 3;
            }

        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordText.getText().toString().equals("") && !usernameText.getText().toString().equals("")) {

                    String password = passwordText.getText().toString();
                    String username = usernameText.getText().toString();
                    String email = emailText.getText().toString();

                    if (accountType == 1) {
                        user = new User(username, password, email, LoggedInStates.LOGGED_IN, AccountType.NORMAL_USER);
                    }
                    else if (accountType == 2) {
                        user = new User(username, password, email, LoggedInStates.LOGGED_IN, AccountType.PARENT_USER);
                    }
                    else if (accountType == 3) {
                        user = new User(username, password, email, LoggedInStates.LOGGED_IN, AccountType.ADVISER);
                    }

                    UserController controller = new UserController();
                    System.out.println("calling post");

                    controller.sendPostLoginAndRegister(LoginActivity.this, user, url);
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

}