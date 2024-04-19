package visualizer;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private final String labelName;
    private final String labelText;
    private final int XCenterCord;
    private final int YCenterCord;
    private final int diameter = 50;
    private boolean isVisited = false;
    private Color vertexColour;

    public Vertex(int XCord, int YCord) {
        this.XCenterCord = XCord;
        this.YCenterCord = YCord;
        this.labelName = null;
        this.labelText = null;
        this.vertexColour = Color.BLACK; // Default color
    }

    public Vertex(String panelName, String labelName, String labelText, int XCord, int YCord) {
        this.labelName = labelName;
        this.labelText = labelText;
        this.XCenterCord = XCord;
        this.YCenterCord = YCord;
        this.vertexColour = Color.BLACK; // Default color
        setName(panelName);
        setPreferredSize(new Dimension(diameter,diameter));
        setLayout(null);
        createLabel();
    }

    public boolean getIsVisited() { return isVisited; }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
        if (!this.isVisited) {
            setColourVisited();
        } else {
            resetColour();
        }
    }

    public String getVertexText() {
        return this.labelText;
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

    private void resetColour() {
        vertexColour = Color.BLACK; // Reset to default color
        repaint(); // Repaint the panel to reflect the color change
    }

    private void setColourVisited() {
        vertexColour = Color.RED; // Set color for visited vertex
        repaint(); // Repaint the panel to reflect the color change
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
