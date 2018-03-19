package domain;

/**
 * Created by David on 3/19/2018.
 */

public class Coordinate {

    private int _x;
    private int _y;

    public Coordinate(int x, int y){
        _x = x;
        _y = y;
    }

    public int getX(){
        return _x;
    }

    public int getY(){
        return _y;
    }

    public void setX(int x){
        _x = x;
    }

    public void setY(int y){
        _y = y;
    }

    public boolean equals(Coordinate coordinate){
        if(_x == coordinate.getX() && _y == coordinate.getY()){
            return true;
        }
        return false;
    }
}
