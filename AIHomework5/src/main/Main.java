package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();

        System.out.println("Would you like to be first?(y/n)");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();

        if (input.equalsIgnoreCase("y")) {
            game.begin(true);
        } else {
            game.begin(false);
        }
        
        scan.close();
    }
}