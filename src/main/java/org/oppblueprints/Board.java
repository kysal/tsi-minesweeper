package org.oppblueprints;

import java.util.Random;

public class Board {

    private final String[] CHARS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private final Cell[][] board;

    private void addMines(int mines) {
        Random random = new Random();

        for (int i = 0; i < mines; i++) {
            int row = random.nextInt(this.board.length);
            int col = random.nextInt(this.board[0].length);

            while (this.board[row][col] != null) {
                if (col == this.board[0].length-1) {
                    if (row == this.board.length-1) {
                        row = 0;
                    } else {
                        row++;
                    }
                    col = 0;
                } else {
                    col++;
                }
            }


            this.board[row][col] = new Cell(true);

        }
    }

    public Board(Difficulty opts) {
        this.board = new Cell[opts.rows()][opts.cols()];

        addMines(opts.mines());


        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                if (this.board[row][col] == null) this.board[row][col] = new Cell();
            }
        }
        System.out.println(mineCount());
    }

    public Board(int x, int y) {
        this.board = new Cell[y][x];
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                this.board[row][col] = new Cell();
            }
        }

        this.board[3][2] = new Cell(true);

    }

    private void generateBoard() {

    }

    private int getSurroundingMines(int row, int col) {
        int[][] surroundingIndices = {
                {row+1, col  },
                {row-1, col  },
                {row  , col+1},
                {row  , col-1},
                {row+1, col+1},
                {row+1, col-1},
                {row-1, col+1},
                {row-1, col-1}
        };

        int mines = 0;

        for (int[] index : surroundingIndices) {
            if (index[0] < 0 || index[1] < 0 || index[0] > board.length-1 || index[1] > board[0].length-1) continue;
            if (board[index[0]][index[1]].hasMine()) mines++;
        }

        if (mines == 0) {
            for (int[] index : surroundingIndices) {
                if (index[0] < 0 || index[1] < 0 || index[0] > board.length-1 || index[1] > board[0].length-1) continue;
                action(new GameInput(index[0], index[1], ActionType.Mine));
            }
        }

        return mines;
    }

    public GameResult action(GameInput input) {

        if (input.getRow_idx() < 0 || input.getRow_idx() > this.board.length-1 || input.getCol_idx() < 0 || input.getCol_idx() > this.board[0].length-1)
            return new GameResult(ErrorType.InvalidIndex);

        if (input.action == ActionType.Mine) {
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ErrorType.AlreadyCleared);

            if (this.board[input.getRow_idx()][input.getCol_idx()].isFlagged())
                return new GameResult(ErrorType.Flagged);

            GameResult result = this.board[input.getRow_idx()][input.getCol_idx()].reveal();
            if (result.lost) return result;

            int surroundingMines = getSurroundingMines(input.getRow_idx(), input.getCol_idx());
            this.board[input.getRow_idx()][input.getCol_idx()].setMineNumber(surroundingMines);




            return result;


        } else if (input.action == ActionType.Flag) {
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ErrorType.AlreadyCleared);

            return this.board[input.getRow_idx()][input.getCol_idx()].flag();
        }

        return new GameResult(ErrorType.Temp);
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

    public boolean hasWon() {
        for (Cell[] row : this.board) {
            for (Cell cell: row) {
                if (!cell.isFlaggedCorrectly()) return false;
            }
        }
        return true;
    }

    private String rowLine() {
        return "-" + "+--".repeat(Math.max(0, board[0].length + 1)) + "\n";
    }

    private int mineCount() {
        int mines = 0;
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                if (this.board[row][col].hasMine()) mines++;
            }
        }
        return mines;
    }



}
