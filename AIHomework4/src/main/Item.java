package main;

public class Item implements Comparable<Item> {

    public double weight;
    public double value;
    public double ratio;

    public Item(double weight, double value) {
        this.value = value;
        this.weight = weight;
        this.ratio = value / weight;
    }

    @Override
    public int compareTo(Item other) {
        if (this.ratio > other.ratio) {
            return 1;
        }
        if (this.ratio < other.ratio) {
            return -1;
        }
        return 0;
    }
}
