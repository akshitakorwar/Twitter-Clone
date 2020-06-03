package com.example.twitter;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class ParseActivity extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("952ffce7720668c62f5ce351abcc807714f17786")
                .clientKey("594f7136e1b4f6373983b680c8a94b6cac2dc568")
                .server("http://18.222.54.255:80/parse/")
                .build()
        );

        final ParseObject score = new ParseObject("TestStuff");
        score.put("playerName", "Simba");
        score.put("totalScore", 50000);

        score.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("Message", "Success!");
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        /*ParseUser user = new ParseUser();
        user.setUsername("Jlo");
        user.setPassword("password");
        user.setEmail("jlo@foo.com");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("Message", "Successfully signed up!");
                }
                else{
                    e.printStackTrace();
                }
            }
        });*/

        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
