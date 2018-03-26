package com.example.clemensb.a177147;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HSActivity extends AppCompatActivity {

    Button submit;
    Button hsListButton;
    EditText userName;
    EditText score;
    List<User> users;
    User user;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("UserHighScore");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);
        users = new ArrayList<>();
        userName = (EditText) findViewById(R.id.nameEditText);
        score = (EditText) findViewById(R.id.scoreEditText);

        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                addUser();
            }
        });

        hsListButton = (Button) findViewById(R.id.hsListButton);
        hsListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(HSActivity.this, HSTableActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                for(DataSnapshot ds : childs){
                    //System.out.println("Test" + ds.getValue() + " Parent: " + ds.getKey());
                    User newUser = new User();
                    newUser.setUserName(ds.getKey());
                    newUser.setScore(ds.getValue(User.class).getScore());
                    //System.out.println(newUser.getUserName() + " : " + newUser.getScore());
                    users.add(newUser);
                }
                //System.out.println("Sorted: ");
                //sortUsers();
                //System.out.println("Datasnapshot: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addUser(){
        user = new User();
        user.setUserName(userName.getText().toString());
        user.setScore(Integer.parseInt(score.getText().toString()));

        System.out.println(userName.getText().toString() + "    " + Integer.parseInt(score.getText().toString()));
        User currentUser = new User();
        boolean userFound = false;
        for(User u : users){
            if(u.getUserName().equals(userName.getText().toString())){
                currentUser = u;
                userFound = true;
                System.out.println("User found");
            }
        }
        if(userFound){
            if(currentUser.getScore() < Integer.parseInt(score.getText().toString())) {
                System.out.println("Score updated");
                mUserRef.child(currentUser.getUserName()).setValue(new User(Integer.parseInt(score.getText().toString())));
            } else {
                System.out.println("No new highscore");
            }
        } else {
            System.out.println("New User");
            mUserRef.child(userName.getText().toString()).setValue(new User(Integer.parseInt(score.getText().toString())));
        }

        //System.out.println("Key: " + mUserRef.child(userName.getText().toString()).child("score").getRef().toString());

        if(!TextUtils.isEmpty(user.getUserName()) || user.getScore().equals(null)){
            //String id = mUserRef.push().getKey();

            mUserRef.setValue(user);

            Toast.makeText(this, "User Highscore added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "A field is empty", Toast.LENGTH_LONG).show();
        }
    }

    public void sortUsers(){
        Collections.sort(users);
        /*for(int i = 0; i < users.size(); i++){
            System.out.println("Rank "  + (i+1) + " : " + users.get(i).getUserName() + " : " + users.get(i).getScore());
        }*/
    }
}
