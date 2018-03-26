package com.example.clemensb.a177147;

import android.app.Activity;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clickButton = (Button) findViewById(R.id.startButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, GameModesActivity.class);
                startActivity(intent);
            }
        });

        Button hsButton = (Button) findViewById(R.id.highscoreButton);
        hsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, HSTableActivity.class);
                startActivity(intent);
            }
        });

        Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        System.out.println(Locale.getDefault().getDisplayLanguage().toString());
    }

}
