package org.oppblueprints;

public class Board {

    private final String[] CHARS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private final Cell[][] board;

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

            this.board[input.getRow_idx()][input.getCol_idx()].flag();
            return new GameResult(false);
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

    private String rowLine() {
        return "-" + "+--".repeat(Math.max(0, board[0].length + 1)) + "\n";
    }



}
