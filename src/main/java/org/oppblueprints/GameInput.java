package org.oppblueprints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInput {

    private int rowIdx;
    private int colIdx;
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
        return "Action: " + action + "\nRow: " + rowIdx + "\nCol: " + colIdx;
    }

    /**
     * Default constructor, assigns the basic attributes of a game move.
     * @param row The row index where the action should happen.
     * @param col The column index where the move should happen.
     * @param action The action to take place at a given row column index.
     */
    public GameInput(int row, int col, ActionType action) {
        this.rowIdx = row;
        this.colIdx = col;
        this.action = action;
    }

    /**
     * Parses the row alphabetic character into a row index
     * @param rawString The letters inputted by the user
     * @return The row index
     */
    private static int parseLetterIndex(String rawString) {
        String alphabet = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;
        for (char c : rawString.toUpperCase().toCharArray()) {
            index = index * 26 + alphabet.indexOf(c);
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
        if (inputArr.length != 2) return new GameInput(InputErrorType.COMMAND_SYNTAX);

        GameInput gi = new GameInput(InputErrorType.NONE);

        // Set action
        gi.action = (switch (inputArr[0].toLowerCase()) {
            case "open", "o" -> ActionType.OPEN;
            case "flag", "f" -> ActionType.FLAG;
            default -> ActionType.INVALID;
        });
        // If unknown command return error
        if (gi.getAction() == ActionType.INVALID) return new GameInput(InputErrorType.UNKNOWN_COMMAND);
        // If special chars used return error
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(inputArr[1]);
        if (matcher.find()) return new GameInput(InputErrorType.UNKNOWN_CHAR);

        // If option contains no alphabet chars return error
        if (inputArr[1].replaceAll("[^A-Za-z]", "").equals("")) return new GameInput(InputErrorType.ROW_INDEX_UNDEFINED);
        if (inputArr[1].replaceAll("[^A-Za-z]", "").length() > 6) return new GameInput(InputErrorType.ROW_INDEX_TOO_LARGE);
            // Parse letter to row index value
        gi.rowIdx = GameInput.parseLetterIndex((inputArr[1].replaceAll("[^A-Za-z]", "")));

        // If option contains no numbers return error
        if (inputArr[1].replaceAll("\\D", "").equals("")) return new GameInput(InputErrorType.COL_INDEX_UNDEFINED);
        if (inputArr[1].replaceAll("\\D", "").length() > 9) return new GameInput(InputErrorType.COL_INDEX_TOO_LARGE);
        // Parse number to row index
        gi.colIdx = Integer.parseInt(inputArr[1].replaceAll("\\D", "")) -1;

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
    public int getRowIdx() {
        return rowIdx;
    }

    /**
     * Returns the column index of the input move.
     * @return The column index of the input move.
     */
    public int getColIdx() {
        return colIdx;
    }

    /**
     * Returns true if the input has no errors currently and is usable.
     * @return True if the error type is None.
     */
    public boolean hasNoError() {
        return error == InputErrorType.NONE;
    }

    /**
     * Returns the current error type of the input.
     * @return The current error type of the input.
     */
    public InputErrorType getError() {
        return error;
    }
}
