package org.oppblueprints;

public class GameResult {
    ResultErrorType error;
    boolean lost;
    private boolean flagPlaced;

    public boolean isFlagPlaced() {
        return flagPlaced;
    }
    public void setFlagPlaced(boolean flagPlaced) {
        this.flagPlaced = flagPlaced;
    }

    public GameResult(ResultErrorType error) {
        this.error = error;
    }

    public GameResult(boolean lost) {
        this.error = ResultErrorType.None;
        this.lost = lost;
    }

}
