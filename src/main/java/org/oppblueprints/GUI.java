package org.oppblueprints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame frame;
    JPanel mainPanel;
    ActionType actionMode = ActionType.Open;
    Board board;
    JButton[][] buttonGrid;
    Difficulty difficulty;
    int flags_left;

    public GUI(Difficulty difficulty) {
        board = new Board(difficulty);
        flags_left = difficulty.mines();

        frame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        mainPanel.setLayout(new GridLayout(2,1));
        this.difficulty = difficulty;

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);

        // Options panel
        mainPanel.add(generateOptionsPanel());

        // Grid

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(difficulty.rows(), difficulty.cols()));
        buttonGrid = new JButton[difficulty.rows()][difficulty.cols()];

        for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
            for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                JButton button = new JButton(" ");
                int finalRow_idx = row_idx;
                int finalCol_idx = col_idx;
                button.addActionListener(e -> {
                    int row = finalRow_idx;
                    int col = finalCol_idx;

                    System.out.println("Index: " + row + " " + col + " Mode: " + actionMode);
                    GameInput input = new GameInput(row, col, actionMode);
                    postActionGameUpdate(board.action(input));
                });
                buttonGrid[row_idx][col_idx] = button;
                gridPanel.add(button);
            }
        }

        mainPanel.add(gridPanel);

    }

    private JPanel generateOptionsPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(0, 2));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

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
        if (result.error == ErrorType.None) {

            if (result.lost) {
                // DISPLAY BOX
                disableGrid();
                JFrame lostFrame = new JFrame();
                lostFrame.add(new JLabel("You lost"));
                lostFrame.setVisible(true);
            } else if (actionMode == ActionType.Flag) {
                if (result.isFlagPlaced()) flags_left--;
                else flags_left++;
            } else if (actionMode == ActionType.Open) {
                if (board.hasWon()) {
                    disableGrid();
                    JFrame wonFrame = new JFrame();
                    wonFrame.add(new JLabel("You win!"));
                    wonFrame.setVisible(true);
                }
            }

            for (int row_idx = 0; row_idx < difficulty.rows(); row_idx++) {
                for (int col_idx = 0; col_idx < difficulty.cols(); col_idx++) {
                    buttonGrid[row_idx][col_idx].setText(board.getCellSymbol(row_idx, col_idx));
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
