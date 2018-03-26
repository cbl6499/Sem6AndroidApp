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

import java.util.HashMap;
import java.util.Map;

public class HSActivity extends AppCompatActivity {

    Button submit;
    EditText userName;
    EditText score;
    User user;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("UserHighScore");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);

        userName = (EditText) findViewById(R.id.nameEditText);
        score = (EditText) findViewById(R.id.scoreEditText);

        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                addUser();
            }
        });


        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addUser(){
        /*user = new User();
        user.setUserName(userName.getText().toString());
        user.setScore(Integer.parseInt(score.getText().toString()));*/

        //System.out.println(userName.getText().toString() + "    " + Integer.parseInt(score.getText().toString()));

        mUserRef.child(userName.getText().toString()).setValue( new User(Integer.parseInt(score.getText().toString())));


        /*if(!TextUtils.isEmpty(user.getUserName()) || user.getScore().equals(null)){
            //String id = mUserRef.push().getKey();

            mUserRef.setValue(user);

            Toast.makeText(this, "User Highscore added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "A field is empty", Toast.LENGTH_LONG).show();
        }*/
    }
}
