package main;

import java.util.Scanner;

public class Game {

    private char playerSymbol;
    private char computerSymbol;
    private Board board;
    private Scanner scan;

    public void begin(boolean first) {
        board = new Board();
        if (first) {
            playerSymbol = 'X';
            computerSymbol = 'O';
        } else {
            playerSymbol = 'O';
            computerSymbol = 'X';
            board.computerMove(computerSymbol, playerSymbol);
        }

        System.out.println("Game started. Your are " + playerSymbol + ".");

        scan = new Scanner(System.in);
        int x, y;

        while (!board.ended()) {
            board.print();
            if (winCheck()) {
                return;
            }

            do {
                x = scan.nextInt();
                y = scan.nextInt();
            } while (!board.set(x, y, playerSymbol));
            
            if (winCheck()) {
                board.print();
                return;
            }
            if (board.ended()) {
                break;
            }
            
            board.computerMove(computerSymbol, playerSymbol);
        }

        board.print();
        System.out.println("It's a draw!");
    }

    private boolean winCheck() {
        boolean won = false;
        if (board.won(playerSymbol)) {
            System.out.println("Congratulations! You won.");
            won = true;
        }
        if (board.won(computerSymbol)) {
            System.out.println("Ugh... You lost.");
            won = true;
        }
        if (won) {
            scan.close();
        }
        return won;
    }
}
