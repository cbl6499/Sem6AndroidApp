package com.example.clemensb.a177147;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.ArrayList;
import java.util.Locale;

import domain.GameState;

public class MainActivity extends AppCompatActivity {

    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = "SignInActivity";

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    private NotificationUtils mNotificationUtils;

    // Sing out button.

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;

    // TextView to Show Login User Email and Name.
    //TextView LoginUserName, LoginUserEmail;

    Button hsButton, clickButton, exitButton, resumeButton, logoutButton, inviteButton;

    UserSessionManagement user;

    private static final int REQUEST_INVITE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationUtils = new NotificationUtils(this);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        clickButton = (Button) findViewById(R.id.startButton);

        hsButton = (Button) findViewById(R.id.highscoreButton);

        logoutButton = (Button) findViewById(R.id.logoutButton);

        exitButton = (Button) findViewById(R.id.exitButton);

        resumeButton = (Button) findViewById(R.id.resumeButton);

        inviteButton = (Button) findViewById(R.id.inviteButton);

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();



        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignInMethod();
            }
        });

        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameModesActivity.class);
                GameState.deleteState();
                startActivity(intent);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameState.getInstance().loadState();
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mUserRef = mRootRef.child("GameState");
                mUserRef.child("system").setValue(new BoardActivity.PersistentState(new ArrayList<BoardActivity.ListElement>(), 0, false));
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });

                hsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HSTableActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //android.os.Process.killProcess(android.os.Process.myPid());
                //Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                //startActivity(intent);
                //UserSignOutFunction();
                String title = "This is a notification test";
                String author = "Goht di garnix a";

                if(!title.isEmpty() && !author.isEmpty()) {
                    Notification.Builder nb = mNotificationUtils.
                            getAndroidChannelNotification(title, "By " + author);

                    mNotificationUtils.getManager().notify(101, nb.build());
                }
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onInviteClicked();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        user = UserSessionManagement.getInstance();

        check();

    }

    public void check() {
        if(user.getLogin()){
            // Hiding Login in button.
            signInButton.setVisibility(View.GONE);

            clickButton.setVisibility(View.VISIBLE);
            hsButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            inviteButton.setVisibility(View.VISIBLE);
        } else {
            clickButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);
            hsButton.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.GONE);
        }
    }

    // Sign In function Starts From Here.
    public void UserSignInMethod() {

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode) {

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()) {

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                //Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();

                FirebaseUserAuth(googleSignInAccount);

                user.setUserData(googleSignInAccount);

                Toast.makeText(MainActivity.this, user.getName() + "  |  " + user.getEmail(), Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(MainActivity.this, "Login failed, something went wrong!!", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        //Toast.makeText(MainActivity.this, "" + authCredential.getProvider(), Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this, "Welcome " + googleSignInAccount.getDisplayName(), Toast.LENGTH_LONG).show();


        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

                        if (AuthResultTask.isSuccessful()) {

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // Showing Log out button.
                            logoutButton.setVisibility(View.VISIBLE);
                            exitButton.setVisibility(View.VISIBLE);

                            // Hiding Login in button.
                            signInButton.setVisibility(View.GONE);

                            clickButton.setVisibility(View.VISIBLE);
                            hsButton.setVisibility(View.VISIBLE);
                            inviteButton.setVisibility(View.VISIBLE);

                            resumeButton.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UserSignOutFunction() {

        // Sing Out the User.
        firebaseAuth.signOut();

        user.logout(MainActivity.this);

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        // Write down your any code here which you want to execute After Sign Out.

                        // Printing Logout toast message on screen.
                        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();

                    }
                });

        clickButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);
        hsButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);
        inviteButton.setVisibility(View.GONE);

        // After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("177147 App Invite")
                .setMessage("177147")
                .setDeepLink(Uri.parse("https://github.com/cbl6499/Sem6AndroidApp"))
                .setCallToActionText("click here")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}
