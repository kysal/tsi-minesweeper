package org.oppblueprints;

public class GameResult {
    private ResultErrorType error;
    private boolean lost;
    private boolean flagPlaced;
    private int tilesOpened;

    /**
     * Error constructor, contains an unfinished GameResult.
     * @param error Error type thrown.
     */
    public GameResult(ResultErrorType error) {
        this.error = error;
    }

    /**
     * Default constructor.
     * @param lost The new lost state of the game.
     */
    public GameResult(boolean lost) {
        this.error = ResultErrorType.None;
        this.lost = lost;
    }

    /**
     * Returns the error type of the turn result.
     * @return the error type of the turn result.
     */
    public ResultErrorType getError() {
        return error;
    }

    /**
     * Returns true if the input has resulted in losing the game.
     * @return The new lost state of the game.
     */
    public boolean isGameLost() {
        return lost;
    }

    /**
     * Returns the number of tiles opened during a turn.
     * @return The number of tiles opened during a turn.
     */
    public int getTilesOpened() {
        return tilesOpened;
    }

    /**
     * Sets the number of tiles opened during a turn.
     * @param tilesOpened The number of tiles opened during a turn.
     */
    public void setTilesOpened(int tilesOpened) {
        this.tilesOpened = tilesOpened;
    }

    /**
     * Returns true if a flag is currently on the tile.
     * @return the state of a flag currently being on the tile.
     */
    public boolean isFlagPlaced() {
        return flagPlaced;
    }

    /**
     * Places or removes a flag from the tile.
     * @param flagPlaced True to place a flag, false to remove.
     */
    public void setFlagPlaced(boolean flagPlaced) {
        this.flagPlaced = flagPlaced;
    }
}
