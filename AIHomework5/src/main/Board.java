package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private char state[][];

    public Board() {
        this.state = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state[i][j] = ' ';
            }
        }
    }

    public Board(Board board) {
        this.state = copy(board.state);
    }

    public boolean set(int x, int y, char symbol) {
        if (x < 1 || x > 3 || y < 1 || y > 3) {
            System.out.println("Invalid Move. Valid coordinates are 1-3.");
            return false;
        }
        if (this.state[x - 1][y - 1] != ' ') {
            System.out.println("Invalid Move. Field is already taken.");
            return false;
        }
        this.state[x - 1][y - 1] = symbol;
        return true;
    }

    public boolean won(char symbol) {
        boolean mainDiagWin = true, secondDiagWin = true;
        for (int i = 0; i < 3; i++) {

            if (state[i][i] != symbol) {
                mainDiagWin = false;
            }
            if (state[i][2 - i] != symbol) {
                secondDiagWin = false;
            }

            boolean rowWin = true, colWin = true;
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != symbol) {
                    rowWin = false;
                }
                if (state[j][i] != symbol) {
                    colWin = false;
                }
            }

            if (rowWin || colWin) {
                return true;
            }
        }
        if (mainDiagWin || secondDiagWin) {
            return true;
        }
        return false;
    }

    public boolean ended() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;

    }

    public void print() {
        System.out.println(state[0][0] + "|" + state[0][1] + "|" + state[0][2]);
        System.out.println("-----");
        System.out.println(state[1][0] + "|" + state[1][1] + "|" + state[1][2]);
        System.out.println("-----");
        System.out.println(state[2][0] + "|" + state[2][1] + "|" + state[2][2]);
    }

    private static class Move {

        public int x;
        public int y;

        public int score;

        public Move(int x, int y, int score) {
            this.x = x;
            this.y = y;

            this.score = score;
        }
    }

    public void computerMove(char computerSymbol, char playerSymbol) {
        Move bestMove = minimax(new Board(this), computerSymbol, playerSymbol);
        state[bestMove.x][bestMove.y] = computerSymbol;
    }

    private Move minimax(Board board, char symbol, char other) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.state[i][j] == ' ') {
                    moves.add(new Move(i, j, max(new Board(board), symbol, other, i, j, 0, -10, 10)));
                }
            }
        }

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            if (move.score > bestMove.score) {
                bestMove = move;
            }
        }

        int bestScore = bestMove.score;
        List<Move> bestMoves = moves.stream()
                .filter(x -> x.score == bestScore)
                .collect(Collectors.toList());

        return bestMoves.get((int) (Math.random() * bestMoves.size()));
    }

    private int max(Board board, char symbol, char other, int x, int y, int movesCnt, Integer globalBest, Integer globalWorst) {
        board.state[x][y] = symbol;
        if (board.won(symbol)) {
            return -10 + movesCnt;
        }
        if (board.won(other)) {
            return 10 - movesCnt;
        }
        if (board.ended()) {
            return 0;
        }
        movesCnt++;

        int worst = 10;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.state[i][j] == ' ') {
                    int score = min(new Board(board), symbol, other, i, j, movesCnt, globalBest, globalWorst);
                    if (score < worst) {
                        worst = score;
                    }
                    if (score < globalWorst) {
                        globalWorst = score;
                    }
                    if (score < globalBest) {
                        return score;
                    }
                }
            }
        }

        return worst;
    }

    private int min(Board board, char symbol, char other, int x, int y, int movesCnt, Integer globalBest, Integer globalWorst) {
        board.state[x][y] = other;
        if (board.won(symbol)) {
            return -10 + movesCnt;
        }
        if (board.won(other)) {
            return 10 - movesCnt;
        }
        if (board.ended()) {
            return 0;
        }
        movesCnt++;

        int best = -10;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.state[i][j] == ' ') {
                    int score = max(new Board(board), symbol, other, i, j, movesCnt, globalBest, globalWorst);
                    if (score > best) {
                        best = score;
                    }
                    if (score > globalBest) {
                        globalBest = score;
                    }
                    if (score > globalWorst) {
                        return score;
                    }
                }
            }
        }

        return best;
    }

    public static char[][] copy(char[][] src) {
        char[][] dst = new char[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return dst;
    }
}
