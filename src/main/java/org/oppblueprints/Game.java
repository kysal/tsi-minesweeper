package org.oppblueprints;

import java.util.Objects;
import java.util.Scanner;

public class Game {
    Board board;
    int flagsLeft;
    int cellsToOpen;

    public Game() {
    }

    // Logs command syntax help to console
    private void printHelpCommand() {
        System.out.println("""
                --- HELP ---
                open [ROW_LETTER][COL_NUMBER]: reveals tile at index
                flag [ROW_LETTER][COL_NUMBER]: flags tile at index
                """);
    }

    // Asks for and handles each turn of the user's input
    public void play(Difficulty difficulty) {
        boolean isGameRunning = true;
        board = new Board(difficulty);
        flagsLeft = difficulty.mines();

        Scanner scanner = new Scanner(System.in);

        // Game started
        while (isGameRunning) {
            // Print game state
            System.out.println(this.board.printBoard());
            System.out.println("Flags: " + flagsLeft);

            // Ask for input
            System.out.print("Next Input: ");
            String scannerInput = scanner.nextLine();

            // Print help text
            if (scannerInput.equalsIgnoreCase("help")) {
                printHelpCommand();
                continue;
            }

            // Convert scanner string to input object
            GameInput input = GameInput.parseInput(scannerInput);

            if (input.hasNoError()) {
                // Send input to board
                GameResult result = this.board.action(input);
                if (result.getError() == ResultErrorType.None) {
                    if (result.isGameLost()) {
                        isGameRunning = false;
                        System.out.println(this.board.printBoard());
                        System.out.println("Mine detected! You lose!");
                    } else if (input.action == ActionType.Flag) {
                        // Change flag variable when action taken
                        if (result.isFlagPlaced()) flagsLeft--;
                        else flagsLeft++;
                    } else {
                        cellsToOpen -= result.getTilesOpened();
                        if (cellsToOpen <= 0) {
                            isGameRunning = false;
                            System.out.println("You win");
                        }

                    }
                } else {
                    // Handles Input and output error messages received from GameResult
                    switch (result.getError()) {
                        case InvalidIndex -> System.out.println("Input Error: Out of Bounds");
                        case AlreadyCleared -> System.out.println("Input Error: Tile already cleared");
                        default -> System.out.println("Result Error: Error with message: " + result.getError());
                    }
                }
            } else {
                switch (input.getError()) {
                    case UnknownChar -> System.out.println("Input Error: An unexpected character was found in input");
                    case CommandSyntax -> System.out.println("Input Error: Error in command syntax. Try 'help'");
                    case RowIndexUndefined -> System.out.println("Input Error: Your input contained no alphabet characters and thus row could not be determined");
                    case ColIndexUndefined -> System.out.println("Input Error: Your input contained no number characters and thus column could not be determined");
                    default -> System.out.println("Input Error: Error without message: " + input.getError());
                }
            }
        }
    }

    // Initialises the initial values of the game
    public void start() {
        Scanner scanner = new Scanner(System.in);
        Difficulty difficulty = null;

        // Set difficulty of board
        while (difficulty == null) {
            System.out.println("Choose a difficulty:\n(b)eginner\n(i)ntermediate\n(e)xpert\n(c)ustom");
            String input = scanner.nextLine();

            // Custom difficulty input definition
            if (Objects.equals(input, "c") || Objects.equals(input, "custom")) {
                System.out.print("Rows: ");
                int rows = Integer.parseInt(scanner.nextLine());
                System.out.print("Columns: ");
                int cols = Integer.parseInt(scanner.nextLine());
                System.out.print("Mines: ");
                int mines = Integer.parseInt(scanner.nextLine());

                if (mines < rows * cols - 9) {
                    difficulty = new Difficulty(rows, cols, mines);
                } else {
                    System.out.println("Too many mines to fit at current board size");
                }
            } else {
                // Choice of set difficulties outlined in Difficulty.java
                difficulty = switch (input) {
                    case "b", "beginner" -> Difficulty.beginner();
                    case "i", "intermediate" -> Difficulty.intermediate();
                    case "e", "expert" -> Difficulty.expert();
                    default -> null;
                };
            }
        }
        cellsToOpen = (difficulty.rows() * difficulty.cols()) - difficulty.mines();

        // Choice of interface
        System.out.print("(C)ommand Line or (G)UI: ");
        String displayTypeInput = scanner.nextLine().toUpperCase();

        // CLI
        if(displayTypeInput.equals("C") || displayTypeInput.equals("COMMAND LINE") || displayTypeInput.equals("COMMAND")) {
            play(difficulty);
        }
        // GUI
        else if (displayTypeInput.equals("G") || displayTypeInput.equals("GUI")) {
            GUI.playGUI(difficulty);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
