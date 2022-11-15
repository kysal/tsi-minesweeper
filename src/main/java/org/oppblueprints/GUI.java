package org.oppblueprints;

import javax.swing.*;
import java.awt.*;

enum GUIMode {
    Open,
    Flag
}

public class GUI {
    JFrame frame;
    JPanel mainPanel;
    GUIMode mode = GUIMode.Open;

    public GUI(Difficulty difficulty) {
        frame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        mainPanel.setLayout(new GridLayout(2,1));

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);

        // Options panel
        JButton[] options = {new JButton("Dig Mode"), new JButton("Flag Mode")};
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(0, 2));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        for (int i = 0; i < options.length; i++) {
            optionPanel.add(options[i]);
        }
        mainPanel.add(optionPanel);

        // Grid

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(difficulty.rows(), difficulty.cols()));

        for (int i = 0; i < difficulty.rows()*difficulty.cols(); i++) {
            gridPanel.add(new JButton("Test " + i));
        }

        mainPanel.add(gridPanel);

    }

}
