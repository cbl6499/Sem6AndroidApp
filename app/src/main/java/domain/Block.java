package domain;

/**
 * Created by David on 3/19/2018.
 */

public class Block {

    private int _value;
    private Coordinate _coordinate;

    public Block(){
        _value = 0;
    }

    public Block(int value){
        _value = value;
    }

    public void setValue(int value){
        _value = value;
    }

    public int getValue(){
        return _value;
    }

    public void setCoordinate(Coordinate coordinate){
        _coordinate = coordinate;
    }

    public Coordinate getCoordinate(){
        return _coordinate;
    }

    public boolean isEmpty(){
        if(_value == 0){
            return true;
        }
        return false;
    }

    public boolean canMerge(Block b){
        if(_value == b.getValue()){
            return true;
        }
        return false;
    }

    public void merge(Block b){
        b.setValue(0);
        _value *= 3;
    }
}
