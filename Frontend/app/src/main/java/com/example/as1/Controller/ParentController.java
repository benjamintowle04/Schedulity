package com.example.as1.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.FriendsAdapter;
import com.example.as1.AddFriendsActivity;
import com.example.as1.MainActivity;
import com.example.as1.Models.Friend;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentController {
    public void requestToParent(Context context, String url, int parentId, int childId) {
        url = url + "/" + parentId + "/" + childId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    if (response.getString("message") != null && response.getString("message").equals("success")) {
                        System.out.println("Request Made");
                        Toast toast = Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG);
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

    public void getParentRequestInfo(Context context, String url, int parentId, TextView status, Button accept, Button decline) {
        url = url + "/" + parentId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                User user = User.getUser(context);
                try {
                    String email = response.getString("email");
                    String username = response.getString("username");
                    int id = response.getInt("id");
                    Friend parent = new Friend(username, id, email);
                    status.setText("You have 1 request from " + username);

                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UserController controller = new UserController();
                            controller.acceptParentReq(context, URL.URL_GET_USER_INFO, user.getId(), username);
                            user.setParent(parent);
                            user.setParentReq(0);
                            User.saveUser(context, user);
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                    });

                    decline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UserController controller = new UserController();
                            controller.declineParentRequest(context, URL.URL_GET_USER_INFO, user.getId(), username);
                            user.setParentReq(0);
                            User.saveUser(context, user);
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                    });
                }
                catch (JSONException e) {
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

    public void getChildForDisplay(Context context, String url, int parentId, ListView listView, SearchView searchView, Boolean delete) {
        url = url + "/" + parentId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Friend> childList = new ArrayList<>();

                try {
                    if (!response.getString("childName").equals("")) {
                        JSONObject childJson = response.getJSONObject("child");
                        Friend child = new Friend();
                        child.setId(childJson.getInt("id"));
                        child.setUsername(childJson.getString("username"));
                        child.setEmail(childJson.getString("email"));
                        childList.add(child);
                        FriendsAdapter adapter = new FriendsAdapter(context, childList);
                        listView.setAdapter(adapter);
                        adapter.setDelete(delete);
                        adapter.initSearchView(searchView);
                    }

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
