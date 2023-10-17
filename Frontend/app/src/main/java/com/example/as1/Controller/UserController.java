package com.example.as1.Controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.MainActivity;
import com.example.as1.Models.Course;
import com.example.as1.Models.Extracurricular;
import com.example.as1.Models.Friend;
import com.example.as1.Models.User;
import com.example.as1.R;
import com.example.as1.Utils.URL;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserController extends Application {

    public void sendPostLoginAndRegister(Context context, User user, String url) {

        JSONObject json = new JSONObject();
        try {
            // Convert object to JSON object
            json.put("username", user.getUsername());
            json.put("password", user.getPassword());
            json.put("email", user.getEmail());
            if (user.getAccountType() == AccountType.NORMAL_USER) {
                json.put("type", 1);
            }
            else if (user.getAccountType() == AccountType.PARENT_USER) {
                 json.put("type", 2);
            }
            else if (user.getAccountType() == AccountType.ADVISER) {
                json.put("type", 3);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("JSON string:\n " + json.toString());

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println("response: ");

                        System.out.println(response.toString() + "\n\n");
                        String message = response.optString("message");


                        if (message.equals("success")) {
                            int id = Integer.parseInt(response.optString("id"));
                            user.setType(LoggedInStates.LOGGED_IN);
                            user.setId(id);
                            System.out.println("succesful logged in:\n");
                            Log.d("USERController", "recieved json: " + json.toString());
                            Log.d("USERController", "my user: " + user.toString());
                            User.saveUser(context, user);
                            if (context != null) {
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }

                        } else {
                            System.out.println("login was not succesful \n");
                            Log.d("USERController", "my user: " + user.toString());
                            Toast.makeText(context, "Failure: " + message, Toast.LENGTH_LONG).show();
                            user.setType(LoggedInStates.NOT_LOGGED_IN);
                            User.saveUser(context, user);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        System.out.println("login was not succesful \n");
                        Log.d("USERController", "my user: " + user.toString());
                        Toast.makeText(context, "Failure: " + error, Toast.LENGTH_LONG).show();
                        user.setType(LoggedInStates.NOT_LOGGED_IN);
                        User.saveUser(context, user);
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }


    public void sendPostChangeUser(Context context, User user, String url) {
        JSONObject json = null;
        try {
            // Convert object to JSON object
            json = new JSONObject(new Gson().toJson(user));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (user == null) {
            return;
        }
        System.out.println("JSON string:\n " + json.toString());

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println("response: ");

                        System.out.println(response.toString() + "\n\n");
                        String message = response.optString("message");

                        if (message.equals("success")) {
                            System.out.println("user changed\n");
                            Toast.makeText(context, "User changed Successful", Toast.LENGTH_LONG).show();

                            User.saveUser(context, user);
                            if (context != null) {
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }

                        } else {
                            System.out.println("Could not change user\n");
                            Toast.makeText(context, "Failure: " + message, Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Could not change user\n");
                        Toast.makeText(context, "Failure: " + error, Toast.LENGTH_LONG).show();
                        error.printStackTrace();

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void sendGetRequest(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("id") && response.has("username") && response.has("email")) {
                                // Create a Friend object from the JSON object
                                Friend friend = new Friend();
                                friend.setId(response.getInt("id"));
                                friend.setUsername(response.getString("username"));
                                friend.setEmail(response.getString("email"));

                                User user = User.getUser(context);
//                                if (user != null && user.getFriendsList() != null) {
//                                    user.setFriendsList(new ArrayList<>());
//                                }
                                user.addFriendsToList(friend);
                                User.saveUser(context, user);
                                // Do something with the Friend object (e.g. update UI)
                            } else {
                                // The requested data is not in the response
                                Log.e("GetRequest", "Requested data is missing from response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getUserInfo(Context context, String url, Integer id, TextView status, Button accept, Button decline) {
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //We are going to set the list of courses and extracurricular activities to the user

                System.out.println(response.toString());

                try {
                    //Used to determine if a parent has requested to be linked with the user
                    User user = User.getUser(context);
                    int parentReq = response.getInt("parentReq");
                    ParentController controller = new ParentController();
                    controller.getParentRequestInfo(context, URL.URL_GET_PARENT_INFO, parentReq, status, accept, decline);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void acceptParentReq(Context context, String url, Integer id, String parentName) {
        url = url + "/" + id + "/AcceptParent/" + parentName;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    if (response.getString("message") != null && response.getString("message").equals("success")) {
                        Toast toast = Toast.makeText(context, "Request Accepted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void declineParentRequest(Context context, String url, Integer id, String parentName) {
        url = url + "/" + id + "/DeclineParent/" + parentName;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    if (response.getString("message") != null && response.getString("message").equals("success")) {
                        Toast toast = Toast.makeText(context, "Request Declined", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        Toast toast = Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void getParent(Context context, String url, Integer id, TextView textView) {
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String parent = response.getString("parentName");
                    textView.setText("Parent: " + parent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    public void getAdvisor(Context context, String url, Integer id, TextView textView) {
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String advisor = response.getString("advisorName");
                    textView.setText("Advisor: " + advisor);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }






}
