package com.example.clemensb.a177147;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by ClemensB on 16.04.18.
 */

public class UserSessionManagement {

    private static UserSessionManagement instance;

    public static UserSessionManagement getInstance(){
        if(instance == null){
            instance = new UserSessionManagement();
        }
        return instance;
    }

    private UserSessionManagement(){}


    private Boolean login = false;

    private String name;

    private String username;

    private String email;

    public void setUserData(GoogleSignInAccount googleSignInAccount){
        name = googleSignInAccount.getDisplayName();
        email = googleSignInAccount.getEmail();
        username = googleSignInAccount.getDisplayName();
        login = true;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public Boolean getLogin(){
        return login;
    }

    public String getUsername(){return username;}

    public void logout(Context context){
        //name = "";
        //email = "";
        login = false;
        Intent i = new Intent(context, MainActivity.class);
    }

}
