package org.oppblueprints;


public record Difficulty(int rows, int cols, int mines) {

    // Beginner: 8x8 10 miens
    public static Difficulty beginner() {
        return new Difficulty(8, 8, 10);
    }

    // Intermediate: 13x15 40 mines
    public static Difficulty intermediate() {
        return new Difficulty(13, 15, 40);
    }

    // Expert: 16x30 99 mines
    public static Difficulty expert() {
        return new Difficulty(16, 30, 99);
    }

}
