package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static class Position {

        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Field {

        public boolean queen;
        public int conflicts;

        Field(boolean queen, int conflicts) {
            this.queen = queen;
            this.conflicts = conflicts;
        }

        public String toString() {
            if (queen) {
                return "*";
            }
            return "_";
        }
    }
    
    private static void minConfclits(Field[][] board, int n) {
        boolean solved = false;
        long startTime = System.currentTimeMillis();
        int iterations = 0;
        
        while (!solved) {
            solved = true;

            int queenConflicts = n;
            int fieldConflicts = 0;

            List<Position> queensToMove = new ArrayList<>();
            List<Position> fieldsToMoveTo = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j].queen && board[i][j].conflicts == 0) {
                        solved = false;
                    }
                    if (board[i][j].queen && board[i][j].conflicts < queenConflicts) {
                        queenConflicts = board[i][j].conflicts;
                    }
                    
                    if (!board[i][j].queen && board[i][j].conflicts > fieldConflicts) {
                        fieldConflicts = board[i][j].conflicts;
                    }
                }
            }
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j].queen && board[i][j].conflicts == queenConflicts) {
                        queensToMove.add(new Position(i, j));
                    }
                    
                    if (!board[i][j].queen && board[i][j].conflicts == fieldConflicts) {
                        fieldsToMoveTo.add(new Position(i, j));
                    }
                }
            }
            
            Position queenToMove = queensToMove.get((int) (Math.random() * queensToMove.size()));
            Position fieldToMoveTo = fieldsToMoveTo.get((int) (Math.random() * fieldsToMoveTo.size()));
            if (!solved) {
                unsetQueen(board, n, queenToMove.x, queenToMove.y);
                setQueen(board, n, fieldToMoveTo.x, fieldToMoveTo.y);
            }
            iterations++;

            if (iterations % 100 == 0) {
                System.out.println("Iterations so far: " + iterations);
                System.out.println("Least conflicted queen: " + queenConflicts);
                System.out.println("Queens with " + queenConflicts + " conflicts: " + queensToMove.size());
            }
        }
        //printBoard(board, n);
        System.out.println();
        System.out.println("Solved in (milliseconds): " + (System.currentTimeMillis() - startTime));
        System.out.println("Solved in (iterations): " + iterations);
    }

    private static Field[][] initialBoard(int n) {
        Field[][] board = new Field[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = new Field(false, 0);
            }
        }

        for (int i = 0; i < n; i++) {
            int x = (int) (Math.random() * (n - 1));
            int y = (int) (Math.random() * (n - 1));
            while (board[x][y].queen) {
                x = (int) (Math.random() * (n - 1));
                y = (int) (Math.random() * (n - 1));
            }

            setQueen(board, n, x, y);
        }

        return board;
    }

    private static void setQueen(Field[][] board, int n, int x, int y) {
        board[x][y].queen = true;

        // adding conflicts to fields in the same row or column
        for (int j = 0; j < n; j++) {
            board[x][j].conflicts++;
            board[j][y].conflicts++;
        }

        // adding conflicts to main diagonal fields
        int mainDiagonalX, mainDiagonalY;
        if (x > y) {
            mainDiagonalX = x - y;
            mainDiagonalY = 0;
        } else {
            mainDiagonalX = 0;
            mainDiagonalY = y - x;
        }

        while (mainDiagonalX < n && mainDiagonalY < n) {
            board[mainDiagonalX++][mainDiagonalY++].conflicts++;
        }

        board[x][y].conflicts -= 3;

        // adding conflicts to secondary diagonal fields
        int secondaryDiagonalX, secondaryDiagonalY;

        secondaryDiagonalX = x - 1;
        secondaryDiagonalY = y + 1;

        while (secondaryDiagonalX >= 0 && secondaryDiagonalY < n) {
            board[secondaryDiagonalX--][secondaryDiagonalY++].conflicts++;
        }

        secondaryDiagonalX = x + 1;
        secondaryDiagonalY = y - 1;

        while (secondaryDiagonalX < n && secondaryDiagonalY >= 0) {
            board[secondaryDiagonalX++][secondaryDiagonalY--].conflicts++;
        }
    }

    private static void unsetQueen(Field[][] board, int n, int x, int y) {
        if (board[x][y].queen == false) {
            throw new IllegalArgumentException("Something went wrong. Trying to unset queen from field with no queen.");
        }
        board[x][y].queen = false;

        // adding conflicts to fields in the same row or column
        for (int j = 0; j < n; j++) {
            board[x][j].conflicts--;
            board[j][y].conflicts--;
        }

        // adding conflicts to main diagonal fields
        int mainDiagonalX, mainDiagonalY;
        if (x > y) {
            mainDiagonalX = x - y;
            mainDiagonalY = 0;
        } else {
            mainDiagonalX = 0;
            mainDiagonalY = y - x;
        }

        while (mainDiagonalX < n && mainDiagonalY < n) {
            board[mainDiagonalX++][mainDiagonalY++].conflicts--;
        }

        board[x][y].conflicts += 3;

        // adding conflicts to secondary diagonal fields
        int secondaryDiagonalX, secondaryDiagonalY;

        secondaryDiagonalX = x - 1;
        secondaryDiagonalY = y + 1;

        while (secondaryDiagonalX >= 0 && secondaryDiagonalY < n) {
            board[secondaryDiagonalX--][secondaryDiagonalY++].conflicts--;
        }

        secondaryDiagonalX = x + 1;
        secondaryDiagonalY = y - 1;

        while (secondaryDiagonalX < n && secondaryDiagonalY >= 0) {
            board[secondaryDiagonalX++][secondaryDiagonalY--].conflicts--;
        }
    }

    private static void printBoard(Field[][] board, int n) {
        System.out.println();

        for (Field[] row : board) {
            for (Field field : row) {
                System.out.print(field + "" + field.conflicts + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Field[][] board = initialBoard(n);

        minConfclits(board, n);

        printBoard(board, n);
        scanner.close();
    }


}
