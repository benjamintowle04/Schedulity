package com.example.as1;

import static com.example.as1.Utils.URL.URL_GET_FRIENDS;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Controller.FriendsController;
import com.example.as1.Models.User;

public class RemoveFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_friends);

        SearchView searchView = findViewById(R.id.searchView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.baseline_group_24);
        getSupportActionBar().setTitle("  Remove Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView listView = findViewById(R.id.listView);

        FriendsController controller = new FriendsController();
        String url = URL_GET_FRIENDS.replace("{id}", User.getUser(RemoveFriendsActivity.this).getId().toString());
        controller.sendGetRequest(this, url, listView, searchView, false);
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