// Written by zhan6698 and bogir001
import java.util.Random;// import random

public class Board {
    private Cell[][] board; // board layout
    private Boat[] boats; // holds all the boat objects we need to place
    private int num_boats, rows, cols;
    private int shots; // shots remaining
    private int[] boatLengths;
    private Random r = new Random(); // random object

    public Board(int row, int col) { // setting the game board
        rows = row;
        cols = col;
        board = new Cell[row][col];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Cell(rows, cols, '-'); // setting default status of all cells in game board to be "has not been guessed, no boat present"
            }
        }
        int min = Math.min(rows, cols);

        if (min == 3) {
            boatLengths = new int[]{2};
        } else if (min == 4) {
            boatLengths = new int[]{2, 3};
        } else if (min <= 6) {
            boatLengths = new int[]{2, 3, 3};
        } else if (min <= 8) {
            boatLengths = new int[]{2, 3, 3, 4};
        } else {
            boatLengths = new int[]{2, 3, 3, 4, 5};
        }
        num_boats = boatLengths.length;
        boats = new Boat[num_boats];

        place_boats();
    }

    public void place_boats() { // places boats on board
        for (int i = 0; i < num_boats; i++) {
            while (true) {
                Boat boat = new Boat(boatLengths[i]);
                int[] start = chooseValidPosition(boat); // start gives [row,col] -- coordinate pair

                if (spacesAreAvailable(boat, start)) {
                    updateBoardAndBoatCells(boat, start);
                    boats[i] = boat;
                    break;
                }
            }
        }
    }

    public int[] chooseValidPosition(Boat boat) { //finds valid starting pos based on boat length and orientation
        int row, col;

        if (boat.is_horizontal()) {
            row = r.nextInt(rows);
            col = r.nextInt(cols - boat.get_size() + 1);
        } else {
            row = r.nextInt(rows - boat.get_size());
            col = r.nextInt(cols);
        }

        return new int[]{row, col};
    }

    // count from start indices to the length of the boat how many open spaces exist
    public boolean spacesAreAvailable(Boat boat, int[] start) {
        int row = start[0], col = start[1];
        if (boat.is_horizontal()) {
            for (int i = 0; i < boat.get_size(); i++) {
                if (board[row][col + i].get_status() == 'B') {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < boat.get_size(); i++) {
                if (board[row + i][col].get_status() == 'B') {
                    return false;
                }
            }
        }
        return true;
    }

    //updates board cells to 'B' and gives updated cells to boat in set_cells()
    public void updateBoardAndBoatCells(Boat boat, int[] start) {
        int row = start[0], col = start[1];
        Cell[] cells = new Cell[boat.get_size()];

        if (boat.is_horizontal()) {
            for (int i = 0; i < boat.get_size(); i++) {
                board[row][col + i].set_status('B');
                cells[i] = board[row][col + i];
            }
        } else {
            for (int i = 0; i < boat.get_size(); i++) {
                board[row + i][col].set_status('B');
                cells[i] = board[row + i][col];
            }
        }
        boat.set_cells(cells);
    }

    public int boats_remaining() {
        int remaining = 0;
        for (Boat boat : boats){
            for (Cell cell : boat.get_cells()){
                if(cell.get_status() == 'B'){
                    remaining ++;
                    break;
                }
            }
        }
        return remaining;
    }

    public int fire(int row, int col) { // firing a coordinate
        if (!inBounds(row, col) || board[row][col].get_status() == 'H' || board[row][col].get_status() == 'M'){ // penalty, out of bounds or redundant guess
            return -1;
        }
        else if (board[row][col].get_status() == '-'){ // miss a boat
            board[row][col].set_status('M');
            shots ++;
            return 0;
        }
        else{ // hit a boat
            board[row][col].set_status('H');
            shots ++;
            return 1;
        }
    }

    public boolean inBounds(int row, int col){ // checking if coordinate is in bounds
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    public void missile(int row, int col) { // fires a missile on a specified coordinate
        for (int i = row - 1; i < row + 2; i++) {
            for (int j = col - 1; j < col + 2; j++) {
                fire(i, j);
            }
        }
    }

    public int drone(String direction, int index) { //scans a specific row or column
        int spots = 0; // number of spots that contain a boat in this specific col or row
        if (direction.equals("r")) {
            for (int i = 0; i < cols; i++) {
                Cell cell = board[index][i];
                if (cell.get_status() == 'B' || cell.get_status() == 'H') {
                    spots++;
                }
            }
        } else{
            for (int i = 0; i < rows; i++) {
                Cell cell = board[i][index];
                if (cell.get_status() == 'B' || cell.get_status() == 'H') {
                    spots++;
                }
            }
        }
        return spots;
    }

    public void display() { // prints out the play board every turn
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j].get_status() == 'B') {
                    System.out.print("- ");
                } else {
                    System.out.print(board[i][j].get_status() + " ");
                }
            }
            System.out.println();
        }
    }

    public void print() { //prints out fully revealed board (used for debugging)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j].get_status() + " ");
            }
            System.out.println();
        }
    }
    public int get_shots(){
        return shots;
    }
    public int get_rows(){
        return rows;
    }
    public int get_cols(){
        return cols;
    }
    public static void main(String[] args){
        Board b = new Board(3, 3);
        b.print();

        System.out.println();

        Board b2 = new Board(4, 4);
        b2.print();


    }

}
