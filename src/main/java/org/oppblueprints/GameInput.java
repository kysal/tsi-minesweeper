package org.oppblueprints;

enum InputErrorType {
    None,
    CommandSyntax,
    UnknownCommand,
    NoFlagsLeft

}

enum ActionType {
    Open,
    Flag,
    Invalid
}

public class GameInput {

    String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int row_idx;
    private int col_idx;
    ActionType action;
    boolean valid;
    private InputErrorType error;

    // THINK OF BETTER WAY TO RETURN INVALID INPUT
    private GameInput(boolean valid) {
        this.valid = valid;
    }

    private GameInput(InputErrorType error) {
        this.error = error;
    }

    private GameInput() {

    }

    // UNSURE IF NEEDED
    private GameInput(int row_idx, int col_idx, ActionType action, boolean valid) {

    }

    @Override
    public String toString() {
        return "Action: " + action + "\nRow: " + row_idx + "\nCol: " + col_idx;
    }

    private int getIndexOfChar(String s) {
        return ALPHABET.indexOf(s);
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

        GameInput gi = new GameInput();

        // Set action
        gi.setAction(switch (inputArr[0].toLowerCase()) {
            case "open", "o" -> ActionType.Open;
            case "flag", "f" -> ActionType.Flag;
            default -> ActionType.Invalid;
        });
        if (gi.getAction() == ActionType.Invalid) return new GameInput(InputErrorType.UnknownCommand);


        System.out.println("Chars: " + inputArr[1].replaceAll("[^A-Za-z]", ""));

        gi.row_idx = GameInput.parseLetterIndex((inputArr[1].replaceAll("[^A-Za-z]", "")));

        //gi.row_idx = gi.getIndexOfChar(inputArr[1].replaceAll("[^A-Za-z]", "").toUpperCase());


        gi.col_idx = Integer.parseInt(inputArr[1].replaceAll("[^0-9]", "")) -1;


        gi.error = InputErrorType.None;

        System.out.println(gi);

        gi.valid = true;

        return gi;
    }



    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getRow_idx() {
        return row_idx;
    }

    public int getCol_idx() {
        return col_idx;
    }

    public InputErrorType getError() {
        return error;
    }

    public void setError(InputErrorType error) {
        this.error = error;
    }
}
