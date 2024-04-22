package tictactoe;

import tictactoe.utils.RoundSquareBorder;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    private final String buttonText;

    public Button(String buttonText) {
        super(buttonText);
        this.buttonText = buttonText;
        setName("Button" + this.buttonText);
        setLayout(null);
        setPreferredSize(new Dimension(50, 50));
        setBorder(new RoundSquareBorder(50));
        setForeground(Color.WHITE); // Set text color to white
        setBackground(Color.WHITE);
        setVisible(true);
    }
}
