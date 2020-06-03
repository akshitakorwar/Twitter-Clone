package com.example.twitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_feed);

        setTitle("Your Feed");
        Intent intent = getIntent();
        //intent.getStringExtra("Feed");

        final ListView listView = findViewById(R.id.usersTweetList);
        final List<Map<String,String>> itemDataList = new ArrayList<>();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("UserTweet");
        //parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser());
        parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        Log.i("followers", ParseUser.getCurrentUser().getList("isFollowing").toString());
        parseQuery.addDescendingOrder("updatedAt");
        parseQuery.setLimit(20);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject object : objects){
                        Map<String, String> map = new HashMap<>();
                        map.put("tweet", object.getString("tweetPosted"));
                        map.put("username", object.get("username").toString());
                        itemDataList.add(map);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(YourFeed.this, itemDataList, android.R.layout.simple_list_item_2, new String[]{"tweet", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
                    listView.setAdapter(simpleAdapter);
                }
            }
        });
    }
}
