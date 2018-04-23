package com.example.clemensb.a177147;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
            evaluateText.setText("Yay, you won!");
            resumeButton.setText("Continue Playing!");
        } else {
            evaluateText.setText("OMG, you lost");
            resumeButton.setText("Back to game");
        }
        resumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EvaluateActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EvaluateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
