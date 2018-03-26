package com.example.clemensb.a177147;

import android.support.annotation.NonNull;

/**
 * Created by ClemensB on 19.03.18.
 */

public class User implements Comparable<User>{

    public String userName;
    public Integer score;


    public User(Integer s){
        score = s;
    }

    public User(){}

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean equals(User u){
        if(u.getUserName().equals(userName)){
            return true;
        }
        return false;
    }


    @Override
    public int compareTo(@NonNull User user) {
        return user.score - score;
    }
}
