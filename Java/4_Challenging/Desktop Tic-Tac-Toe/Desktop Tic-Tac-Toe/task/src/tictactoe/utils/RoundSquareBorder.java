package tictactoe.utils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundSquareBorder extends AbstractBorder {
    private final int radius;

    public RoundSquareBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the rounded rectangle with the fill color
        graphics.setColor(Color.BLACK);
        graphics.fillRoundRect(x, y, width - 1, height - 1, radius, radius);

        // Draw the border with the background color
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = this.radius;
        return insets;
    }
}
