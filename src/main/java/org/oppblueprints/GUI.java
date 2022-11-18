package org.oppblueprints;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

/**
 * GUI extension class for the Game class for Minesweeper.
 * When used handles basic game methods and variables in place of the command line functionality.
 */
public class GUI {
    JFrame frame;
    JPanel mainPanel;
    ActionType actionMode = ActionType.Open;
    Board board;
    JButton[][] buttonGrid;
    Difficulty difficulty;
    int flags_left;
    boolean lost = false;
    JLabel infoBox = new JLabel("");
    int cellsToOpen;
    int secondsPassed = 0;
    boolean timerActive = false;

    /**
     * Default constructor. Initialises the GUI and game variables.
     * Display options panel and minesweeper board.
     * Game functionality similar to the Game.play() method.
     * @param difficulty A Difficulty record that sets the rows, columns and mine amount.
     */
    private GUI(Difficulty difficulty) {
        board = new Board(difficulty);
        flags_left = difficulty.mines();
        this.difficulty = difficulty;
        cellsToOpen = (difficulty.rows() * difficulty.cols()) - difficulty.mines();

        frame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        mainPanel.setLayout(new FlowLayout());


        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");


        // Options panel
        JPanel optPanel = generateOptionsPanel();
        JLabel timerLabel = new JLabel("0s");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timerActive) {
                    secondsPassed++;
                    timerLabel.setText(secondsPassed + "s");
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
        optPanel.add(timerLabel);

        mainPanel.add(optPanel);

        // Grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setSize(200, 200);
        gridPanel.setLayout(new GridLayout(difficulty.rows(), difficulty.cols()));
        buttonGrid = new JButton[difficulty.rows()][difficulty.cols()];

        for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
            for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                JButton button = new JButton("██");
                button.setSize(60,60);
                final int row = row_idx;
                final int col = col_idx;
                button.addActionListener(e -> {
                    if (!lost) {
                        GameInput input = new GameInput(row, col, actionMode);
                        postActionGameUpdate(board.action(input));
                    }
                });
                buttonGrid[row_idx][col_idx] = button;
                gridPanel.add(button);
            }
        }

        mainPanel.add(gridPanel);
        mainPanel.add(infoBox);

//        frame.setSize(600, 800);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Constructs the options panel containing the change mode buttons and mode label.
     * @return The JPanel options panel.
     */
    private JPanel generateOptionsPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout());
        optionPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        optionPanel.setSize(0, 50);

        JLabel modeDisplay = new JLabel("Currently In: Dig Mode");
        JButton digModeSelector = new JButton("Dig Mode");
        digModeSelector.addActionListener(e -> {
            actionMode = ActionType.Open;
            modeDisplay.setText("Currently In: Dig Mode");
        } );
        JButton flagModeSelector = new JButton("Flag Mode");
        flagModeSelector.addActionListener(e -> {
            actionMode = ActionType.Flag;
            modeDisplay.setText("Currently In: Flag Mode");
        });


        optionPanel.add(digModeSelector);
        optionPanel.add(flagModeSelector);
        optionPanel.add(modeDisplay);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            board = new Board(difficulty);
            flags_left = difficulty.mines();
            cellsToOpen = (difficulty.rows() * difficulty.cols()) - difficulty.mines();
            secondsPassed = 0;
            lost = false;
            timerActive = false;
            for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
                for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                    buttonGrid[row_idx][col_idx].setText("██");
                    buttonGrid[row_idx][col_idx].setForeground(Color.BLACK);
                    buttonGrid[row_idx][col_idx].setBackground(null);
                }
            }
        });
        optionPanel.add(restartButton);

        return optionPanel;
    }

    /**
     * The actions taken place after a move has been taken.
     * Contains result error checking, variable updates, and visual updates.
     * @param result The GameResult object containing move aftermath information.
     */
    private void postActionGameUpdate(GameResult result) {
        if (result.getError() == ResultErrorType.None) {

            if (result.isGameLost()) {
                // DISPLAY BOX
                lost = true;
                timerActive = false;
                infoBox.setText("You lose!");
            } else if (actionMode == ActionType.Flag) {
                if (result.isFlagPlaced()) flags_left--;
                else flags_left++;
            } else if (actionMode == ActionType.Open) {
                if (!timerActive) timerActive = true;
                cellsToOpen -= result.getTilesOpened();
                if (cellsToOpen <= 0) {
                    timerActive = false;
                    disableGrid();
                    infoBox.setText("You win!");
                }
            }

            for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
                for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                    String symbol = board.getCellSymbol(row_idx, col_idx);
                    buttonGrid[row_idx][col_idx].setText(symbol);

                    buttonGrid[row_idx][col_idx].setForeground( switch (board.getCellState(row_idx, col_idx)) {
                        case Mined1 -> Color.BLUE;
                        case Mined2 -> Color.GREEN;
                        case Mined3 -> Color.RED;
                        case Mined4 -> new Color(25, 25, 112);
                        case Mined5 -> new Color(128,0,0);
                        case Mined6 -> new Color(70,130,180);
                        case Mined7 -> new Color(139,0,139);
                        case Mined8 -> Color.GRAY;
                        default -> Color.BLACK;
                    });

                    if (board.getCellState(row_idx, col_idx) == CellState.Mine) { buttonGrid[row_idx][col_idx].setBackground(Color.RED); }
                }
            }
        }
    }

    /**
     * Disables the minesweeper grid for when input is no longer necessary.
     */
    private void disableGrid() {
        for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
            for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                buttonGrid[row_idx][col_idx].setEnabled(false);
            }
        }
    }

    /**
     * Sets up Minesweeper game and displays GUI to user
     * @param difficulty The game difficulty containing the number of rows, columns and mines in the game.
     */
    public static void playGUI(Difficulty difficulty) {
        new GUI(difficulty);
    }


}
