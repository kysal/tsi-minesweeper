package org.oppblueprints;

/**
 * The Cell class represents a single tile on a board of minesweeper. It can contain a mine, and can be revealed and subsequently clears.
 */
public class Cell {

    private CellState visualCellState = CellState.Unmined;
    private final boolean isMine;
    private boolean isCleared = false;

    /**
     * Default constructor of a cell that doesn't have a mine.
     */
    public Cell() {
        isMine = false;
    }

    /**
     * Constructor of cell where the mine state needs to be determined.
     * @param hasMine The mine state of the cell.
     */
    public Cell(boolean hasMine) {
        isMine = hasMine;
    }

    /**
     * Returns if the cell contains a mine.
     * @return True if the cell contains a mine.
     */
    public boolean hasMine() {
        return isMine;
    }

    /**
     * Returns whether the cell has already been cleared.
     * @return True is the cell is cleared.
     */
    public boolean isCleared() {return isCleared;}

    /**
     * Returns the visual cell state.
     * @return The visual cell state.
     */
    public CellState getState() {
        return visualCellState;
    }

    /**
     * Sets the state of the Cell.
     * @param state The new state of the cell.
     */
    public void setState(CellState state) {
        this.visualCellState = state;
    }

    /**
     * Sets the state of the cell to how many mines surround it.
     * @param mines The number of mines surround the cell.
     */
    public void setMineNumber(int mines) {
        this.setState(switch (mines) {
            case 0 -> CellState.MinedNone;
            case 1 -> CellState.Mined1;
            case 2 -> CellState.Mined2;
            case 3 -> CellState.Mined3;
            case 4 -> CellState.Mined4;
            case 5 -> CellState.Mined5;
            case 6 -> CellState.Mined6;
            case 7 -> CellState.Mined7;
            case 8 -> CellState.Mined8;
            default -> CellState.Unmined;

        });
    }

    /**
     * Reveals if the cell has a bomb or not and sets it to cleared.
     * @return A GameResult object containing if the cell contained a mine and the game is lost.
     */
    public GameResult reveal() {
        isCleared = true;
        if (isMine) {
            this.visualCellState = CellState.Mine;
            return new GameResult(true);
        }
        return new GameResult(false);
    }

    /**
     * Sets an unflagged cell to flagged and a flagged cell to unflagged.
     * @return A GameResult object containing if the cell flag was added or removed.
     */
    public GameResult flag() {
        GameResult result = new GameResult(false);
        if (this.visualCellState == CellState.Flag) {
            this.visualCellState = CellState.Unmined;
            result.setFlagPlaced(false);
        }
        else {
            this.visualCellState = CellState.Flag;
            result.setFlagPlaced(true);
        }
        return result;
    }

    /**
     * Checks if the cell currently has a flag on it.
     * @return True if the cell has a flag.
     */
    public boolean isFlagged() {
        return this.visualCellState == CellState.Flag;
    }

    /**
     * Checks if a cell is in the correct state for a win.
     * @return True if the cell contains a mine or.
     */
    public boolean isFlaggedCorrectly() {
        return (hasMine()) || (isCleared && !hasMine());
    }

    /**
     * Returns the symbol associated with a given cell state the cell is.
     * @return A string to be shown to visually represent this cell's state.
     */
    public String getStateSymbol() {
        return switch (this.visualCellState) {
            case Unmined -> "██";
            case Flag -> "⚐ ";
            case MinedNone -> "  ";
            case Mined1 -> "1 ";
            case Mined2 -> "2 ";
            case Mined3 -> "3 ";
            case Mined4 -> "4 ";
            case Mined5 -> "5 ";
            case Mined6 -> "6 ";
            case Mined7 -> "7 ";
            case Mined8 -> "8 ";
            case Mine -> "**";
            default -> "??";
        };
    }

}
