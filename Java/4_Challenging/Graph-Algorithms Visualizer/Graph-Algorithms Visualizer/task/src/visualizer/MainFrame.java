package visualizer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graph-Algorithms Visualizer");
        setSize(800, 600);
        setLayout(null);
        setVisible(true);

        JPanel graph = new JPanel();
        graph.setName("Graph");
        graph.setBackground(Color.BLACK);
        graph.setLayout(null);
        graph.setSize(800, 600);
        this.add(graph);

        Vertex vertex0 = new Vertex("Vertex 0", "VertexLabel 0", "0");
        vertex0.setBounds(0, 0, 50, 50);
        graph.add(vertex0);

        Vertex vertex1 = new Vertex("Vertex 1", "VertexLabel 1", "1");
        vertex1.setBounds(750, 0, 50, 50);
        graph.add(vertex1);

        Vertex vertex2 = new Vertex("Vertex 2", "VertexLabel 2", "2");
        vertex2.setBounds(0, 550, 50, 50);
        graph.add(vertex2);

        Vertex vertex3 = new Vertex("Vertex 3", "VertexLabel 3", "3");
        vertex3.setBounds(750, 550, 50, 50);
        graph.add(vertex3);
    }

}