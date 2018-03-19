package domain;

import java.util.Random;

/**
 * Created by David on 3/19/2018.
 */

public class Grid {

    private Block[][] _grid;

    public Grid(int x, int y){
        _grid = new Block[x][y];
        init();
    }

    private void init(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[i].length; j++){
                _grid[i][j] = new Block(0);
            }
        }
        Coordinate c1 = getRandomEmptyCoordinate();
        Coordinate c2 = getRandomEmptyCoordinate();
        while(c1.equals(c2)){
            c2 = getRandomEmptyCoordinate();
        }
        _grid[c1.getX()][c1.getY()] = spawnBlock();
        _grid[c2.getX()][c2.getY()] = spawnBlock();
    }

    public Coordinate getRandomEmptyCoordinate(){
        Coordinate c = getRandomCoordinate();
        while(!_grid[c.getX()][c.getY()].isEmpty()){
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

    public Block spawnBlock(){
        return new Block(3);
    }

    public Block getBlock(int x, int y){
        return _grid[x][y];
    }

    //TODO: Stimman alle no ned so
    public void moveLeft(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[0].length; j++){
                if(_grid[i][j].isEmpty()){
                    if(j-1 >= 0) {
                        if (_grid[i][j-1].isEmpty()) {
                            swap(_grid[i][j], _grid[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    public void moveUp(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[0].length; j++){
                if(_grid[i][j].isEmpty()){
                    if(i-1 >= 0) {
                        if (_grid[i-1][j].isEmpty()) {
                            swap(_grid[i][j], _grid[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    public void moveRight(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[0].length; j++){
                if(_grid[i][j].isEmpty()){
                    if(i+1 < _grid[i].length) {
                        if (_grid[i+1][j].isEmpty()) {
                            swap(_grid[i][j], _grid[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    public void moveDown(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[0].length; j++){
                if(_grid[i][j].isEmpty()){
                    if(j+1 < _grid[i].length) {
                        if (_grid[i][j + 1].isEmpty()) {
                            swap(_grid[i][j], _grid[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    private void swap(Block a, Block b){
        Block tmp = a;
        a = b;
        b = tmp;
    }

    public void printGrid(){
        for(int i = 0; i < _grid.length; i++){
            for(int j = 0; j < _grid[i].length; j++){
                System.out.print(_grid[i][j].getValue() + "|");
            }
            System.out.println();
        }
    }
}

