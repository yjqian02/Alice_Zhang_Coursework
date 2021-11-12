// Written by zhan6698 and bogir001
import java.util.Scanner;
public class Game {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome to Battleship");
        System.out.println("The goal of this game is to sink as many boats as possible.");
        System.out.println("To do this, you can hit a coordinate (regular turn) or use one of two special powers:");
        System.out.println("Missile allows you to hit a 3x3 square around a desired coordinate.");
        System.out.println("Drone allows you to scan a specified row or column and tells you how many spots hold a boat in that row/column.");
        System.out.println("Missile uses up a shot, but Drone doesn't.");
        System.out.println("Commands are case sensitive.");
        int r, c;

        while (true) {
            System.out.println("Enter row and col (between 3-10): ");
            r = s.nextInt();
            c = s.nextInt();
            if (r > 2 && c > 2 && r < 11 && c < 11) {
                break;
            } else {
                System.out.println("Invalid bounds, try again.");
            }
        }

        Board b = new Board(r, c);

        int turns = 0;
        int num_boats = b.boats_remaining();

        while (num_boats != 0) {
            System.out.println("BOARD");
            b.display();
            System.out.println("Enter an option (print, fire, missile, drone): ");
            String option = s.next();

            if (option.equals("print")) {
                b.print();
                System.out.println();
            } else if (option.equals("fire")) {
                System.out.println("Enter coordinates to fire at (row, column): ");
                r = s.nextInt();
                c = s.nextInt();
                int result = b.fire(r, c);
                if (result == -1) {
                    System.out.println("Penalty");
                    turns++;
                } else if (result == 0) {
                    System.out.println("Miss");
                } else {
                    System.out.println("Hit!");
                }
                turns++;
            } else if (option.equals("missile")) {
                while (true) {
                    System.out.println("Enter coordinates to fire missile at (row column): ");
                    r = s.nextInt();
                    c = s.nextInt();
                    if (b.inBounds(r, c)) {
                        break;
                    } else {
                        System.out.println("Invalid coordinate, try again.");
                    }
                }
                b.missile(r, c);
                turns++;
            } else if (option.equals("drone")) {
                String direction;
                int index;
                while (true) {
                    System.out.println("Do you want to scan a row (r) or column (c)? ");
                    direction = s.next();
                    if (direction.equals("r") || direction.equals("c")) {
                        break;
                    } else {
                        System.out.println("Invalid option, try again");
                    }
                }
                while (true) {
                    System.out.println("Select a vlaid index to scan: ");
                    index = s.nextInt();
                    if (direction.equals("r")) {
                        if (index >= 0 && index < b.get_rows()) {
                            break;
                        } else {
                            System.out.println("Please enter a valid row index.");
                        }
                    } else {
                        if (index >= 0 && index < b.get_cols()) {
                            break;
                        } else {
                            System.out.println("Please enter a valid column index.");
                        }
                        System.out.println("The done has scanned " + b.drone(direction, index) + " targets in the chosen row/column.");
                    }
                }
                turns++;
            } else {
                System.out.println("Invalid option, try again");
                continue; // resets loop (skips over anything after)
            }
            int remaining = b.boats_remaining();
            while (num_boats > remaining) {
                num_boats--;
                System.out.println("Sunk!");
            }
        }
        System.out.println("CONGRATS U SUNK ALL THE BOATS! You fired " + b.get_shots() + " shots and took " + turns + " turns.");
    }
}
