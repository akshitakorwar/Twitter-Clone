package com.example.twitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends AppCompatActivity {
    //    EditText editText;
     ParseObject parseObject;
     ArrayList<String> userArrayList = new ArrayList<String>();
     ArrayAdapter<String> userArrayAdapter;
     ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        setTitle("User List");

//        editText = findViewById(R.id.editText);
        Intent intent = getIntent();
        intent.getStringExtra("UsersList");

        listView = findViewById(R.id.listOfUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, userArrayList);
        listView.setAdapter(userArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){
                    Log.i("Info", "Following " + userArrayList.get(i));
                    ParseUser.getCurrentUser().add("isFollowing", userArrayList.get(i));
                    Log.i("Followers List", ParseUser.getCurrentUser().getList("isFollowing").toString());
                }
                else{
                    Log.i("Info", "Unfollowed " + userArrayList.get(i));
                    ParseUser.getCurrentUser().getList("isFollowing").remove(userArrayList.get(i));
                    Log.i("Followers List", ParseUser.getCurrentUser().getList("isFollowing").toString());
                    List temp = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", temp);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseUser user : objects){
                        userArrayList.add(user.getUsername());
                    }
                    userArrayAdapter.notifyDataSetChanged();

                    for(String username : userArrayList){
                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)){
                            listView.setItemChecked(userArrayList.indexOf(username), true);
                        }
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tweet){
            Log.i("Message", "Clicked on Tweet");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View customView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
            final EditText editText = customView.findViewById(R.id.editText);
            parseObject = new ParseObject("UserTweet");

            builder.setTitle("Tweet")
                    .setView(customView)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String tweetSent = editText.getText().toString();
                            Log.i("Tweet Message", tweetSent);
                            parseObject.put("username", ParseUser.getCurrentUser());
                            parseObject.put("tweetPosted", tweetSent);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e ==null){
                                        Log.i("Store Message", "Tweet saved successfully!");
                                    }
                                    else{
                                        Log.i("Store Error Message", e.getMessage());
                                    }
                                }
                            });
                            Toast.makeText(UsersList.this, "Tweet posted", Toast.LENGTH_SHORT).show();
                        }})
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UsersList.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
        else if(item.getItemId() == R.id.feed){
            Intent intent = new Intent(getApplicationContext(), YourFeed.class);
            intent.putExtra("Feed", 0);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.logout){
            ParseUser.logOut();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
