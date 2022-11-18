package org.oppblueprints;

import java.util.Random;

public class Board {

    private final Cell[][] board;
    private final Difficulty difficulty;
    private boolean initialised;

    /**
     * Places a given number of cells into board as mines.
     * @param mines number of mines to be placed.
     */
    private void addMines(int mines) {
        Random random = new Random();

        for (int i = 0; i < mines; i++) {
            // Randomly generate new co-ordinate for mine location
            int row = random.nextInt(this.board.length);
            int col = random.nextInt(this.board[0].length);

            // If mine location is already filled (previous mine or initial move blank space) move to next index location
            // Moves through board column first, resets to [0,0] is board end is reached until empty space found
            while (this.board[row][col] != null) {
                if (col == this.board[0].length-1) {
                    if (row == this.board.length-1) row = 0;
                    else row++;
                    col = 0;
                } else col++;
            }
            this.board[row][col] = new Cell(true);
        }
    }

    /**
     * Fills board Cell[][] with Cell objects.
     * @param firstMoveRow Row index of the first move.
     * @param firstMoveCol Column index of the first move.
     */
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

    /**
     * Default constructor.
     * Note: All cells should be null on construction, board filled during first move.
     * @param opts The Difficulty record params containing the desired rows, columns and number of mines of the board.
     */
    public Board(Difficulty opts) {
        this.board = new Cell[opts.rows()][opts.cols()];
        this.difficulty = opts;
        this.initialised = false;
    }

    /**
     * Returns the number of tiles opened during a recursive opening when the mine number is 0.
     * @param row Row index of the players move.
     * @param col Column index of the player move.
     * @return The number of tiles opened during recursive opening.
     */
    private int autoOpen(int row, int col) {
        int[][] surroundingIndices = {{row+1, col}, {row-1, col}, {row, col+1}, {row, col-1}, {row+1, col+1}, {row+1, col-1}, {row-1, col+1}, {row-1, col-1}};
        int tilesOpened = 0;
        for (int[] index : surroundingIndices) {
            if (index[0] < 0 || index[1] < 0 || index[0] > board.length-1 || index[1] > board[0].length-1) continue;
            if (board[index[0]][index[1]].isCleared()) continue;
            GameResult result = action(new GameInput(index[0], index[1], ActionType.Open));
            tilesOpened += result.getTilesOpened();
        }
        return tilesOpened;
    }

    /**
     * Returns the number of mines in the 8 surrounding tiles of a select tile index.
     * @param row Row index of the players move.
     * @param col Column index of the player move.
     * @return Number of mines in the 8 surrounding tiles.
     */
    private int getSurroundingMines(int row, int col) {
        int[][] surroundingIndices = {{row+1, col}, {row-1, col}, {row, col+1}, {row, col-1}, {row+1, col+1}, {row+1, col-1}, {row-1, col+1}, {row-1, col-1}};
        int mines = 0;
        for (int[] index : surroundingIndices) {
            if (index[0] < 0 || index[1] < 0 || index[0] > board.length-1 || index[1] > board[0].length-1) continue;
            if (board[index[0]][index[1]].hasMine()) mines++;
        }
        return mines;
    }

    /**
     * Performs the input action on the board.
     * @param input An GameInput object containing the game action desired and the row and col index where it should happen.
     * @return GameResult informing success and win/loss state.
     */
    public GameResult action(GameInput input) {

        // If input index out of bounds
        if (input.getRow_idx() < 0 || input.getRow_idx() > this.board.length-1 || input.getCol_idx() < 0 || input.getCol_idx() > this.board[0].length-1)
            return new GameResult(ResultErrorType.InvalidIndex);

        // Input open command
        if (input.action == ActionType.Open) {

            // Init board before first action
            if (!initialised) {
                generateBoard(input.getRow_idx(), input.getCol_idx());
                initialised = true;
            }

            // Return Error if tile already opened or flagged
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ResultErrorType.AlreadyCleared);
            if (this.board[input.getRow_idx()][input.getCol_idx()].isFlagged())
                return new GameResult(ResultErrorType.Flagged);

            // GAME RESULT PRODUCED
            // Update visual and check if opened mine
            GameResult result = this.board[input.getRow_idx()][input.getCol_idx()].reveal();

            if (result.isGameLost()) { revealAllMines(); return result;}
//            result.setWon(hasWon());

            // If not mine, check surrounding tiles for mines to produce tile number
            int surroundingMines = getSurroundingMines(input.getRow_idx(), input.getCol_idx());
            if (surroundingMines == 0) {
                try {
                    result.setTilesOpened(autoOpen(input.getRow_idx(), input.getCol_idx())+1);
                } catch (StackOverflowError e) {
                    System.out.println("Stack Overflow Occurred");
                }
            }
            else result.setTilesOpened(1);

            this.board[input.getRow_idx()][input.getCol_idx()].setMineNumber(surroundingMines);

            return result;

        // Input flag command
        } else if (input.action == ActionType.Flag) {
            if (!initialised) return new GameResult(ResultErrorType.FlagFirstMove);

            // Return Error if tile already opened
            if (this.board[input.getRow_idx()][input.getCol_idx()].isCleared())
                return new GameResult(ResultErrorType.AlreadyCleared);

            // Return game result from flag
            return this.board[input.getRow_idx()][input.getCol_idx()].flag();
        }

        // Return error if no action
        return new GameResult(ResultErrorType.NoAction);
    }

    /**
     * Takes in a row index and returns row title Eg. A = 0; B = 1; AA = 27; AB = 28;
     * @param index The number to convert into an alphabetic char string
     * @return The char string corresponding to the index number
     */
    private String getRowTitle(int index) {
        return index < 0 ? "" : getRowTitle((index / 26) - 1) + (char)(65 + index %26);
    }

    /**
     * Gets visual representation of current board state.
     * @return String representation of the current board state.
    */
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

    /**
     * Generates a representation of a full line for the board table printBoard() method.
     * @return A line of appropriate length to the number of columns.
     */
    private String rowLine() {
        return ("-" + "+--".repeat(Math.max(0, board[0].length + 1))).substring(0, 2 + (board[0].length) * 3)+ "\n";
    }

    /**
     * DEBUG: Checks total number of mines in board.
     * @return The number of mines on the board.
     */
    private int mineCount() {
        int mines = 0;
        for (Cell[] rows : this.board) {
            for (Cell cell : rows) {
                if (cell.hasMine()) mines++;
            }
        }
        return mines;
    }

    /**
     * Returns the visual cell state icon of a particular cell on the board.
     * @param row Input row index.
     * @param col Input row index.
     * @return The visual state icon of the cell at row col.
     */
    public String getCellSymbol(int row, int col) {
        return board[row][col].getStateSymbol();
    }

    /**
     * Returns the cell state of a particular cell on the board.
     * @param row Input row index.
     * @param col Input row index.
     * @return The state of the cell at row col.
     */
    public CellState getCellState(int row, int col) { return board[row][col].getState(); }

    /**
     * Called when game is lost, displays all mines as opened
     */
    public void revealAllMines() {
        for (Cell[] row : board) {
            for (Cell cell : row) {
                if (cell.hasMine()) cell.reveal();
            }
        }
    }
}
