package org.oppblueprints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum InputErrorType {
    None,
    CommandSyntax,
    UnknownCommand,
    RowIndexUndefined,
    ColIndexUndefined,
    UnknownChar,
    ColIndexTooLarge,
    RowIndexTooLarge

}

enum ActionType {
    Open,
    Flag,
    Invalid
}

public class GameInput {

    private int row_idx;
    private int col_idx;
    ActionType action;
    private InputErrorType error;

    private GameInput(InputErrorType error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Action: " + action + "\nRow: " + row_idx + "\nCol: " + col_idx;
    }

    public GameInput(int row, int col, ActionType action) {
        this.row_idx = row;
        this.col_idx = col;
        this.action = action;
    }

    public static int parseLetterIndex(String rawString) {
        String ALPHABET = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;
        for (char c : rawString.toUpperCase().toCharArray()) {
            index = index * 26 + ALPHABET.indexOf(c);
        }
        return index-1;
    }

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

    public ActionType getAction() {
        return action;
    }

    public int getRow_idx() {
        return row_idx;
    }

    public int getCol_idx() {
        return col_idx;
    }

    public boolean hasNoError() {
        return error == InputErrorType.None;
    }

    public InputErrorType getError() {
        return error;
    }
}
