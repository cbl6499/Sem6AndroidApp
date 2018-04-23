package domain;

public class GameState {

    private String[][] state;
    private static GameState instance;

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

    public void clear(){
        for(int i = 0; i < this.state.length; i++){
            for(int j = 0; j < this.state[i].length; j++){
                this.state[i][j] = "";
            }
        }
    }

    public void setState(String[][] state){
        this.state = state;
    }

    public String[][] getState(){
        return this.state;
    }
}
