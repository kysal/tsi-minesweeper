package org.oppblueprints;

/**
 * The Cell class represents a single tile on a board of minesweeper. It can contain a mine, and can be revealed and subsequently clears.
 */
public class Cell {

    private CellState visualCellState = CellState.UNMINED;
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
            case 0 -> CellState.MINED_NONE;
            case 1 -> CellState.MINED_1;
            case 2 -> CellState.MINED_2;
            case 3 -> CellState.MINED_3;
            case 4 -> CellState.MINED_4;
            case 5 -> CellState.MINED_5;
            case 6 -> CellState.MINED_6;
            case 7 -> CellState.MINED_7;
            case 8 -> CellState.MINED_8;
            default -> CellState.UNMINED;

        });
    }

    /**
     * Reveals if the cell has a bomb or not and sets it to cleared.
     * @return A GameResult object containing if the cell contained a mine and the game is lost.
     */
    public GameResult reveal() {
        if (isFlagged()) return new GameResult(ResultErrorType.FLAGGED);
        if (isCleared()) return new GameResult(ResultErrorType.ALREADY_CLEARED);

        isCleared = true;
        if (isMine) {
            this.visualCellState = CellState.MINE;
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
        if (this.visualCellState == CellState.FLAG) {
            this.visualCellState = CellState.UNMINED;
            result.setFlagPlaced(false);
        }
        else {
            this.visualCellState = CellState.FLAG;
            result.setFlagPlaced(true);
        }
        return result;
    }

    /**
     * Checks if the cell currently has a flag on it.
     * @return True if the cell has a flag.
     */
    public boolean isFlagged() {
        return this.visualCellState == CellState.FLAG;
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
            case UNMINED -> "██";
            case FLAG -> "⚐ ";
            case MINED_NONE -> "  ";
            case MINED_1 -> "1 ";
            case MINED_2 -> "2 ";
            case MINED_3 -> "3 ";
            case MINED_4 -> "4 ";
            case MINED_5 -> "5 ";
            case MINED_6 -> "6 ";
            case MINED_7 -> "7 ";
            case MINED_8 -> "8 ";
            case MINE -> "**";
            default -> "??";
        };
    }

}
