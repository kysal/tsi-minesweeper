package org.oppblueprints;


public class Difficulty {
    private final int rows;
    private final int cols;
    private final int mines;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }

    public Difficulty(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
    }

    public static Difficulty beginner() {
        return new Difficulty(8,8,10);
    }

    public static Difficulty intermediate() {
        return new Difficulty(13,15,40);
    }

    public static Difficulty expert() {
        return new Difficulty(16,30,99);
    }

}
