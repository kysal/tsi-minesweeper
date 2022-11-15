package org.oppblueprints;

public class Cell {

    private CellState visualCellState = CellState.Unmined;
    private boolean isMine;
    private boolean isCleared = false;

    public Cell() {
        isMine = false;
    }

    public Cell(boolean hasMine) {
        isMine = hasMine;
    }

    public boolean hasMine() {
        return isMine;
    }

    public boolean isCleared() {return isCleared;}

    public CellState getState() {
        return visualCellState;
    }

    public void setState(CellState state) {
        this.visualCellState = state;
    }

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

    public GameResult reveal() {
        isCleared = true;
        if (isMine) {
            this.visualCellState = CellState.Mine;
            return new GameResult(true);
        }
        return new GameResult(false);
    }

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

    public boolean isFlagged() {
        return this.visualCellState == CellState.Flag;
    }

    public boolean isFlaggedCorrectly() {
        return (hasMine()) || (isCleared && !hasMine());
    }

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
