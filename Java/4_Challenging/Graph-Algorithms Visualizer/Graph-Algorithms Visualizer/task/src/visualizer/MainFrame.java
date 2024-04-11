package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame extends JFrame implements MouseListener {

    private int mouseX = 0;
    private int mouseY = 0;
    private JPanel graph;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graph-Algorithms Visualizer");
        setSize(800, 600);
        setLayout(null);
        initGraph();
        setVisible(true);
    }

    private void initGraph() {
        graph = new JPanel();
        graph.setName("Graph");
        graph.setBackground(Color.BLACK);
        graph.setLayout(null);
        graph.setSize(800, 600);
        graph.addMouseListener(this);
        this.add(graph);
    }

    private void createVertex(String vertexID) {
        Vertex vertex = new Vertex("Vertex " + vertexID, "VertexLabel " + vertexID, vertexID);
        // Set vertex position
        vertex.setBounds(mouseX-25, mouseY-25, 50, 50);
        graph.add(vertex);
        graph.repaint();
    }

    private String getVertexID() {
        String vertexID = null;
        boolean validInput = false;
        while (!validInput) {
            // Prompt for vertex ID
            vertexID = JOptionPane.showInputDialog(null, "Enter Vertex ID (Should be 1 char):", "Vertex", JOptionPane.PLAIN_MESSAGE);
            if (vertexID == null) {
                // User clicked cancel
                return null;
            }
            if (vertexID.length() == 1 && (Character.isLetter(vertexID.charAt(0)) || Character.isDigit(vertexID.charAt(0)))) {
                validInput = true;
            }
        }
        return vertexID;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        this.mouseX = mouseEvent.getX();
        this.mouseY = mouseEvent.getY();
        String vertexID = getVertexID();
        if (vertexID != null) {
            // Create vertex
            createVertex(vertexID);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {    }
}