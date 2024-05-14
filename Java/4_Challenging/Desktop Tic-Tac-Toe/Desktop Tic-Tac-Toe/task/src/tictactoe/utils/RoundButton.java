package tictactoe.utils;


import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {
    private final Color backgroundColor;

    public RoundButton(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        setContentAreaFilled(false);
        setFocusPainted(false); // Remove the border around the text
        setName("ButtonReset");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Paint the background color of the supportBar
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        // Paint the button text
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) { }
}
