package org.oppblueprints;

import java.util.Random;

public class Board {

    private final String[] CHARS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private final Cell[][] board;
    private final Difficulty difficulty;
    private boolean initialised;

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

    private void generateBoard(int firstMoveRow, int firstMoveCol) {
        // Make first move an empty
        int[][] surroundingIndices = {
                {firstMoveRow  , firstMoveCol  },
                {firstMoveRow+1, firstMoveCol  },
                {firstMoveRow-1, firstMoveCol  },
                {firstMoveRow  , firstMoveCol+1},
                {firstMoveRow  , firstMoveCol-1},
                {firstMoveRow+1, firstMoveCol+1},
                {firstMoveRow+1, firstMoveCol-1},
                {firstMoveRow-1, firstMoveCol+1},
                {firstMoveRow-1, firstMoveCol-1}
        };
        for (int[] index: surroundingIndices) {
            if (index[0] < 0 || index[1] < 0 || index[0] > board.length-1 || index[1] > board[0].length-1) continue;
            board[index[0]][index[1]] = new Cell(false);
        }

        // Add mines
        addMines(difficulty.mines());

        // Fill all remaining squares
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                if (this.board[row][col] == null) this.board[row][col] = new Cell();
            }
        }

    }

    public Board(Difficulty opts) {
        this.board = new Cell[opts.rows()][opts.cols()];
        this.difficulty = opts;
        this.initialised = false;
//        addMines(opts.mines());
//
//
//        for (int row = 0; row < this.board.length; row++) {
//            for (int col = 0; col < this.board[0].length; col++) {
//                if (this.board[row][col] == null) this.board[row][col] = new Cell();
//            }
//        }
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
                action(new GameInput(index[0], index[1], ActionType.Open));
            }
        }

        return mines;
    }

    public GameResult action(GameInput input) {

        // If input index out of bounds
        if (input.getRow_idx() < 0 || input.getRow_idx() > this.board.length-1 || input.getCol_idx() < 0 || input.getCol_idx() > this.board[0].length-1)
            return new GameResult(ErrorType.InvalidIndex);

        // Input open command
        if (input.action == ActionType.Open) {

            // Init board before first action
            if (!initialised) {
                generateBoard(input.getRow_idx(), input.getCol_idx());
                initialised = true;
            }

            // Return Error if tile already opened
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ErrorType.AlreadyCleared);

            // Return Error if tile is flagged
            if (this.board[input.getRow_idx()][input.getCol_idx()].isFlagged())
                return new GameResult(ErrorType.Flagged);

            // Update visual and check if opened mine
            GameResult result = this.board[input.getRow_idx()][input.getCol_idx()].reveal();
            if (result.lost) return result;

            // If not mine, check surrounding tiles for mines to produce tile number
            int surroundingMines = getSurroundingMines(input.getRow_idx(), input.getCol_idx());
            this.board[input.getRow_idx()][input.getCol_idx()].setMineNumber(surroundingMines);

            return result;

        // Input flag command
        } else if (input.action == ActionType.Flag) {
            // Return Error if tile already opened
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ErrorType.AlreadyCleared);

            // Return game result from flag
            return this.board[input.getRow_idx()][input.getCol_idx()].flag();
        }

        // Return error if no action
        return new GameResult(ErrorType.NoAction);
    }

    private String getRowTitle(int index) {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return index < 0 ? "" : getRowTitle((index / 26) - 1) + (char)(65 + index %26);
    }

    public String printBoard() {
        StringBuilder sb = new StringBuilder();

        // Top column
        sb.append(" |");
        for (int col = 0; col < board[0].length; col++) {
            sb.append(String.format("%1$2s", col+1)).append("|");
        }
        sb.append(" \n").append(rowLine());

        for (int row = 0; row < board.length; row++) {
            // Row title
            sb.append(getRowTitle(row)).append("|");
            // Cell states

            if (initialised) {
                for (int col = 0; col < board[0].length; col++) {
                    sb.append(board[row][col].getStateSymbol()).append("|");
                }
            } else {
                for (int col = 0; col < board[0].length; col++) {
                    sb.append("██").append("|");
                }
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

    public String getCellSymbol(int row, int col) {
        return board[row][col].getStateSymbol();
    }



}
