package com.example.clemensb.a177147;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.GameState;

public class EvaluateActivity extends AppCompatActivity {

    TextView evaluateText;
    Button resumeButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        evaluateText = findViewById(R.id.textView);
        resumeButton = findViewById(R.id.resumeButton);
        exitButton = findViewById(R.id.exitButton);
        boolean won = GameState.getInstance().getWin();
        if(won){
            evaluateText.setText("Yay, you won!" + "\nYou score: " + GameState.getInstance().getScore());
            resumeButton.setText("Continue Playing!");
        } else {
            evaluateText.setText("OMG, you lost" + "\nYou score: " + GameState.getInstance().getScore());
            resumeButton.setText("Back to game");
        }
        resumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                //Intent intent = new Intent(EvaluateActivity.this, BoardActivity.class);
                //startActivity(intent);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference mUserRef = mRootRef.child("UserHighScore");
                mUserRef.orderByChild(UserSessionManagement.getInstance().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                        DataSnapshot snap = null;
                        for(DataSnapshot ds : childs){
                            if(ds.getKey().equals(UserSessionManagement.getInstance().getUsername())){
                                snap = ds;
                            }
                        }
                        if(snap != null){
                            Log.d("highscore", ""+(int)(long)snap.getValue());
                            if((int)(long)snap.getValue() < GameState.getInstance().getScore()){
                                mUserRef.child(UserSessionManagement.getInstance().getUsername()).setValue(GameState.getInstance().getScore());
                                MainActivity.sendNewHighScoreNotification(GameState.getInstance().getScore());
                            }
                        }
                        //System.out.println("Sorted: ");
                        //sortUsers();
                        //System.out.println("Datasnapshot: " + dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(EvaluateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
