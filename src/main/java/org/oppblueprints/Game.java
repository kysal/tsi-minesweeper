package org.oppblueprints;

import java.util.Objects;
import java.util.Scanner;

public class Game {
    Board board;
    int score;
    float time;
    int flags_left;


    public Game() {
        this.score = 0;
        this.time = 0;
    }

    private void printHelpCommand() {
        System.out.println("""
                --- HELP ---
                mine [ROW_LETTER][COL_NUMBER]: reveals tile at index
                flag [ROW_LETTER][COL_NUMBER]: flags tile at index
                """);
    }

    private void CheckEnvironmentValidity(GameInput input) {
        if (input.action == ActionType.Flag && flags_left <= 0) {
            input.setError(InputErrorType.NoFlagsLeft);
        }
    }

    public void play(Difficulty difficulty) {
        boolean isGameRunning = true;
        board = new Board(difficulty);
        flags_left = difficulty.mines();

        Scanner scanner = new Scanner(System.in);

        while (isGameRunning) {

            // Print game state
            System.out.println(this.board.printBoard());

            // Print flags left
            System.out.println("Flags: " + flags_left);

            // Ask for input
            System.out.print("Next Input: ");
            String scannerInput = scanner.nextLine();

            // Print help text
            if (scannerInput.equalsIgnoreCase("help")) {
                printHelpCommand();
                continue;
            };


            GameInput input = GameInput.parseInput(scannerInput);

            if (input.getError() == InputErrorType.None) {
                CheckEnvironmentValidity(input);


            }


            if (input.isValid()) {
                if (flags_left == 0 && input.action == ActionType.Flag) {
                    System.out.println("No flags left");
                    continue;
                }
                GameResult result = this.board.action(input);
                if (result.error == ErrorType.None) {
                    if (result.lost) {
                        isGameRunning = false;
                        System.out.println(this.board.printBoard());
                        System.out.println("Mine detected! You lose!");
                    } else if (input.action == ActionType.Flag) {

                        if (result.isFlagPlaced()) flags_left--;
                        else flags_left++;

                        if (flags_left == 0) {
                            if(this.board.hasWon()) {
                                isGameRunning = false;
                                System.out.println("You win");
                            }
                        }
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

    public void start() {
        Scanner scanner = new Scanner(System.in);
        Difficulty difficulty = null;

        while (difficulty == null) {
            System.out.println("Choose a difficulty:\n(b)eginner\n(i)ntermediate\n(e)xpert\n(c)ustom");
            String input = scanner.nextLine();

            if (Objects.equals(input, "c") || Objects.equals(input, "custom")) {
                System.out.print("Rows: ");
                int rows = Integer.parseInt(scanner.nextLine());
                System.out.print("Columns: ");
                int cols = Integer.parseInt(scanner.nextLine());
                System.out.print("Mines: ");
                int mines = Integer.parseInt(scanner.nextLine());

                difficulty = new Difficulty(rows, cols, mines);
                play(difficulty);
            } else {
                difficulty = switch (input) {
                    case "b", "beginner" -> Difficulty.beginner();
                    case "i", "intermediate" -> Difficulty.intermediate();
                    case "e", "expert" -> Difficulty.expert();
                    default -> null;
                };
                play(difficulty);
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();

        game.start();

    }

}
