package com.example.clemensb.a177147;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David on 3/19/2018.
 */

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //updateUI(currentUser);
    }

    private EditText email;
    private EditText passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        email = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.password);
        Button clickButton = (Button) findViewById(R.id.email_sign_in_button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                signIn();
                if(getCurrentUser() != null) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public boolean signIn(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), passwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
        if(getCurrentUser() != null){
            return true;
        }
        return false;
    }

    //TODO: nur platzhalter wegen fehler
    private void updateUI(Object test){

    }

    private FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }
    public String getUserName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = "";
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        return uid;
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
