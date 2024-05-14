package tictactoe;

import tictactoe.enums.SYMBOL;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    private SYMBOL symbol = null;

    public Button(String buttonName) {
        super();
        setName("Button" + buttonName);
        setPreferredSize(new Dimension(100, 50)); // Adjust size as needed
        setForeground(Color.BLACK); // Set text color
        setBackground(Color.ORANGE);
    }

    public void setSymbol(SYMBOL symbol) {
        if (symbol == null) {
            this.symbol = null;
            setText(" ");
        } else {
            this.symbol = symbol;
            setText((this.symbol).toString());
        }
        repaint();
    }

    public SYMBOL getSymbol() {
        return this.symbol;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Draw rounded rectangle as button background
        g2.setColor(getBackground());
        // Adjust the arc width to change the roundness
        int radii = 40;
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radii, radii);
        // Set border color and thickness
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3)); // Adjust thickness as needed
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radii, radii);
        // Draw button text
        g2.setFont(getFont().deriveFont(Font.BOLD, 64)); // Adjust font size as needed
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2;
        g2.drawString(getText(), x, y);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) { /* No need to paint border, as we are drawing rounded rectangle */}

    // Optionally, you can override getPreferredSize to ensure proper size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 50); // Adjust size as needed
    }
}