package visualizer;
import javax.swing.*;
import java.awt.*;

public class Edge extends JComponent {
    private final Vertex source;
    private final Vertex destination;
    private final String edgeName;
    private final String weight;
    private final char sourceID;
    private final char destinationID;
    private final int xCord;
    private final int yCord;
    private final int width;
    private final int height;

    public Edge(Vertex source, Vertex destination, String weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.sourceID = this.source.getName().charAt(source.getName().length() - 1);
        this.destinationID = this.destination.getName().charAt(destination.getName().length() - 1);
        this.edgeName = "Edge <" + this.sourceID + " -> " + this.destinationID + ">";
        xCord = Math.min(source.getCenterX(), destination.getCenterX());
        yCord = Math.min(source.getCenterY(), destination.getCenterY());
        width = Math.abs(destination.getCenterX() - source.getCenterX());
        height = Math.abs(destination.getCenterY() - source.getCenterY());
        setName(this.edgeName);
        setBounds(xCord, yCord, width, height);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        float thickness = 4.0f;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(source.getCenterX() - xCord, source.getCenterY() - yCord, destination.getCenterX() - xCord, destination.getCenterY() - yCord);
    }

    public void createLabel(JPanel graph) {
        int xCordLabel = (source.getCenterX() + destination.getCenterX()) / 2;
        int yCordLabel = (source.getCenterY() + destination.getCenterY()) / 2;
        JLabel label = new JLabel(weight);
        label.setName("EdgeLabel <" + this.sourceID + " -> " + this.destinationID + ">");
        label.setFont(label.getFont().deriveFont(16f));
        label.setBounds(xCordLabel + 5, yCordLabel + 5, 50, 20);
        graph.add(label);
    }
}