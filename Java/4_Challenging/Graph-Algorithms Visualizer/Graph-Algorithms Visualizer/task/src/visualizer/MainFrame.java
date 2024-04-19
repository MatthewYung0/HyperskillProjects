package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;

public class MainFrame extends JFrame {

    private int mouseX = 0;
    private int mouseY = 0;
    private JPanel graph;
    private MODE mode = MODE.ADD_VERTEX;
    private MouseAdapter mouseAdapter;
    private final String currentOptionString = "Current Mode -> ";
    private final String addAVertexString = "Add a Vertex";
    private final String addAnEdgeString = "Add an Edge";
    private final String removeAVertexString = "Remove a Vertex";
    private final String removeAnEdgeString = "Remove an Edge";
    private final String addNoneString = "None";
    private JLabel currentMode;
    private JLabel algoLabel;
    private Vertex selectedVertex1 = null;
    private Vertex selectedVertex2 = null;
    private Vertex startingVertex = null;
    private ArrayList<String> vertexesVisited = new ArrayList<>();

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graph-Algorithms Visualizer");
        setSize(800, 600);
        setLayout(null);
        initCurrentMode();
        initGraph();
        initMenu();
        initMouseAdapter();
        addAlgoJLabel();
        setVisible(true);
    }

    private void initCurrentMode() {
        currentMode = new JLabel(currentOptionString + addAVertexString);
        currentMode.setName("Mode");
        currentMode.setForeground(Color.BLACK);
        currentMode.setBounds(10, 530, 300, 50);
        add(currentMode);
    }

    private void initMouseAdapter() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
                switch (mode) {
                    case ADD_VERTEX -> createVertex(Objects.requireNonNull(getVertexID()));
                    case ADD_EDGE -> handleEdgeCreation();
                    case REMOVE_VERTEX -> handleVertexRemoval();
                    case REMOVE_EDGE -> removeEdge();
                }
            }
        };
        graph.addMouseListener(mouseAdapter);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        initFileMenuItems(fileMenu);
        JMenu modeMenu = new JMenu("Mode");
        menuBar.add(modeMenu);
        JMenu algorithms = new JMenu("Algorithms");
        menuBar.add(algorithms);
        initModeMenuItems(modeMenu);
        initAlgoMenuItems(algorithms);
    }

    private void initAlgoMenuItems(JMenu algoMenu) {
        String dfsString = "Depth-First Search";
        String bfsString = "Breadth-First Search";
        JMenuItem dfs = new JMenuItem(dfsString);
        JMenuItem bfs = new JMenuItem(bfsString);
        dfs.setName(dfsString);
        bfs.setName(bfsString);

        ActionListener algo = algoEvent -> {
            mode = MODE.NONE;
            setCurrentOption(mode);
            String algoName = ((JMenuItem) algoEvent.getSource()).getText();
            algoLabel.setText("Please choose a starting vertex");
            refreshJFrame();
            graph.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    super.mouseClicked(mouseEvent);
                    mouseX = mouseEvent.getX();
                    mouseY = mouseEvent.getY();
                    startingVertex = (Vertex) getComponent(Vertex.class);
                    if (algoName.equals(dfsString)) {
                        runDFS(startingVertex);
                    } else if (algoName.equals(bfsString)) {
                        runBFS(startingVertex);
                    }
                    graph.removeMouseListener(this);
                }
            });
            startingVertex = null;
        };
        dfs.addActionListener(algo);
        bfs.addActionListener(algo);
        algoMenu.add(dfs);
        algoMenu.add(bfs);
    }

    private void initFileMenuItems(JMenu fileMenu) {
        String resetString = "New";
        JMenuItem reset = new JMenuItem("New");
        String exitString = "Exit";
        JMenuItem exit = new JMenuItem("Exit");
        reset.setName(resetString);
        exit.setName(exitString);
        reset.addActionListener(event -> {
            graph.removeAll();
            graph.repaint();
            mode = MODE.ADD_VERTEX;
            setCurrentOption(mode);
        });
        exit.addActionListener(event -> System.exit(0));
        fileMenu.add(reset);
        fileMenu.add(exit);
    }

    private void initModeMenuItem(JMenu modeMenu, String label, MODE mode) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setName(label);
        modeMenu.add(menuItem);
        menuItem.addActionListener(event -> {
            this.mode = mode;
            setCurrentOption(mode);
        });
    }

    private void initModeMenuItems(JMenu modeMenu) {
        initModeMenuItem(modeMenu, addAVertexString, MODE.ADD_VERTEX);
        initModeMenuItem(modeMenu, addAnEdgeString, MODE.ADD_EDGE);
        initModeMenuItem(modeMenu, removeAVertexString, MODE.REMOVE_VERTEX);
        initModeMenuItem(modeMenu, removeAnEdgeString, MODE.REMOVE_EDGE);
        initModeMenuItem(modeMenu, addNoneString, MODE.NONE);
    }

    private void initGraph() {
        graph = new JPanel();
        graph.setName("Graph");
        graph.setBackground(new Color(0, 0, 0, 0));
        graph.setLayout(null);
        graph.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        graph.setSize(800, 600);
        graph.addMouseListener(mouseAdapter);
        this.add(graph);
    }

    private void refreshJFrame() {
        revalidate();
        repaint();
    }

    private void runBFS(Vertex startVertex) {
        algoLabel.setText("Please wait...");
        refreshJFrame();

        if (startVertex == null) {
            return;
        }

        // Clear previously visited vertices
        vertexesVisited.clear();

        // Initialize a queue for BFS traversal
        Queue<Vertex> queue = new LinkedList<>();

        // Mark the start vertex as visited and enqueue it
        startVertex.setIsVisited(true);
        queue.add(startVertex);

        // Perform BFS traversal
        while (!queue.isEmpty()) {
            // Dequeue a vertex from the queue
            Vertex currentVertex = queue.poll();

            // Perform any necessary operations on the current vertex
            vertexesVisited.add(currentVertex.getVertexText());

            // Get all adjacent vertices of the current vertex
            for (Component component : graph.getComponents()) {
                if (component instanceof Edge edge) {
                    if (edge.getSource() == currentVertex) {
                        Vertex adjacentVertex = edge.getDestination();
                        if (!adjacentVertex.getIsVisited()) {
                            // Mark the adjacent vertex as visited and enqueue it
                            adjacentVertex.setIsVisited(true);
                            queue.add(adjacentVertex);
                        }
                    }
                }
            }
        }
        Timer timer = getTimer("BFS : ");
        timer.start();
    }

    private void runDFS(Vertex startVertex) {
        algoLabel.setText("Please wait...");
        refreshJFrame();
        if (startVertex == null) {
            return;
        }
        // Mark the current vertex as visited
        startVertex.setIsVisited(true);
        graph.revalidate();
        graph.repaint();

        // Perform any necessary operations on the current vertex (e.g., display or process it)
        vertexesVisited.add(startVertex.getVertexText());

        // Get all adjacent edges of the current vertex and sort them by weight
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        for (Component component : graph.getComponents()) {
            if (component instanceof Edge edge) {
                if (edge.getSource() == startVertex && !edge.getDestination().getIsVisited()) {
                    adjacentEdges.add(edge);
                }
            }
        }
        adjacentEdges.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getWeight())));

        // Traverse adjacent vertices in ascending order of edge weights
        for (Edge edge : adjacentEdges) {
            Vertex destinationVertex = edge.getDestination();
            if (!destinationVertex.getIsVisited()) {
                // Perform any necessary operations on the current vertex (e.g., display or process it)
                // Recursive call to visit the next vertex
                runDFS(destinationVertex);
            }
        }
        Timer timer = getTimer("DFS : ");
        timer.start();
    }

    private Timer getTimer(String algorithmName) {
        Timer timer = new Timer(2000, e -> {
            StringBuilder result = new StringBuilder(algorithmName);
            for (int i = 0; i < vertexesVisited.size(); i++) {
                if (i != vertexesVisited.size() - 1) {
                    result.append(vertexesVisited.get(i)).append(" -> ");
                } else {
                    result.append(vertexesVisited.get(i));
                }
            }
            algoLabel.setText(result.toString());
            refreshJFrame();
        });
        timer.setRepeats(false); // Only run once
        return timer;
    }

    public void clearAlgoData() {
        algoLabel.setText("");
        refreshJFrame();
        vertexesVisited.clear();
    }

    private void resetIsVisited() {
        for (Component component : graph.getComponents()) {
            if (component instanceof Vertex) {
                ((Vertex) component).setIsVisited(false);
            }
        }
    }

    private void setCurrentOption(MODE mode) {
        clearAlgoData();
        resetIsVisited();
        switch (mode) {
            case ADD_VERTEX -> currentMode.setText(currentOptionString + addAVertexString);
            case ADD_EDGE -> currentMode.setText(currentOptionString + addAnEdgeString);
            case REMOVE_VERTEX -> currentMode.setText(currentOptionString + removeAVertexString);
            case REMOVE_EDGE -> currentMode.setText(currentOptionString + removeAnEdgeString);
            case NONE -> currentMode.setText(currentOptionString + addNoneString);
        }
    }

    private void addAlgoJLabel() {
        algoLabel = new JLabel();
        algoLabel.setForeground(Color.BLACK);
        algoLabel.setName("Display");
        algoLabel.setBounds(10, 550, 300, 50);
        add(algoLabel);
    }

    private String getWeight() {
        String weightNum = null;
        boolean validInput = false;
        while (!validInput) {
            // Prompt for weightNum
            weightNum = JOptionPane.showInputDialog(null, "Enter Weight:", "Input", JOptionPane.PLAIN_MESSAGE);
            if (weightNum == null) {
                // User clicked cancel
                return "";
            } else if (weightNum.isEmpty()) {
                continue;
            }
            if (weightNum.length() > 1 && weightNum.charAt(0) == '-') {
                if (Character.isDigit(weightNum.charAt(1))) {
                    validInput = true;
                }
            } else if (Character.isDigit(weightNum.charAt(0))) {
                validInput = true;
            }
        }
        return weightNum;
    }

    private Component getComponent(Class<?> type) {
        for (Component component : graph.getComponents()) {
            if (type.isInstance(component)) {
                if (component.getBounds().contains(this.mouseX, this.mouseY)) {
                    return component;
                }
            }
        }
        return null;
    }

    private void createEdge() {
        if (!selectedVertex1.equals(selectedVertex2)) {
            String weight = getWeight();
            if (selectedVertex1 != null && selectedVertex2 != null && !weight.isEmpty()) {
                // Create an edge with weight and add it to the graph
                Edge edge1 = new Edge(selectedVertex1, selectedVertex2, weight);
                Edge edge2 = new Edge(selectedVertex2, selectedVertex1, weight);
                graph.add(edge1);
                graph.add(edge2);
                edge1.createLabel(graph);
                graph.repaint();
            }
        }
    }

    private void handleEdgeCreation() {
        Vertex clickedVertex = (Vertex) getComponent(Vertex.class);
        if (clickedVertex != null) {
            if (selectedVertex1 == null) {
                selectedVertex1 = clickedVertex;
            } else {
                selectedVertex2 = clickedVertex;
                createEdge();
                // Reset selected vertices for next edge creation
                selectedVertex1 = null;
                selectedVertex2 = null;
            }
        }
    }

    private boolean isPointNearEdge(Edge edge, int x, int y) {
        int x1 = edge.getSource().getCenterX();
        int y1 = edge.getSource().getCenterY();
        int x2 = edge.getDestination().getCenterX();
        int y2 = edge.getDestination().getCenterY();
        double distance = pointToLineDistance(x, y, x1, y1, x2, y2);
        // Define a threshold for considering the point near the edge
        double threshold = 15.0; // Adjust this threshold as needed
        return distance <= threshold;
    }

    private double pointToLineDistance(int x, int y, int x1, int y1, int x2, int y2) {
        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;
        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = dot / len_sq;
        double xx, yy;
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private boolean spaceIsFree() {
        Vertex tempVertex = new Vertex(mouseX, mouseY);
        tempVertex.setBounds(mouseX - 25, mouseY - 25, 50, 50);
        for (Component component : graph.getComponents()) {
            if (component instanceof Vertex existingVertex) {
                if (tempVertex.getBounds().intersects(existingVertex.getBounds())) {
                    return false;
                }
            } else if (component instanceof Edge existingEdge) {
                if (isPointNearEdge(existingEdge, mouseX, mouseY)) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getVertexID() {
        if (spaceIsFree()) {
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
        return "NOT_FREE";
    }

    private void createVertex(String vertexID) {
        if (!vertexID.equals("NOT_FREE")) {
            Vertex vertex = new Vertex("Vertex " + vertexID, "VertexLabel " + vertexID, vertexID, mouseX, mouseY);
            // Set vertex position
            vertex.setBounds(mouseX - 25, mouseY - 25, 50, 50);
            graph.add(vertex);
            graph.repaint();
        }
    }

    private void removeEdge() {
        int numberOfEdgesRemoved = 0;
        for (int i = 0; i < graph.getComponentCount(); i++) {
            Edge selectedEdge = (Edge) getComponent(Edge.class);
            if (selectedEdge != null) {
                selectedEdge.removeLabel(graph);
                graph.remove(selectedEdge);
                numberOfEdgesRemoved++;
            }
            if (numberOfEdgesRemoved == 2) {
                break;
            }
        }
        revalidate();
        repaint();
    }

    private void removeVertex(Vertex vertex) {
        for (Component component : graph.getComponents()) {
            if (component instanceof Edge edge) {
                if (edge.getSource().equals(vertex) || edge.getDestination().equals(vertex)) {
                    edge.removeLabel(graph);
                    graph.remove(edge);
                }
            }
        }
        graph.remove(vertex);
        revalidate();
        repaint();
    }

    private void handleVertexRemoval() {
        Vertex clickedVertex = (Vertex) getComponent(Vertex.class);
        if (clickedVertex != null) {
            removeVertex(clickedVertex);
        }
    }

}