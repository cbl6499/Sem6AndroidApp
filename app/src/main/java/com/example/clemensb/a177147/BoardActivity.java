package com.example.clemensb.a177147;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import domain.Coordinate;
import domain.GameState;

/**
 * Created by Jan Fleisch
 * Them Logix by David Hutter und Diego
 */

public class BoardActivity extends Activity {

    private int score = 0;
    // Game view
    SquareLayout gameview;
    Button[][] board2DArray = new Button[4][4];

    TextView scoreView;
    TextView highScoreView;
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
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.FeedActivityThemeDark);
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.FeedActivityThemeLight);
        }

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

        scoreView = findViewById(R.id.score);
        highScoreView = findViewById(R.id.highscore);
        //highScoreView.setText("99999");
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUserRef = mRootRef.child("UserHighScore");
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
                    highScoreView.setText(""+(int)(long)snap.getValue());
                } else {
                    highScoreView.setText("0");
                    Log.d("no highscore", "NO HIGHSCORE");
                }
                //System.out.println("Sorted: ");
                //sortUsers();
                //System.out.println("Datasnapshot: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mUserRef.orderByChild(UserSessionManagement.getInstance().getName()).addValueEventListener(new ValueEventListener() {
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
                    highScoreView.setText(""+(int)(long)snap.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //init board

       // GameState.getInstance().loadState();
        recoverState();
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
                saveState();
            }
            public void onSwipeRight() {
                shiftRight();
                Log.d("Right", "Right");
                saveState();
            }
            public void onSwipeLeft() {
                shiftLeft();
                Log.d("Left", "Left");
                saveState();
            }
            public void onSwipeBottom() {
                shiftBottom();
                Log.d("Botoom", "Bottom");
                saveState();
            }
        });

    }

    private void initBoard(){
        this.score = 0;
        for(int i = 0; i < board2DArray.length; i++){
            for(int j = 0; j < board2DArray[i].length; j++){
                board2DArray[i][j].setText("");
            }
        }
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
        int row = rand.nextInt(board2DArray.length);
        int column = rand.nextInt(board2DArray[0].length);
        return new Coordinate(row, column);
    }

    private void merge(Coordinate a, Coordinate b){
        //int result = 1;
        //if(!isEmptyField(a.getX(), a.getY()) && !isEmptyField(b.getX(), b.getY())) {
            int valueA = getIntValue((String) board2DArray[a.getX()][a.getY()].getText());
            int valueB = getIntValue((String) board2DArray[b.getX()][b.getY()].getText());
            int result = Math.abs(valueA);
            if(valueB != -1){
                result = Math.abs(valueA) * 3;//Math.abs(valueB); //*3
            }
        //}
        if(result != 1) {
            board2DArray[a.getX()][a.getY()].setText(result + "");
            board2DArray[b.getX()][b.getY()].setText("");
            //if(!isEmptyField(a.getX(), a.getY()) && !isEmptyField(b.getX(), b.getY())){
                score += result;
                scoreView.setText(score + "");
            //}
        }

    }

    private void resetBoard(){
      initBoard();
    }

    private void shiftTop(){
        boolean shifted = false;
        for (int i = 0; i < board2DArray.length - 1; i++) {
            for (int j = 0; j < board2DArray[i].length; j++) {
                boolean merged = false;
                if (board2DArray[i][j].getText().equals(board2DArray[i+1][j].getText()) || isEmptyField(i+1,j)) {
                    merge(new Coordinate(i,j), new Coordinate(i+1,j));
                    shifted = true;
                    merged = true;
                }
                if (needsShift(board2DArray,j)) {
                    //shifted = true;
                    for(int k = 0; k < board2DArray.length; k++){
                        if (isEmptyField(k,j)) {
                            if(k < board2DArray[k].length - 1){
                                shift(new Coordinate(k, j), new Coordinate(k+1, j));
                                shifted = true;
                            }
                        }
                    }
                }
                if (board2DArray[i][j].getText().equals(board2DArray[i+1][j].getText()) || isEmptyField(i+1,j) && !merged) {
                    merge(new Coordinate(i,j), new Coordinate(i+1,j));

                }
            }
        }
        if(shifted) {
            spawnNumber();
        }
    }

    private void shiftLeft(){
        boolean shifted = false;
        for(int i = 0; i < board2DArray.length; i++) {
            for (int j = 0; j < board2DArray[i].length -1; j++) {
                boolean merged = false;
                if (board2DArray[i][j].getText().equals(board2DArray[i][j+1].getText()) || isEmptyField(i, j+1)) {
                    merge(new Coordinate(i, j), new Coordinate(i, j+1));
                    shifted = true;
                    merged = true;
                }
                if(needsShift(board2DArray[i])){
                    //shifted = true;
                    for(int k = 0; k < board2DArray[i].length; k++){
                        if (isEmptyField(i,k)) {
                            if(k < board2DArray[i].length - 1){
                                shift(new Coordinate(i, k), new Coordinate(i, k+1));
                                shifted = true;
                            }
                        }
                    }
                    if (board2DArray[i][j].getText().equals(board2DArray[i][j+1].getText()) || isEmptyField(i, j+1) && !merged) {
                        merge(new Coordinate(i, j), new Coordinate(i, j+1));

                    }
                }
            }
        }
        if(shifted) {
            spawnNumber();
        }
    }

    private void shiftRight(){
        boolean shifted = false;
        for(int i = 0; i < board2DArray.length; i++) {
            for (int j = board2DArray[i].length - 1; j > 0; j--) {
                boolean merged = false;
                if (board2DArray[i][j-1].getText().equals(board2DArray[i][j].getText()) || isEmptyField(i, j-1)) {
                    merge(new Coordinate(i, j), new Coordinate(i, j-1));
                    shifted = true;
                    merged = true;
                }
                if(needsShift(board2DArray[i])){
                    //shifted = true;
                    for(int k = board2DArray[i].length-1; k > 0; k--){
                        if (isEmptyField(i,k)) {
                            if(k < board2DArray[i].length){//k > 0){
                                shift(new Coordinate(i, k), new Coordinate(i, k-1));
                                shifted = true;
                            }
                        }
                    }
                    if (board2DArray[i][j-1].getText().equals(board2DArray[i][j].getText()) || isEmptyField(i, j-1) && !merged) {
                        merge(new Coordinate(i, j), new Coordinate(i, j-1));
                    }
                }
            }
        }
        if(shifted) {
            spawnNumber();
        }
    }


    private void shiftBottom(){
        boolean shifted = false;
        for (int i = board2DArray.length-1; i > 0; i--) {
            for (int j = 0; j < board2DArray[i].length; j++) {
                boolean merged = false;
                if (board2DArray[i-1][j].getText().equals(board2DArray[i][j].getText()) || isEmptyField(i-1,j)) {
                    merge(new Coordinate(i,j), new Coordinate(i-1,j));
                    shifted = true;
                    merged = true;
                }
                if (needsShift(board2DArray,j)) {
                    //shifted = true;
                    for(int k = board2DArray.length-1; k > 0; k--){
                        if (isEmptyField(k,j)) {
                            if(k < board2DArray[k].length){
                                shift(new Coordinate(k, j), new Coordinate(k-1, j));
                                shifted = true;
                            }
                        }
                    }
                }
                if (board2DArray[i-1][j].getText().equals(board2DArray[i][j].getText()) || isEmptyField(i-1,j) && !merged) {
                    merge(new Coordinate(i,j), new Coordinate(i-1,j));
                }
            }
        }
        if(shifted) {
            spawnNumber();
        }
    }


    private void spawnNumber(){
        Random rand = new Random();
        List<Coordinate> free = new ArrayList<>();
        for(int i = 0; i < board2DArray.length; i++){
            for(int j = 0; j < board2DArray[i].length; j++){
                if(getIntValue((String)board2DArray[i][j].getText()) == -1){
                    free.add(new Coordinate(i, j));
                }
            }
        }
        if(free.size() > 0) {
            //animation
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in


            int value = rand.nextInt(free.size());
            board2DArray[free.get(value).getX()][free.get(value).getY()].setText("3");
            board2DArray[free.get(value).getX()][free.get(value).getY()].startAnimation(animation);
        }
    }


    private void shift(Coordinate a, Coordinate b){
        board2DArray[a.getX()][a.getY()].setText(board2DArray[b.getX()][b.getY()].getText());
        board2DArray[b.getX()][b.getY()].setText("");
    }


    private boolean needsShift(Button[] array){
        for(int i = 0; i < array.length; i++){
            if(getIntValue(array[i].getText().toString()) != -1){
                return true;
            }
        }
        return false;
    }

    private boolean needsShift(Button[][] array, int column) {
        for (int i = 0; i < array.length; i++) {
            if (getIntValue(array[i][column].getText().toString()) != -1) {
                return true;
            }
        }
        return false;
    }


    private boolean isEmptyField(int x, int y){
        if(getIntValue((String)board2DArray[x][y].getText()) == -1 || getIntValue((String)board2DArray[x][y].getText()) == 0){
            return true;
        }
        return false;
    }

    private int getIntValue(String s){
        if(!s.equals("") || s.equals("1")) {
            return Integer.parseInt(s);
        }
        return -1;
    }

    private void saveState(){
        GameState state = GameState.getInstance();
        String[][] currentState = convertStateToStringArray();
        state.setState(currentState);
        state.setScore(score);
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUserRef = mRootRef.child("GameState");
        mUserRef.child(UserSessionManagement.getInstance().getUsername()).setValue(new PersistentState(convertArrayToList(), GameState.getInstance().getScore(), GameState.getInstance().getWin(), true));


        final DatabaseReference mHighScoreUserRef = mRootRef.child("UserHighScore");
        //mHighScoreUserRef.child(UserSessionManagement.getInstance().getUsername()).setValue();
        mHighScoreUserRef.orderByChild(UserSessionManagement.getInstance().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        mHighScoreUserRef.child(UserSessionManagement.getInstance().getUsername()).setValue(GameState.getInstance().getScore());
                    }
                } else {
                    mHighScoreUserRef.child(UserSessionManagement.getInstance().getUsername()).setValue(GameState.getInstance().getScore());
                }
                //System.out.println("Sorted: ");
                //sortUsers();
                //System.out.println("Datasnapshot: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(state.evaluateState() != 0){
            Intent intent = new Intent(BoardActivity.this, EvaluateActivity.class);
            startActivity(intent);
        } else {
            Log.d("Playing", "Keep going");
        }
    }

    private String[][] convertStateToStringArray(){
        String[][] state = new String[board2DArray.length][board2DArray[0].length];
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                state[i][j] = board2DArray[i][j].getText().toString();
            }
        }
        return state;
    }

    public void recoverState(){
        GameState state = GameState.getInstance();
        if(!state.isEmptyState()) {
            this.score = state.getScore();
            String[][] savedState = state.getState();
            for (int i = 0; i < board2DArray.length; i++) {
                for (int j = 0; j < board2DArray[i].length; j++) {
                    board2DArray[i][j].setText(savedState[i][j]);
                }
            }
            //GameState.getInstance().loadState();
        } else {
            initBoard();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        saveState();
    }

    public void onTerminate(){
        onDestroy();
    }

    private List<ListElement> convertArrayToList(){
        List<ListElement> state = new ArrayList<>();
        String[][] currentState = GameState.getInstance().getState();
        for(int i = 0; i < currentState.length; i++){
            for(int j = 0; j < currentState[i].length; j++){
                state.add(new ListElement(new Coordinate(i, j), currentState[i][j]));
            }
        }
        return state;
    }

    public static class PersistentState{
        List<ListElement> state;
        int score;
        boolean win;
        boolean loaded = false;

        public PersistentState(){

        }

        public PersistentState(List<ListElement> state, int score, boolean won, boolean loaded){
            this.state = state;
            this.score = score;
            this.win = won;
            this.loaded = loaded;
        }

        public List<ListElement> getState(){
            return this.state;
        }

        public int getCurrentScore(){
            return this.score;
        }

        public boolean getLoaded(){
            return this.loaded;
        }

        public void setLoaded(boolean loaded){
            this.loaded = loaded;
        }

        public boolean getWin(){
            return this.win;
        }

        public void setState(List<ListElement> state){
            this.state = state;
        }

        public void setCurrentScore(int score){
            this.score = score;
        }

        public void setWin(boolean win){
            this.win = win;
        }

    }

    public static class ListElement{
        Coordinate _coordinate;
        String _value;

        public ListElement(){

        }

        public ListElement(Coordinate coordinate, String value){
            _coordinate = coordinate;
            _value = value;
        }

        public String getValue(){
            return _value;
        }

        public Coordinate getCoordinate(){
            return _coordinate;
        }

        public void setValue(String value){
            _value = value;
        }

        public void setCoordinate(Coordinate coordinate){
            _coordinate = coordinate;
        }
    }

}
