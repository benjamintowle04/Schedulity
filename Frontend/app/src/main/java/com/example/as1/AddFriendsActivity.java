package com.example.as1;

import static com.example.as1.Utils.URL.URL_GET_USER;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Adapters.FriendsAdapter;
import com.example.as1.Controller.FriendsController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.User;

public class AddFriendsActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        User user = User.getUser(AddFriendsActivity.this);
        SearchView searchView = findViewById(R.id.searchView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (user.getAccountType() == AccountType.PARENT_USER) {
            searchView.setQueryHint("Search for your Child");
            getSupportActionBar().setIcon(R.drawable.baseline_child_care_24);
            getSupportActionBar().setTitle("  Add Dependents");
        }
        else if (user.getAccountType() == AccountType.ADVISER) {
            searchView.setQueryHint("Search Students");
            getSupportActionBar().setIcon(R.drawable.baseline_blind_24);
            getSupportActionBar().setTitle("  Add Students");
        }
        else {
            getSupportActionBar().setIcon(R.drawable.baseline_group_24);
            getSupportActionBar().setTitle("  Add Friends");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);

        FriendsController controller = new FriendsController();
        String url = URL_GET_USER.replace("{id}", user.getId().toString());
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