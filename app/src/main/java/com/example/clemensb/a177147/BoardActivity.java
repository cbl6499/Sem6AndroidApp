package com.example.clemensb.a177147;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

import domain.Coordinate;

public class BoardActivity extends Activity {

    // Game view
    LinearLayout gameview;
    Button[][] board2DArray = new Button[4][4];
    //Line1
    Button b00;
    Button b01;
    Button b02;
    Button b03;
    //Line2
    Button b10;
    Button b11;
    Button b12;
    Button b13;
    //Line3
    Button b20;
    Button b21;
    Button b22;
    Button b23;
    //Line4
    Button b30;
    Button b31;
    Button b32;
    Button b33;

    //Buttons
    Button backButton;
    Button resetButton;

    //Sonstiges
    int zzz;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //Game View - View by ID from XML
        gameview = findViewById(R.id.gameview);

        b00 = findViewById(R.id.b00);
        b01 = findViewById(R.id.b01);
        b02 = findViewById(R.id.b02);
        b03 = findViewById(R.id.b03);

        b10 = findViewById(R.id.b10);
        b11 = findViewById(R.id.b11);
        b12 = findViewById(R.id.b12);
        b13 = findViewById(R.id.b13);

        b20 = findViewById(R.id.b20);
        b21 = findViewById(R.id.b21);
        b22 = findViewById(R.id.b22);
        b23 = findViewById(R.id.b23);

        b30 = findViewById(R.id.b30);
        b31 = findViewById(R.id.b31);
        b32 = findViewById(R.id.b32);
        b33 = findViewById(R.id.b33);

        // Other Elements by ID from XML
        backButton = findViewById(R.id.backButton);
        resetButton = findViewById(R.id.resetButton);

        //Fill 2 Dimensional Array
        board2DArray[0][0] = b00;
        board2DArray[0][1] = b01;
        board2DArray[0][2] = b02;
        board2DArray[0][3] = b03;

        board2DArray[1][0] = b10;
        board2DArray[1][1] = b11;
        board2DArray[1][2] = b12;
        board2DArray[1][3] = b13;

        board2DArray[2][0] = b20;
        board2DArray[2][1] = b21;
        board2DArray[2][2] = b22;
        board2DArray[2][3] = b23;

        board2DArray[3][0] = b30;
        board2DArray[3][1] = b31;
        board2DArray[3][2] = b32;
        board2DArray[3][3] = b33;

        //init board

        initBoard();
        //Button Click
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                resetBoard();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        //Swipe
        gameview.setOnTouchListener(new OnSwipeTouchListener(BoardActivity.this) {
            public void onSwipeTop() {
                shiftTop();
                Log.d("Top", "Top");
            }
            public void onSwipeRight() {
                shiftRight();
                Log.d("Right", "Right");
            }
            public void onSwipeLeft() {
                shiftLeft();
                Log.d("Left", "Left");
            }
            public void onSwipeBottom() {
                shiftBottom();
                Log.d("Botoom", "Bottom");
            }

        });
    }

    private void initBoard(){
        Coordinate c1 = getRandomEmptyCoordinate();
        Coordinate c2 = getRandomEmptyCoordinate();
        while(c1.equals(c2)){
            c2 = getRandomEmptyCoordinate();
        }
        board2DArray[c1.getX()][c1.getY()].setText("3");
        board2DArray[c2.getX()][c2.getY()].setText("3");
    }

    public Coordinate getRandomEmptyCoordinate(){
        Coordinate c = getRandomCoordinate();
        while(!board2DArray[c.getX()][c.getY()].getText().equals("")){
            c = getRandomCoordinate();
        }
        return c;
    }

    public Coordinate getRandomCoordinate(){
        Random rand = new Random();
        int row = rand.nextInt(4);
        int column = rand.nextInt(4);
        return new Coordinate(row, column);
    }

    private boolean canMerge(Coordinate a, Coordinate b){
        if(board2DArray[a.getX()][a.getY()].getText().equals(board2DArray[b.getX()][b.getY()])){
            return true;
        }
        return false;
    }

    private void merge(Coordinate a, Coordinate b){
        if(canMerge(a, b)) {
            int valueA = Integer.parseInt((String) board2DArray[a.getX()][a.getY()].getText());
            int valueB = Integer.parseInt((String) board2DArray[b.getX()][b.getY()].getText());
            int result = valueA * valueB;
            board2DArray[a.getX()][a.getY()].setText(result + "");
            board2DArray[b.getX()][b.getY()].setText("");
        }
    }

    private void resetBoard(){
      /*  for(int i = 0; i < board2DArray.length; i++) {
            for(int j = 0; j < board2DArray[i].length; j++) {
                if((zzz%2)==0) {
                    board2DArray[i][j].setText("X");
                }else{
                    board2DArray[i][j].setText("0");
                }
            }
        }
        zzz = zzz + 1;*/
      initBoard();
    }

    private void shiftTop(){
        for(int i = 0; i < board2DArray.length; i++) {
            for(int j = 0; j < board2DArray[i].length; j++) {
                board2DArray[i][j].setBackgroundColor(Color.RED);
            }
        }
    }

    private void shiftRight(){
        for(int i = 0; i < board2DArray.length; i++) {
            for(int j = 0; j < board2DArray[i].length; j++) {
                board2DArray[i][j].setBackgroundColor(Color.GREEN);
            }
        }
    }

    private void shiftLeft(){
        for(int i = 0; i < board2DArray.length; i++) {
            for(int j = 0; j < board2DArray[i].length; j++) {
                board2DArray[i][j].setBackgroundColor(Color.BLUE);
            }
        }
    }

    private void shiftBottom(){
        for(int i = 0; i < board2DArray.length; i++) {
            for(int j = 0; j < board2DArray[i].length; j++) {
                board2DArray[i][j].setBackgroundColor(Color.YELLOW);
            }
        }
    }


}
