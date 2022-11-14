package org.oppblueprints;

public class Board {

    private String[] CHARS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Cell[][] board;

    public Board(int x, int y) {
        this.board = new Cell[y][x];
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                this.board[row][col] = new Cell();
            }
        }
    }

    private void generateBoard() {

    }


    public String printBoard() {
        StringBuilder sb = new StringBuilder();

        sb.append(" |");
        for (int col = 0; col < board[0].length; col++) {
            sb.append(String.format("%1$2s", col+1)).append("|");
        }
        sb.append(" \n").append(rowLine());

        for (int row = 0; row < board.length; row++) {
            sb.append(CHARS[row]).append("|");
            for (int col = 0; col < board[0].length; col++) {
                sb.append(board[row][col].getStateSymbol()).append("|");
            }
            sb.append(" \n");
            sb.append(rowLine());
        }

        return sb.toString();
    }

    private String rowLine() {
        return "-" + "+--".repeat(Math.max(0, board[0].length + 1)) + "\n";
    }



}
