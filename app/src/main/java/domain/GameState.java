package domain;

public class GameState {

    private String[][] state;
    private int score;
    private static GameState instance;
    private static String FINAL_SCORE = "177147";
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
                    if(state[i][j].equals(FINAL_SCORE)) {
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

    private boolean canMerge(String a, String b){
        if(a.equals(b)){
            return true;
        }
        return false;
    }
}
