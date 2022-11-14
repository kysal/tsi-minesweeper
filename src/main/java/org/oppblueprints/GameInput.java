package org.oppblueprints;

enum ActionType {
    Mine,
    Flag,
    Invalid
}

public class GameInput {

    String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int row_idx;
    private int col_idx;
    ActionType action;
    boolean valid;

    // THINK OF BETTER WAY TO RETURN INVALID INPUT
    private GameInput(boolean valid) {
        this.valid = valid;
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


    public static GameInput parseInput(String rawInput) {
        String[] inputArr = rawInput.split(" ");
        if (inputArr.length != 2) return new GameInput(false);

        GameInput gi = new GameInput();

        // Set action
        gi.setAction(switch (inputArr[0].toLowerCase()) {
            case "mine", "m" -> ActionType.Mine;
            case "flag", "f" -> ActionType.Flag;
            default -> ActionType.Invalid;
        });
        if (gi.getAction() == ActionType.Invalid) return new GameInput(false);

        gi.row_idx = gi.getIndexOfChar(inputArr[1].replaceAll("[0-9]", "")) -1;
        gi.col_idx = Integer.parseInt(inputArr[1].replaceAll("[A-z]", "")) -1;

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

    public void setRow_idx(int row_idx) {
        this.row_idx = row_idx;
    }

    public int getCol_idx() {
        return col_idx;
    }

    public void setCol_idx(int col_idx) {
        this.col_idx = col_idx;
    }


}
