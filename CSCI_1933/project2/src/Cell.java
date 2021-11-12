// Written by zhan6698 and bogir001
public class Cell {
    private int row; // indicates row of cell
    private int col; // indicates column of cell
    private char status; // 4 possible options, indicates status of cell

    public Cell(int row, int col, char status){
        this.row = row;
        this.col = col;
        this.status = status;
    }
    public int get_row(){
        return row;
    }
    public void set_row(int r){
        row = r;
    }
    public int get_col(){
        return col;
    }
    public void set_col(int c){
        col = c;
    }
    public char get_status(){
        return status;
    }
    public void set_status(char c){
        status = c;
    }
}
