package com.example.as1.Controller;

import static com.example.as1.Utils.URL.URL_GET_ONE_USER;

import android.content.Context;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.FriendsAdapter;
import com.example.as1.AddFriendsActivity;
import com.example.as1.Models.Friend;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdvisorController {

    public void sendPostAddStudent(Context context, String url, int myId, Integer stuId) {
        url = url + "/" + myId + "/" + stuId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        String message = response.optString("message");
                        if (message.equals("success")) {
                            String out = "successful add";
                            Toast toast = Toast.makeText(context, out, Toast.LENGTH_LONG);
                            toast.show();
                            UserController userController = new UserController();
                            String url = URL_GET_ONE_USER.replace("{id}", stuId.toString());
                            userController.sendGetRequest(context, url);
                        }
                        else {
                            String out = "error occured";
                            Toast toast = Toast.makeText(context, out, Toast.LENGTH_LONG);
                            toast.show();
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


    //Get the student info of the user for the advisor
    public void getAdvisorStudents(Context context, String url, Integer id) {
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = User.getUser(context);
                try {
                    JSONArray studentsJson = response.getJSONArray("student");
                    for (int i = 0; i < studentsJson.length(); i++) {
                        JSONObject currentStudentJson = studentsJson.getJSONObject(i);
                        String username = currentStudentJson.getString("username");
                        int id = currentStudentJson.getInt("id");
                        String email = currentStudentJson.getString("email");
                        Friend student = new Friend(username, id, email);
                        user.addFriendsToList(student);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                User.saveUser(context, user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void getStudentsToDisplay(Context context, String url, Integer id, ListView listView, SearchView searchView, boolean delete) {
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Friend> studentList = new ArrayList<>();
                User user = User.getUser(context);
                List<Friend> currentStudents = new ArrayList<Friend>();
                List<Integer> currentFriendIds = new ArrayList<Integer>();
                if (user != null && user.getFriendsList() != null) {
                    currentStudents = user.getFriendsList();
                    for (int i = 0; i < currentStudents.size(); i++) {//Collecting id numbers from all friends in the list
                        currentFriendIds.add(currentStudents.get(i).getId());
                    }
                }

                try {
                    JSONArray studentsJson = response.getJSONArray("student");
                    for (int i = 0; i < studentsJson.length(); i++) {
                        JSONObject currentStudentJson = studentsJson.getJSONObject(i);
                        String username = currentStudentJson.getString("username");
                        int id = currentStudentJson.getInt("id");
                        String email = currentStudentJson.getString("email");
                        Friend student = new Friend(username, id, email);

                        if (student.getId() != User.getUser(context).getId()) {
                            if ((currentStudents != null && currentFriendIds.contains(student.getId()))
                                    || context instanceof AddFriendsActivity) {
                                studentList.add(student);
                            }
                        }
                    }

                    FriendsAdapter adapter = new FriendsAdapter(context, studentList);
                    listView.setAdapter(adapter);
                    adapter.setDelete(delete);
                    adapter.initSearchView(searchView);


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

        queue.add(jsonObjectRequest);
    }

}
