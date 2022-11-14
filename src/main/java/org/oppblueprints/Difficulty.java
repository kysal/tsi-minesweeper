package org.oppblueprints;


public record Difficulty(int rows, int cols, int mines) {

    public static Difficulty beginner() {
        return new Difficulty(8, 8, 10);
    }

    public static Difficulty intermediate() {
        return new Difficulty(13, 15, 40);
    }

    public static Difficulty expert() {
        return new Difficulty(16, 30, 99);
    }

}
