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


    public GameResult(ErrorType error) {
        this.error = error;
    }

    public GameResult(boolean lost) {
        this.error = ErrorType.None;
        this.lost = lost;
    }

}
