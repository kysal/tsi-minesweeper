package org.oppblueprints;

import javax.swing.*;
import java.awt.*;

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

    public GUI(Difficulty difficulty) {
        board = new Board(difficulty);
        flags_left = difficulty.mines();

        frame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        mainPanel.setLayout(new FlowLayout());
        this.difficulty = difficulty;

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");


        // Options panel
        mainPanel.add(generateOptionsPanel());

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
                        System.out.println("Index: " + row + " " + col + " Mode: " + actionMode);
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
        return optionPanel;
    }

    private void postActionGameUpdate(GameResult result) {
        if (result.getError() == ResultErrorType.None) {

            if (result.lost) {
                // DISPLAY BOX
                lost = true;
//                disableGrid();
//                JFrame lostFrame = new JFrame();
//                lostFrame.add(new JLabel("You lost"));
//                lostFrame.pack();
//                lostFrame.setVisible(true);
                infoBox.setText("You lose!");
            } else if (actionMode == ActionType.Flag) {
                if (result.isFlagPlaced()) flags_left--;
                else flags_left++;
            } else if (actionMode == ActionType.Open) {
                if (board.hasWon()) {
                    disableGrid();
//                    JFrame wonFrame = new JFrame();
//                    wonFrame.add(new JLabel("You win!"));
//                    wonFrame.pack();
//                    wonFrame.setVisible(true);
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

    private void disableGrid() {
        for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
            for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                buttonGrid[row_idx][col_idx].setEnabled(false);
            }
        }
    }

}
