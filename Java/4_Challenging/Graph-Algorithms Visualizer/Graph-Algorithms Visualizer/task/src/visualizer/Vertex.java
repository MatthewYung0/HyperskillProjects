package visualizer;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private final String labelName;
    private final String labelText;
    private final int XCenterCord;
    private final int YCenterCord;
    private final int diameter = 50;

    public Vertex(int XCord, int YCord) {
        this.XCenterCord = XCord;
        this.YCenterCord = YCord;
        this.labelName = null;
        this.labelText = null;
    }

    public Vertex(String panelName, String labelName, String labelText, int XCord, int YCord) {
        this.labelName = labelName;
        this.labelText = labelText;
        this.XCenterCord = XCord;
        this.YCenterCord = YCord;
        setName(panelName);
        setPreferredSize(new Dimension(diameter,diameter));
        setLayout(null);
        createLabel();
    }

    public int getCenterX() {
        return XCenterCord;
    }

    public int getCenterY() {
        return YCenterCord;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0,0, diameter, diameter);
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, diameter, diameter);
    }

    public void createLabel() {
        JLabel label = new JLabel(labelText);
        label.setName(this.labelName);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, diameter, diameter); // Adjust label bounds to match the panel size
        add(label);
    }
}
