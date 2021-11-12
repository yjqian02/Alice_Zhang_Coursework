// Names: Alice Zhang, Ruchitha Bogireddi
// x500s: zhan6698, bogir001

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MyMaze{
    private Cell[][] maze;
    private int startRow;
    private int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        this.startRow = startRow;
        this.endRow = endRow;
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                maze[r][c] = new Cell();
            }
        }

    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {

        MyMaze myMaze = new MyMaze(rows, cols, startRow, endRow);

        Stack1Gen<int[]> stack = new Stack1Gen();
        int[] startIndexData = new int[] {startRow, 0};
        stack.push(startIndexData);
        myMaze.maze[startRow][0].setVisited(true);

        while (!(stack.isEmpty())){
            int[] top_node = stack.top();
            int r = top_node[0];
            int c = top_node[1];

            //checking for valid neighbors
            int numNeighbors = 0;
            int[][] neighbors = new int[4][2]; // largest possible num of neighbors is 4
            if (r - 1 >= 0 && !myMaze.maze[r - 1][c].getVisited()) {
                int[] addCoor = new int[] {r-1, c};
                neighbors[numNeighbors] = addCoor;
                numNeighbors ++;
            }
            if (r + 1 < rows && !myMaze.maze[r + 1][c].getVisited()){
                int[] addCoor = new int[] {r+1, c};
                neighbors[numNeighbors] = addCoor;
                numNeighbors ++;
            }
            if (c + 1 < cols && !myMaze.maze[r][c+1].getVisited()){
                int[] addCoor = new int[] {r, c + 1};
                neighbors[numNeighbors] = addCoor;
                numNeighbors ++;
            }
            if (c - 1 >= 0 && !myMaze.maze[r][c-1].getVisited()){
                int[] addCoor = new int[] {r, c -1};
                neighbors[numNeighbors] = addCoor;
                numNeighbors ++;
            }

            if (numNeighbors == 0){ // dead end
                stack.pop();
            }
            else {
                Random random = new Random();
                int neighborIndex = random.nextInt(numNeighbors);
                if (neighbors[neighborIndex][0] < r) { // above
                    int[] addNodeData = new int[]{neighbors[neighborIndex][0], neighbors[neighborIndex][1]};
                    stack.push(addNodeData);
                    myMaze.maze[r-1][c].setVisited(true);
                    myMaze.maze[r-1][c].setBottom(false);

                } else if (neighbors[neighborIndex][0] > r) { // below
                    int[] addNodeData = new int[]{neighbors[neighborIndex][0], neighbors[neighborIndex][1]};
                    stack.push(addNodeData);
                    myMaze.maze[r+1][c].setVisited(true);
                    myMaze.maze[r][c].setBottom(false);

                } else if (neighbors[neighborIndex][1] < c) { // left
                    int[] addNodeData = new int[]{neighbors[neighborIndex][0], neighbors[neighborIndex][1]};
                    stack.push(addNodeData);
                    myMaze.maze[r][c-1].setVisited(true);
                    myMaze.maze[r][c-1].setRight(false);

                } else if (neighbors[neighborIndex][1] > c) { // right
                    int[] addNodeData = new int[]{r, c+1};
                    stack.push(addNodeData);
                    myMaze.maze[r][c+1].setVisited(true);
                    myMaze.maze[r][c].setRight(false);
                }
            }
        }
        for (int r =0; r < myMaze.maze.length; r++){ // set everything to not visited for solving the maze
            for (int c=0; c < myMaze.maze[0].length; c++){
                myMaze.maze[r][c].setVisited(false);
            }
        }
        return myMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */

    public void printMaze() {
        for (int topIndex = 0; topIndex < maze[0].length; topIndex++){ // top row
            System.out.print("|---"); // bottom get rid of --- //right get rid of |
        }
        System.out.print("|");
        for(int row=0; row < maze.length; row++){
            System.out.println();
            if (row != startRow) {
                System.out.print("|");
            }
            if (row == startRow){
                System.out.print(" ");
            }
            for(int midIndex = 0; midIndex < maze[0].length; midIndex++){ // middle row
                if(maze[row][midIndex].getVisited()){
                    System.out.print(" * ");
                }
                else{
                    System.out.print("   ");
                }
                if(maze[row][midIndex].getRight()){
                    if (midIndex != maze[0].length - 1 || row != endRow) {
                        System.out.print("|");
                    }
                    else{
                        System.out.print(" ");
                    }
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println();
            System.out.print("|");
            for(int bottomIndex = 0; bottomIndex < maze[0].length; bottomIndex++){ // bottom row
                if (maze[row][bottomIndex].getBottom()) {
                    System.out.print("---");
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }

        }
        System.out.println();
        System.out.println();
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q2Gen<int[]> queue = new Q2Gen<int[]>();
        int[] add_indices = new int[] {startRow, 0};
        queue.add(add_indices);
        maze[startRow][0].setVisited(true);

        while (queue.length() != 0) { // solve with BFS
            int[] index_polled = queue.remove();
            int r = index_polled[0];
            int c = index_polled[1];
            maze[r][c].setVisited(true);

            if (r == endRow && c == maze[0].length - 1) {
                System.out.println("CONGRATS BRO maze has been solved!!!!!!!");
                break;
            }

            if (r - 1 >= 0 && !maze[r - 1][c].getVisited() && !maze[r-1][c].getBottom()){ // above
                add_indices = new int[]{r - 1, c};
                queue.add(add_indices);
            }
            if (r + 1 < maze.length && !maze[r + 1][c].getVisited() && !maze[r][c].getBottom()) { // below
                add_indices = new int[]{r + 1, c};
                queue.add(add_indices);
            }
            if (c - 1 >= 0 && !maze[r][c - 1].getVisited() && !maze[r][c - 1].getRight()) { // left
                add_indices = new int[]{r, c - 1};
                queue.add(add_indices);
            }
            if (c + 1 < maze[0].length && !maze[r][c + 1].getVisited() && !maze[r][c].getRight()) { // right
                add_indices = new int[]{r, c + 1};
                queue.add(add_indices);
            }
        }
        printMaze();

    }

    public static void main(String[] args){
        /* Any testing can be put in this main function */
        for (int i=0; i < 10; i++) {
            MyMaze test = makeMaze(5, 20, 0, 4);
            System.out.println("unsolved version");
            test.printMaze();
            System.out.println("solved version");
            test.solveMaze();
        }

    }
}
