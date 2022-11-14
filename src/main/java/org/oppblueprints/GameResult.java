package org.oppblueprints;

enum ErrorType {
    None,
    InvalidIndex,
    AlreadyCleared,
    Flagged,
    Temp
}

public class GameResult {
    ErrorType error;

    boolean lost;

    private boolean flagPlaced;

    public boolean isFlagPlaced() {
        return flagPlaced;
    }
    public void setFlagPlaced(boolean flagPlaced) {
        this.flagPlaced = flagPlaced;
    }

    public GameResult(ErrorType error) {
        this.error = error;
    }

    public GameResult(boolean lost) {
        this.error = ErrorType.None;
        this.lost = lost;
    }

}
