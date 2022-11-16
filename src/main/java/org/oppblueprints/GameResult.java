package org.oppblueprints;

public class GameResult {
    private ResultErrorType error;
    private boolean lost;
    private boolean won;
    private boolean flagPlaced;
    private int tilesOpened;

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

    public ResultErrorType getError() {
        return error;
    }

    public boolean isGameLost() {
        return lost;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isGameWon() {
        return won;
    }

    public int getTilesOpened() {
        return tilesOpened;
    }

    public void setTilesOpened(int tilesOpened) {
        this.tilesOpened = tilesOpened;
    }
}
