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

    private void printHelpCommand() {
        System.out.println("""
                --- HELP ---
                mine [ROW_LETTER][COL_NUMBER]: reveals tile at index
                flag [ROW_LETTER][COL_NUMBER]: flags tile at index
                """);
    }

    public void play() {
        boolean isGameRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isGameRunning) {

            // Print game state
            System.out.println(this.board.printBoard());

            // Ask for input
            System.out.println("Next Input: ");

            String scannerInput = scanner.nextLine();

            if (scannerInput.toLowerCase().equals("help")) printHelpCommand();

            GameInput input = GameInput.parseInput(scannerInput);

            if (input.isValid()) {
                GameResult result = this.board.action(input);
                if (result.error == ErrorType.None) {
                    if (result.lost) {
                        isGameRunning = false;
                        System.out.println(this.board.printBoard());
                        System.out.println("Mine detected! You lose!");
                    }
                } else {
                    switch (result.error) {
                        case InvalidIndex -> System.out.println("Input Error: Out of Bounds");
                        case AlreadyCleared -> System.out.println("Input Error: Tile already cleared");
                        default -> System.out.println("Error with message");
                    }
                }
            } else {
                System.out.println("Input Error: Invalid Syntax");
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();

        game.play();

    }

}
