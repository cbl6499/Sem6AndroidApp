package com.example.clemensb.a177147;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameModesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemodes);
        Button clickButton = (Button) findViewById(R.id.classic);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(GameModesActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = (Button) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        /* Alternate Back
        Button backButton = (Button) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameModesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }) ;
        */
    }

}
