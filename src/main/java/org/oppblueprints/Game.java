package org.oppblueprints;

import java.util.Scanner;

public class Game {
    Board board;
    int score;
    float time;


    public Game() {
        this.board = new Board(10,8);
        this.score = 0;
        this.time = 0;

        System.out.println(this.board.printBoard());

    }

    public void play() {
        boolean isGameRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isGameRunning) {

            // Print game state
            System.out.println(this.board.printBoard());

            // Ask for input
            System.out.println("Next Input: ");


            GameInput input = GameInput.parseInput(scanner.nextLine());

            if (input.isValid()) {
                try {
                    this.board.action(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Input Error: Out of Bounds");
                }

            }

            System.out.println("Input Error: Invalid Syntax");



            // System.out.println(input);
        }

    }


    public static void main(String[] args) {
        Game game = new Game();

        game.play();

    }

}
