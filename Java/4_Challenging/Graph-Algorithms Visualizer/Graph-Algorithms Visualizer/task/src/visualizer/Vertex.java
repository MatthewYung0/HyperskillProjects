package visualizer;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private final String labelName;
    private final String labelText;
    private final int diameter = 50;

    public Vertex(String panelName, String labelName, String labelText) {
        this.labelName = labelName;
        this.labelText = labelText;
        setName(panelName);
        setPreferredSize(new Dimension(diameter,diameter));
        setLayout(null);
        createLabel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0, diameter, diameter);
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, diameter, diameter);
    }

    public void createLabel() {
        JLabel label = new JLabel(labelText);
        label.setName(this.labelName);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, diameter, diameter); // Adjust label bounds to match the panel size
        add(label);
    }
}
