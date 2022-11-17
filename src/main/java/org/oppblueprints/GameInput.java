package org.oppblueprints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInput {

    private int row_idx;
    private int col_idx;
    ActionType action;
    private InputErrorType error;

    /**
     * Error constructor, an unfinished input to note that an error has taken place and must be handled.
     * @param error The error type that has occurred.
     */
    private GameInput(InputErrorType error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Action: " + action + "\nRow: " + row_idx + "\nCol: " + col_idx;
    }

    /**
     * Default constructor, assigns the basic attributes of a game move.
     * @param row The row index where the action should happen.
     * @param col The column index where the move should happen.
     * @param action The action to take place at a given row column index.
     */
    public GameInput(int row, int col, ActionType action) {
        this.row_idx = row;
        this.col_idx = col;
        this.action = action;
    }

    /**
     * Parses the row alphabetic character into a row index
     * @param rawString The letters inputted by the user
     * @return The row index
     */
    private static int parseLetterIndex(String rawString) {
        String ALPHABET = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;
        for (char c : rawString.toUpperCase().toCharArray()) {
            index = index * 26 + ALPHABET.indexOf(c);
        }
        return index-1;
    }

    /**
     * Takes a raw string and parses it to a GameInput object.
     * String must be made up of two words: an action and co-ordinates represented as an alphabetic character and a number
     * @param rawInput The raw input string from the user.
     * @return A GameInput object usable for board actions.
     */
    public static GameInput parseInput(String rawInput) {
        String[] inputArr = rawInput.split(" ");
        if (inputArr.length != 2) return new GameInput(InputErrorType.CommandSyntax);

        GameInput gi = new GameInput(InputErrorType.None);

        // Set action
        gi.action = (switch (inputArr[0].toLowerCase()) {
            case "open", "o" -> ActionType.Open;
            case "flag", "f" -> ActionType.Flag;
            default -> ActionType.Invalid;
        });
        // If unknown command return error
        if (gi.getAction() == ActionType.Invalid) return new GameInput(InputErrorType.UnknownCommand);
        // If special chars used return error
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(inputArr[1]);
        if (matcher.find()) return new GameInput(InputErrorType.UnknownChar);

        // If option contains no alphabet chars return error
        if (inputArr[1].replaceAll("[^A-Za-z]", "").equals("")) return new GameInput(InputErrorType.RowIndexUndefined);
        if (inputArr[1].replaceAll("[^A-Za-z]", "").length() > 6) return new GameInput(InputErrorType.RowIndexTooLarge);
            // Parse letter to row index value
        gi.row_idx = GameInput.parseLetterIndex((inputArr[1].replaceAll("[^A-Za-z]", "")));

        // If option contains no numbers return error
        if (inputArr[1].replaceAll("[^0-9]", "").equals("")) return new GameInput(InputErrorType.ColIndexUndefined);
        if (inputArr[1].replaceAll("[^0-9]", "").length() > 9) return new GameInput(InputErrorType.ColIndexTooLarge);
        // Parse number to row index
        gi.col_idx = Integer.parseInt(inputArr[1].replaceAll("[^0-9]", "")) -1;

        return gi;
    }

    /**
     * Returns the action type of the input.
     * @return The action type of the input.
     */
    public ActionType getAction() {
        return action;
    }

    /**
     * Returns the row index of the input move.
     * @return The row index of the input move.
     */
    public int getRow_idx() {
        return row_idx;
    }

    /**
     * Returns the column index of the input move.
     * @return The column index of the input move.
     */
    public int getCol_idx() {
        return col_idx;
    }

    /**
     * Returns true if the input has no errors currently and is usable.
     * @return True if the error type is None.
     */
    public boolean hasNoError() {
        return error == InputErrorType.None;
    }

    /**
     * Returns the current error type of the input.
     * @return The current error type of the input.
     */
    public InputErrorType getError() {
        return error;
    }
}
