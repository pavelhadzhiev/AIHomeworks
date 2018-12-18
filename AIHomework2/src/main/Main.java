package main;

import static main.Move.DOWN;
import static main.Move.LEFT;
import static main.Move.RIGHT;
import static main.Move.UP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    /*
    SAMPLE: 
    8    
    1 2 3
    4 5 6
    0 7 8
    
    MEDIUM:
    8
    0 2 4
    1 5 3
    7 8 6
    
    HARD:
    8
    5 6 2
    1 0 7
    8 4 3
    
    VERY HARD:
    8
    2 1 4
    5 3 8
    7 0 6
    */

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();
        List<Integer> inputList = new ArrayList<>();
        if (Math.sqrt((int) n + 1) % 1 != 0) {
            scan.close();
            throw new IllegalArgumentException("Invalid size.");
        }
        for (int i = 0; i < n + 1; i++) {
            inputList.add(scan.nextInt());
        }
        State startState = new State(inputList);

        List<Integer> goalStateList = new ArrayList<>();
        for (int i = 1; i < n + 1; i++) {
            goalStateList.add(i);
        }
        goalStateList.add(0);
        State goalState = new State(goalStateList);

        scan.close();

        List<Move> moves = aStarManhattanSearch(startState, goalState);

        if (moves != null) {
            System.out.println(moves.size());
            for (Move move : moves) {
                System.out.println(move);
            }
        } else {
            System.out.println("Couldn't solve that.");
        }
    }

    private static List<Move> aStarManhattanSearch(State startState, State goalState) {
        PriorityQueue<State> queue = new PriorityQueue<>();
        Set<State> visited = new HashSet<>();

        queue.add(startState);

        while (!queue.isEmpty()) {
            State current = queue.peek();

            System.out.println("Queue size: " + queue.size());
            System.out.println("Visited size: " + visited.size());
            System.out.println(current);
            queue.remove();

            List<State> childStates = expandState(current).stream()
                    .filter(state -> !visited.contains(state))
                    .collect(Collectors.toList());

            queue.addAll(childStates);
            visited.add(current);

            if (current.equals(goalState)) {
                return retracePath(current);
            }
        }

        return null;
    }

    private static List<Move> retracePath(State current) {
        List<Move> moves = new ArrayList<>();

        while (current.parent != null) {
            moves.add(0, current.originMove);
            current = current.parent;
        }

        return moves;
    }

    private static List<State> expandState(State state) {
        List<State> children = new ArrayList<>();

        if (validLeft(state)) {
            children.add(new State(state, LEFT));
        }
        if (validRight(state)) {
            children.add(new State(state, RIGHT));
        }
        if (validUp(state)) {
            children.add(new State(state, UP));
        }
        if (validDown(state)) {
            children.add(new State(state, DOWN));
        }

        return children;
    }

    private static boolean validRight(State state) {
        if (state.emptyPosition % state.size == 0 || state.originMove == LEFT) {
            return false;
        }
        return true;
    }

    private static boolean validLeft(State state) {
        if (state.emptyPosition % state.size == 1 || state.originMove == RIGHT) {
            return false;
        }
        return true;
    }

    private static boolean validUp(State state) {
        if (state.emptyPosition - state.size <= 0 || state.originMove == DOWN) {
            return false;
        }
        return true;
    }

    private static boolean validDown(State state) {
        if (state.emptyPosition + state.size > state.size * state.size || state.originMove == UP) {
            return false;
        }
        return true;
    }
}
