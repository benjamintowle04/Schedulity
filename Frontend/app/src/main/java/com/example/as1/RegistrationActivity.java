package com.example.as1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Controller.UserController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    EditText password, repeatPassword, email, username;
    Button registerButton;
    private ProgressDialog progressDialog;
    TextView loginText, headerText, studentText, parentText, advisorText;

    String url = URL.URL_POST_USER_REGISTER;
    AccountType type = AccountType.NORMAL_USER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        password = findViewById(R.id.passwordTextEdit);
        repeatPassword = findViewById(R.id.repeatepasswordTextedit);
        email = findViewById(R.id.emailEdittext);
        username = findViewById(R.id.userNameEdittext);
        registerButton = findViewById(R.id.RegisterButton);
        loginText = findViewById(R.id.LoginText);
        headerText = findViewById(R.id.registration);
        studentText = findViewById(R.id.studentTextViewRegister);
        parentText = findViewById(R.id.parentTextViewRegister);
        advisorText = findViewById(R.id.advisorTextViewRegister);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        studentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == AccountType.NORMAL_USER) {
                    String message = "Already Logging in as a student by default";
                    Toast toast = Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }

                headerText.setText("Student Register");
                type = AccountType.PARENT_USER;
                url = URL.URL_POST_USER_REGISTER;

            }
        });

        parentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("Parent Register");
                type = AccountType.PARENT_USER;
                url = URL.URL_POST_PARENT_REGISTER;
            }
        });

        advisorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("Advisor Register");
                type = AccountType.ADVISER;
                url = URL.URL_POST_ADVISOR_REGISTER;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("you clicked on the Register Button");
                if (!password.getText().toString().equals("") && !username.getText().toString().equals("")
                        && !email.getText().toString().equals("") && !repeatPassword.getText().toString().equals("")) {
                    System.out.println("you filled in all fields");

                    if (password.getText().toString().equals(repeatPassword.getText().toString())) {
                        System.out.println("provided passwords does match");
                        User user = new User(username.getText().toString(), password.getText().toString(), email.getText().toString(), LoggedInStates.LOGGED_IN, type);
                        UserController controller = new UserController();
                        controller.sendPostLoginAndRegister(RegistrationActivity.this, user, url);
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "All Fields must be filled", Toast.LENGTH_LONG).show();
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    private void postDataUsingVolley(String name, String job) {
        // url to post our data
        String url = "http://localhost:8080/users";
        showProgressDialog();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                hideProgressDialog();

                // on below line we are displaying a success toast message.
                Toast.makeText(RegistrationActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String name = respObj.getString("name");
                    String job = respObj.getString("job");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(RegistrationActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("name", name);
                params.put("job", job);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}