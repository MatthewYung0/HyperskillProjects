package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class MainFrame extends JFrame{

    private int mouseX = 0;
    private int mouseY = 0;
    private JPanel graph;
    private MODE mode = MODE.NONE;
    private MouseAdapter mouseAdapter;
    private final String currentOptionString = "Current Mode -> ";
    private final String addAVertexString = "Add a Vertex";
    private final String addAnEdgeString = "Add an Edge";
    private final String addNoneString = "None";
    private JLabel currentMode;
    private Vertex selectedVertex1 = null;
    private Vertex selectedVertex2 = null;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graph-Algorithms Visualizer");
        setSize(800, 600);
        setLayout(null);
        initGraph();
        initMenu();
        initMouseAdapter();
        initCurrentMode();
        setVisible(true);
    }

    private void initCurrentMode() {
        currentMode = new JLabel(currentOptionString + addAVertexString);
        currentMode.setName("Mode");
        currentMode.setForeground(Color.BLACK);
        currentMode.setBounds(10,550,300,50);
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
                }
            }
        };
        graph.addMouseListener(mouseAdapter);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu modeMenu = new JMenu("Mode");
        menuBar.add(modeMenu);
        initModeMenuItems(modeMenu);
    }

    private void initModeMenuItems(JMenu modeMenu) {
        // Creating JMenu items
        JMenuItem addVertexMenuItem = new JMenuItem(addAVertexString);
        JMenuItem addEdgeMenuItem = new JMenuItem(addAnEdgeString);
        JMenuItem addNoneMenuItem = new JMenuItem(addNoneString);
        addVertexMenuItem.setName(addAVertexString);
        addEdgeMenuItem.setName(addAnEdgeString);
        addNoneMenuItem.setName(addNoneString);
        // Adding them to the Menu
        modeMenu.add(addVertexMenuItem);
        modeMenu.add(addEdgeMenuItem);
        modeMenu.add(addNoneMenuItem);
        // Changes the mode enum to the users selection
        addVertexMenuItem.addActionListener(event -> {
            mode = MODE.ADD_VERTEX;
            setCurrentOption(MODE.ADD_VERTEX);
        });
        addEdgeMenuItem.addActionListener(event -> {
            mode = MODE.ADD_EDGE;
            setCurrentOption(MODE.ADD_EDGE);
        });
        addNoneMenuItem.addActionListener(event -> {
            mode = MODE.NONE;
            setCurrentOption(MODE.NONE);
        });
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

    private void setCurrentOption(MODE mode) {
        switch(mode) {
            case ADD_VERTEX -> currentMode.setText(currentOptionString + addAVertexString);
            case ADD_EDGE -> currentMode.setText(currentOptionString + addAnEdgeString);
            case NONE -> currentMode.setText(currentOptionString + addNoneString);
        }
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

    private void handleEdgeCreation() {
        Vertex clickedVertex = getClickedVertex();
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

    private void createEdge() {
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

    private Vertex getClickedVertex() {
        for (Component component : graph.getComponents()) {
            if (component instanceof Vertex) {
                if (component.getBounds().contains(this.mouseX, this.mouseY)) {
                    return (Vertex) component;
                }
            }
        }
        return null;
    }

    private boolean spaceIsFree() {
        Vertex tempVertex = new Vertex(mouseX, mouseY);
        tempVertex.setBounds(mouseX-25, mouseY-25, 50, 50);
        for (Component component : graph.getComponents()) {
            if (component instanceof Vertex existingVertex) {
                if (tempVertex.getBounds().intersects(existingVertex.getBounds())) {
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
            vertex.setBounds(mouseX-25, mouseY-25, 50, 50);
            graph.add(vertex);
            graph.repaint();
        }
    }
}