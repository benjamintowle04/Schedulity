package com.example.as1;

import static com.example.as1.Utils.URL.URL_GET_ADVISOR_INFO;
import static com.example.as1.Utils.URL.URL_GET_FRIENDS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Controller.AdvisorController;
import com.example.as1.Controller.FriendsController;
import com.example.as1.Controller.ParentController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class FriendsActivity extends AppCompatActivity {

//    List<Friend> friendsList = new ArrayList<Friend>();
//    Friend friend0 = new Friend("Robert", 0, "email", null);
//    Friend friend1 = new Friend("Ben", 1, "email", null);
//    Friend friend2 = new Friend("Waves", 2, "email", null);
//    Friend friend3 = new Friend("Lizzy", 3, "email", null);
//

    Toolbar toolbar;
    ListView listView;
    ImageButton addFriendsImageBtn, removeFriendsImageBtn;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        User user = User.getUser(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView);
        addFriendsImageBtn = findViewById(R.id.AddFriendsImageBtn);
        removeFriendsImageBtn = findViewById(R.id.DeleteFriendsImageBtn);
        listView = findViewById(R.id.listView);

        if (user.getAccountType() == AccountType.PARENT_USER) {
            searchView.setQueryHint("Search for your Child");
            getSupportActionBar().setIcon(R.drawable.baseline_child_care_24);
            getSupportActionBar().setTitle(" Dependents");
            if (user.getFriendsList() != null && user.getFriendsList().size() == 1) {
                addFriendsImageBtn.setVisibility(View.GONE);
            }
        }
        else if (user.getAccountType() == AccountType.ADVISER) {
            searchView.setQueryHint("Search Students");
            getSupportActionBar().setIcon(R.drawable.baseline_blind_24);
            getSupportActionBar().setTitle(" Students");
        }
        else {
            getSupportActionBar().setIcon(R.drawable.baseline_group_24);
            getSupportActionBar().setTitle(" Friends");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent fromIntent = getIntent();
        String from = fromIntent.getStringExtra("from");
        if (from != null && from.equals("fromViewCourses")) {
            addFriendsImageBtn.setVisibility(View.GONE);
            removeFriendsImageBtn.setVisibility(View.GONE);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("LIST_VIEW", "Item is clicked @ position: " + position);
            }
        });
        addFriendsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, AddFriendsActivity.class);
                intent.putExtra("activity", "fromAddFriends");
                startActivity(intent);
            }
        });
        removeFriendsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, RemoveFriendsActivity.class);
                startActivity(intent);
            }
        });

        if (user.getAccountType() == AccountType.ADVISER) {
            AdvisorController advisorController = new AdvisorController();
            String url = URL_GET_ADVISOR_INFO;
            advisorController.getStudentsToDisplay(this, url, user.getId(), listView, searchView, false);
        }
        else if (user.getAccountType() == AccountType.PARENT_USER) {
            ParentController parentController = new ParentController();
            String url = URL.URL_GET_PARENT_INFO;
            parentController.getChildForDisplay(this, url, user.getId(), listView, searchView, false);
        }
        else {
            FriendsController controller = new FriendsController();
            String url = URL_GET_FRIENDS.replace("{id}", User.getUser(FriendsActivity.this).getId().toString());
            controller.sendGetRequest(this, url, listView, searchView, false);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getUser(FriendsActivity.this);
        //get your friends
        if (user.getFriendsList() != null && user.getFriendsList().size() == 1 && user.getAccountType() == AccountType.PARENT_USER) {
            addFriendsImageBtn.setVisibility(View.GONE);
        }

        if (user.getAccountType() == AccountType.ADVISER) {
            AdvisorController advisorController = new AdvisorController();
            String url = URL_GET_ADVISOR_INFO;
            advisorController.getStudentsToDisplay(this, url, user.getId(), listView, searchView, false);
        }
        else if (user.getAccountType() == AccountType.PARENT_USER) {
            //TODO tonight
        }
        else {
            FriendsController controller = new FriendsController();
            String url = URL_GET_FRIENDS.replace("{id}", User.getUser(FriendsActivity.this).getId().toString());
            controller.sendGetRequest(this, url, listView, searchView, false);
        }

    }

}