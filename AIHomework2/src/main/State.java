package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class State implements Comparable<State> {

    public State parent;
    public Move originMove;

    public Map<Integer, Integer> map;
    public int emptyPosition;
    public int size;

    public int g;
    public int h;

    public State(State state, Move move) {
        this.parent = state;
        this.originMove = move;
        this.map = new HashMap<>(state.map);
        this.emptyPosition = state.emptyPosition;
        this.size = state.size;
        this.g = state.g + 1;
        this.h = state.h;

        switch (move) {
        case LEFT:
            moveLeft();
            break;
        case RIGHT:
            moveRight();
            break;
        case UP:
            moveUp();
            break;
        case DOWN:
            moveDown();
            break;
        default:
            break;
        }
    }

    private void moveLeft() {
        move(emptyPosition - 1, h + 1);
    }

    private void moveRight() {
        move(emptyPosition + 1, h - 1);

    }

    private void moveUp() {
        move(emptyPosition - size, h + 1);

    }

    private void moveDown() {
        move(emptyPosition + size, h - 1);
    }

    private void move(int newEmptyPosition, int newH) {
        map.put(emptyPosition, map.get(newEmptyPosition));
        emptyPosition = newEmptyPosition;
        h = newH;
        map.put(emptyPosition, 0);
    }

    public State(List<Integer> setupList) {
        this.parent = null;
        this.originMove = null;
        this.map = new HashMap<>();
        this.size = (int) Math.sqrt(setupList.size());
        this.g = 0;

        for (int i = 0; i < setupList.size(); i++) {
            int val = setupList.get(i);
            map.put(i + 1, val);
            if (val == 0) {
                this.emptyPosition = i + 1;
            }
        }

        this.h = calculateHeuristic();
    }

    private int calculateHeuristic() {
        int count = 0;
        int start = emptyPosition;
        int target = size * size;

        while (start <= target - size) {
            start += size;
            count++;
        }

        while (start < target) {
            start++;
            count++;
        }

        return count;
    }

    @Override
    public String toString() {
        return "State [map=" + map + ", emptyPosition="
                + emptyPosition + ", size=" + size + ", g=" + g + ", h=" + h + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof State) {
            return this.map.equals(((State) other).map);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public int compareTo(State other) {
        int thisValue = this.g + this.h;
        int otherValue = other.g + other.h;

        return thisValue - otherValue;
    }
}
