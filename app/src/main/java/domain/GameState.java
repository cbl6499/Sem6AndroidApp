package domain;

import com.example.clemensb.a177147.BoardActivity;
import com.example.clemensb.a177147.User;
import com.example.clemensb.a177147.UserSessionManagement;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.util.HashMap;
import java.util.List;

public class GameState {

    private String[][] state;
    private int score;
    private static GameState instance;
    private static int FINAL_SCORE = 117147;
    boolean won = false;

    public static GameState getInstance(){
        if(instance == null){
            instance = new GameState();
        }
        return instance;
    }

    private GameState(){
        init(4, 4);
    }

    public void init(int rows, int columns){
        this.state = new String[4][4];
        clear();
    }

    public void setWon(boolean won){
        this.won = won;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return this.score;
    }

    public boolean getWin(){
        return won;
    }

    public void clear(){
        for(int i = 0; i < this.state.length; i++){
            for(int j = 0; j < this.state[i].length; j++){
                this.state[i][j] = "";
            }
        }
    }

    public static void deleteState(){
        instance = null;
    }

    public void setState(String[][] state){
        this.state = state;
    }

    public String[][] getState(){
        return this.state;
    }

    public boolean isEmptyState(){
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                if(!state[i][j].equals("")){
                    return false;
                }
            }
        }
        return true;
    }

    public int evaluateState(){
        if(!won){
            for(int i = 0; i < state.length; i++){
                for(int j = 0; j < state[i].length; j++){
                    if(state[i][j].equals(FINAL_SCORE+"")) {
                        won = true;
                        return 1;
                    }
                }
            }
        }
        boolean merge = false;
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                if(i != 0 && j != 0 && i < state.length-1 && j < state.length - 1){ //Nicht am Rand
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j-1]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                } else if(i == 0 && j != 0 && i < state.length-1 && j < state.length -1){ //Oberer Rand
                    if(canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j-1]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                } else if(i != 0 && j == 0 && i < state.length-1 && j < state.length - 1){ //Linker Rand
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                } else if(i != 0 && j != 0 && i == state.length-1 && j < state.length - 1){ //Unterer Rand
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i][j-1]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                } else if(i != 0 && j != 0 && i < state.length-1 && j == state.length - 1){ //Rechter Rand
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j-1])){
                        merge = true;
                    }
                } else if(i == 0 && j == 0 && i < state.length-1 && j < state.length - 1){ //Links oben
                    if(canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                } else if(i == 0 && j != 0 && i < state.length-1 && j == state.length - 1){ //Rechts oben
                    if(canMerge(state[i][j], state[i+1][j]) || canMerge(state[i][j], state[i][j-1])){
                        merge = true;
                    }
                } else if(i != 0 && j != 0 && i == state.length-1 && j == state.length - 1){ //Rechts unten
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i][j-1])){
                        merge = true;
                    }
                } else if(i != 0 && j == 0 && i == state.length-1 && j < state.length - 1){ //Links unten
                    if(canMerge(state[i][j], state[i-1][j]) || canMerge(state[i][j], state[i][j+1])){
                        merge = true;
                    }
                }
            }
        }
        if (merge) {
            return 0;
        } else {
            return -1;
        }
    }


    public GameState loadState(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUserRef = mRootRef.child("GameState");
        mUserRef.orderByKey().equalTo(UserSessionManagement.getInstance().getUsername()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //convertPersistentStateToGameState((HashMap)dataSnapshot.getValue());
                GameState currentState = GameState.getInstance();
                currentState.setScore(dataSnapshot.getValue(BoardActivity.PersistentState.class).getCurrentScore());
                currentState.setState(convertListToStringArray(dataSnapshot.getValue(BoardActivity.PersistentState.class).getState()));
                currentState.setWon(dataSnapshot.getValue(BoardActivity.PersistentState.class).getWin());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GameState currentState = GameState.getInstance();
                currentState.setScore(dataSnapshot.getValue(BoardActivity.PersistentState.class).getCurrentScore());
                currentState.setState(convertListToStringArray(dataSnapshot.getValue(BoardActivity.PersistentState.class).getState()));
                currentState.setWon(dataSnapshot.getValue(BoardActivity.PersistentState.class).getWin());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GameState currentState = GameState.getInstance();
                currentState.setScore(dataSnapshot.getValue(BoardActivity.PersistentState.class).getCurrentScore());
                currentState.setState(convertListToStringArray(dataSnapshot.getValue(BoardActivity.PersistentState.class).getState()));
                currentState.setWon(dataSnapshot.getValue(BoardActivity.PersistentState.class).getWin());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                GameState currentState = GameState.getInstance();
                currentState.setScore(dataSnapshot.getValue(BoardActivity.PersistentState.class).getCurrentScore());
                currentState.setState(convertListToStringArray(dataSnapshot.getValue(BoardActivity.PersistentState.class).getState()));
                currentState.setWon(dataSnapshot.getValue(BoardActivity.PersistentState.class).getWin());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });;//(BoardActivity.PersistentState) mUserRef.child(UserSessionManagement.getInstance().getUsername());
        return this;
    }

    private void convertPersistentStateToGameState(HashMap<String, Object> value){
        //BoardActivity.PersistentState state = (BoardActivity.PersistentState) value;
      /*  GameState currentState = GameState.getInstance();
        currentState.setScore(dataSnapshot.getValue(BoardActivity.PersistentState.class).getScore());
        currentState.setScore((int)(long)value.get("currentScore"));//((BoardActivity.PersistentState) value).getCurrentScore());
        currentState.setState(convertListToStringArray((List<BoardActivity.ListElement>)value.get("state")));
        currentState.setWon((Boolean) value.get("won"));*/
    }

    private String[][] convertListToStringArray(List<BoardActivity.ListElement> list){
        String[][] state = new String[4][4];
        for(BoardActivity.ListElement element : list){
            state[element.getCoordinate().getX()][element.getCoordinate().getY()] = element.getValue();
        }
        return state;
    }

    private boolean canMerge(String a, String b){
        if(a.equals(b)){
            return true;
        }
        return false;
    }
}
