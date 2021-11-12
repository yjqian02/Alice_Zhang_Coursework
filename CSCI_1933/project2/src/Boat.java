// Written by zhan6698 and bogir001
import java.util.Random;// import random
public class Boat {
    private int size;
    private boolean horizontal;
    private Cell[] cells; // cells each boat takes up
    public Boat(int s){
        size = s;
        Random r = new Random();
        horizontal = r.nextBoolean(); // random orientation
    }
    public int get_size(){
        return size;
    }
    public void set_size(int s){
        size = s;
    }
    public boolean is_horizontal(){
        return horizontal;
    }
    public void set_horizontal(boolean o){
        horizontal = o;
    }
    public Cell[] get_cells(){
        return cells;
    }
    public void set_cells(Cell[] l){
        cells = l;
    }
}
