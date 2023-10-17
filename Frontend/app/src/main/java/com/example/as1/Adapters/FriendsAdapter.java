package com.example.as1.Adapters;

import static com.example.as1.Utils.URL.URL_POST_REMOVE_FRIEND;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.as1.Controller.FriendsController;
import com.example.as1.CourseSummaryActivity;
import com.example.as1.FriendsDetailActivity;
import com.example.as1.Models.Friend;
import com.example.as1.Models.User;
import com.example.as1.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Friend> implements View.OnClickListener, SearchView.OnQueryTextListener {

    private final Context context;
    private final List<Friend> friendsList;
    private final List<Friend> filteredFriendsList;
    private Boolean delete;

    public FriendsAdapter(Context context, List<Friend> friendsList) {
        super(context, 0, friendsList);
        this.context = context;
        this.friendsList = new ArrayList<>(friendsList); // Make a copy of the original friendsList
        this.filteredFriendsList = new ArrayList<>(friendsList); // Initialize filteredFriendsList with friendsList
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_list_tile, parent, false);

        if (position < filteredFriendsList.size()) {
            TextView fullNameTextView = rowView.findViewById(R.id.fullnametext);
            TextView userNameTextView = rowView.findViewById(R.id.usernametext);

            Friend friend = filteredFriendsList.get(position); // Use filteredFriendsList instead of friendsList
            fullNameTextView.setText(friend.getUsername());
            userNameTextView.setText(friend.getEmail());
            // Set the position of the clicked item as a tag on the rowView
            rowView.setTag(position);

            // Set the OnClickListener to the rowView
            rowView.setOnClickListener(this);

        } else {
            // Hide the row view if the position is out of range
            rowView.setVisibility(View.GONE);
        }
        return rowView;
    }

    @Override
    public void onClick(View view) {
        // Get the position of the clicked item
        int position = (Integer) view.getTag();

        // Get the Friends object for the clicked item
        Friend friend = filteredFriendsList.get(position); // Use filteredFriendsList instead of friendsList
        Intent fromIntent = ((Activity) context).getIntent();
        if (!delete) {
            if (fromIntent.getStringExtra("from") != null && fromIntent.getStringExtra("from").equals("fromViewCourses")) {
                Intent intent = new Intent(context, CourseSummaryActivity.class);
                intent.putExtra("friendName", friend.getUsername());
                intent.putExtra("id", friend.getId());
                intent.putExtra("from", "fromViewCourses");
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, FriendsDetailActivity.class);
                intent.putExtra("friendName", friend.getUsername());
                intent.putExtra("id", friend.getId());
                context.startActivity(intent);
            }
        }
        else {
            FriendsController controller = new FriendsController();
            String url = URL_POST_REMOVE_FRIEND.replace("{id}", User.getUser(context).getId().toString());
            controller.sendPostRequest(context, friend.getId(), url, delete);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // This method is called when the user presses the search button
        // Perform the search operation here using the query string
        return true;
    }

    @Override
    public boolean onQueryTextChange(String str) {
        ArrayList<Friend> filteredFriends = new ArrayList<>();
        filteredFriendsList.clear();

        for (Friend friend : friendsList) {
            if (friend.getUsername().toLowerCase().contains(str.toLowerCase())) {
                filteredFriends.add(friend);
            }
        }

        filteredFriendsList.addAll(filteredFriends);

        notifyDataSetChanged();

        return true;
    }

    public void initSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(this);
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

}
