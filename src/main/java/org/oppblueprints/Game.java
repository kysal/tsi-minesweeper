package org.oppblueprints;

import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    Board board;
    private int flagsLeft;
    private int cellsToOpen;
    private int secondsPassed = 0;

    /**
     * Default constructor, doesn't initialise anything. Use .start() to initialise.
     */
    public Game() {
    }

    /**
     * Logs command syntax help to console
     */
    private void printHelpCommand() {
        System.out.println("""
                --- HELP ---
                open [ROW_LETTER][COL_NUMBER]: reveals tile at index
                flag [ROW_LETTER][COL_NUMBER]: flags tile at index
                """);
    }

    /**
     * Asks for and handles each turn of the user's input.
     * @param difficulty A Difficulty record that sets the rows, columns and mine amount.
     */
    public boolean play(Difficulty difficulty) {
        board = new Board(difficulty);
        flagsLeft = difficulty.mines();
        secondsPassed = 0;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
            }
        };

        Scanner scanner = new Scanner(System.in);
        timer.scheduleAtFixedRate(timerTask, 2000,1000);
        cellsToOpen = (difficulty.rows() * difficulty.cols()) - difficulty.mines();

        // Game started
        while (true) {
            // Print game state
            System.out.println(this.board.printBoard());
            System.out.println("Flags: " + flagsLeft);
            System.out.println("Time: " + secondsPassed + "s");

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
                if (result.getError() == ResultErrorType.NONE) {
                    if (result.isGameLost()) {
                        timer.cancel();
                        System.out.println(this.board.printBoard());
                        System.out.println("Mine detected! You lose!");

                        System.out.print("Play again? (Y/N): ");
                        return scanner.nextLine().equalsIgnoreCase("Y");
                    } else if (input.action == ActionType.FLAG) {
                        // Change flag variable when action taken
                        if (result.isFlagPlaced()) flagsLeft--;
                        else flagsLeft++;
                    } else {
                        cellsToOpen -= result.getTilesOpened();
                        if (cellsToOpen <= 0) {
                            timer.cancel();
                            System.out.println("You win");

                            System.out.print("Play again? (Y/N): ");
                            return scanner.nextLine().equalsIgnoreCase("Y");
                        }

                    }
                } else {
                    // Handles Input and output error messages received from GameResult
                    switch (result.getError()) {
                        case INVALID_INDEX -> System.out.println("Input Error: Out of Bounds");
                        case ALREADY_CLEARED -> System.out.println("Input Error: Tile already cleared");
                        case FLAGGED -> System.out.println("Result Error: Tile currently flagged");
                        case FLAG_FIRST_MOVE -> System.out.println("Result Error: You cannot use a flag on the first move");
                        default -> System.out.println("Result Error: Error with message: " + result.getError());
                    }
                }
            } else {
                // Handles Input Error messages received from GameInput
                switch (input.getError()) {
                    case UNKNOWN_CHAR -> System.out.println("Input Error: An unexpected character was found in input");
                    case COMMAND_SYNTAX -> System.out.println("Input Error: Error in command syntax. Try 'help'");
                    case ROW_INDEX_UNDEFINED -> System.out.println("Input Error: Your input contained no alphabet characters and thus row could not be determined");
                    case COL_INDEX_UNDEFINED -> System.out.println("Input Error: Your input contained no number characters and thus column could not be determined");
                    default -> System.out.println("Input Error: Error without message: " + input.getError());
                }
            }
        }
    }

    /**
     * Initialises the initial values of the game.
     */
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
                String rowsString = scanner.nextLine();
                if (rowsString.replaceAll("[^0-9]", "").equals("")) continue;
                int rows = Integer.parseInt(rowsString.replaceAll("[^0-9]", ""));

                System.out.print("Columns: ");
                String colsString = scanner.nextLine();
                if (colsString.replaceAll("[^0-9]", "").equals("")) continue;
                int cols = Integer.parseInt(colsString.replaceAll("[^0-9]", ""));

                System.out.print("Mines: ");
                String minesString = scanner.nextLine();
                if (minesString.replaceAll("[^0-9]", "").equals("")) continue;
                int mines = Integer.parseInt(minesString.replaceAll("[^0-9]", ""));

                if (mines < rows * cols - 9) {
                    difficulty = new Difficulty(rows, cols, mines);
                } else {
                    System.out.println("Too many mines to fit at current board size");
                }
            } else {
                // Choice of set difficulties outlined in Difficulty.java
                difficulty = switch (input) {
                    case "b", "beginner", "easy" -> Difficulty.beginner();
                    case "i", "intermediate", "medium" -> Difficulty.intermediate();
                    case "e", "expert", "hard" -> Difficulty.expert();
                    default -> null;
                };
            }
        }


        // Choice of interface
        System.out.print("(C)ommand Line or (G)UI: ");
        String displayTypeInput = scanner.nextLine().toUpperCase();

        // CLI
        if(displayTypeInput.equals("C") || displayTypeInput.equals("COMMAND LINE") || displayTypeInput.equals("COMMAND")) {
            boolean playAgain = true;
            while (playAgain) {
                playAgain = play(difficulty);
            }
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
