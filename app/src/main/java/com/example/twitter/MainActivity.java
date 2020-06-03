package com.example.twitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;
    String name;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //userHome();
    }

    public void userHome(){
        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), UsersList.class);
            intent.putExtra("UsersList", "UsersList");
            startActivity(intent);
        }
    }

    public void signIn(View view){
        button = findViewById(R.id.signInButton);
        name = String.valueOf(username.getText());
        pass = String.valueOf(password.getText());

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(name);
        parseUser.setPassword(pass);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo("username", name);
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    ParseUser.logInInBackground(name, pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null && e==null){
                                Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                                userHome();
                            }
                            else{
                                parseUser.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "User signed up successfully", Toast.LENGTH_SHORT).show();
                                            userHome();
                                        } else {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.i("Message", e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });



//        parseUser.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(getApplicationContext(), "User signed up successfully", Toast.LENGTH_SHORT).show();
//                }
//                /*else if(e.getMessage()=="Account already exists for this username"){
//                    ParseUser.logInInBackground(name, pass, new LogInCallback() {
//                        @Override
//                        public void done(ParseUser user, ParseException e) {
//                            if(user!=null && e==null){
//                                Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }*/
//                else{
//                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.i("Message", e.getMessage());
//                }
//            }
//        });


        /*else if(user.isNew()){
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(getApplicationContext(), "User signed up successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("Message", e.getMessage());
                    }
                }
            });
        }*/
    }
}
