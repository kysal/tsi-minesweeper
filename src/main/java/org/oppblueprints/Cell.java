package org.oppblueprints;

enum CellState {
    Unmined,
    MinedNone,
    Mined1,
    Mined2,
    Mined3,
    Mined4,
    Mined5,
    Mined6,
    Mined7,
    Mined8,
    Flag,
    Mine
}

public class Cell {

    private CellState visualCellState = CellState.Unmined;
    private boolean isMine;

    public Cell() {
        isMine = false;
    }

    public boolean hasMine() {
        return isMine;
    }

    public CellState getState() {
        return visualCellState;
    }

    public void setState(CellState state) {
        this.visualCellState = state;
    }

    public String getStateSymbol() {
        return switch (this.visualCellState) {
            case Unmined -> "██";
            case Flag -> "⚐ ";
            case Mined1 -> "1 ";
            case Mined2 -> "2 ";
            case Mined3 -> "3 ";
            case Mined4 -> "4 ";
            case Mined5 -> "5 ";
            case Mined6 -> "6 ";
            case Mined7 -> "7 ";
            case Mined8 -> "8 ";
            default -> "  ";
        };
    }

}
