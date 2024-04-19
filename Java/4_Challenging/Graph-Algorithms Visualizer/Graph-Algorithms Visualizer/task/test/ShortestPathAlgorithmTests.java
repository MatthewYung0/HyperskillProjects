import org.assertj.swing.exception.ComponentLookupException;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.common.Utils;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperskill.hstest.testcase.CheckResult.correct;

class Vertex {
    private final String id;
    private final Point point;

    Vertex(String id, Point point) {
        this.id = id;
        this.point = point;
    }

    public static Vertex of(String id) {
        return new Vertex(id, null);
    }

    public Point point() {
        return point;
    }

    public String id() {
        return id;
    }

    public String name() {
        return "Vertex " + id;
    }

    public String labelName() {
        return "VertexLabel " + id;
    }
}

class Edge {
    private final String from;
    private final String to;
    private final int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public static Edge of(String from, String to) {
        return new Edge(from, to, 0);
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    public int weight() {
        return weight;
    }

    public String name() {
        return "Edge <" + from + " -> " + to + ">";
    }

    public String labelName() {
        return "EdgeLabel <" + from + " -> " + to + ">";
    }

    public Edge opposite() {
        return new Edge(this.to, this.from, this.weight);
    }
}

enum ComponentType {
    VERTEX("Vertex "),
    EDGE("Edge "),
    EDGE_LABEL("EdgeLabel "),
    VERTEX_LABEL("VertexLabel "),
    ANY("");

    private final String prefix;

    ComponentType(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }
}

@SuppressWarnings("unused")
public class ShortestPathAlgorithmTests extends SwingTest {

    @SwingComponent(name = "Graph")
    private JPanelFixture graph;

    @SwingComponent(name = "Mode")
    private JLabelFixture modeText;

    @SwingComponent(name = "Add a Vertex")
    private JMenuItemFixture addAVertexMode;

    @SwingComponent(name = "Add an Edge")
    private JMenuItemFixture addAnEdgeMode;

    @SwingComponent(name = "Remove a Vertex")
    private JMenuItemFixture removeAVertexMode;

    @SwingComponent(name = "Remove an Edge")
    private JMenuItemFixture removeAnEdgeMode;

    @SwingComponent(name = "New")
    private JMenuItemFixture newMenuItem;

    @SwingComponent(name = "Exit")
    private JMenuItemFixture exitMenuItem;

    @SwingComponent(name = "None")
    private JMenuItemFixture noneMode;

    @SwingComponent(name = "Breadth-First Search")
    private JMenuItemFixture breadthFirstSearch;

    @SwingComponent(name = "Depth-First Search")
    private JMenuItemFixture depthFirstSearch;

    @SwingComponent(name = "Dijkstra's Algorithm")
    private JMenuItemFixture dijkstraAlgorithm;

    @SwingComponent(name = "Display")
    private JLabelFixture display;

    public ShortestPathAlgorithmTests() {
        super(new MainFrame());
    }

    @DynamicTest(order = 1, feedback = "Initially graph panel should not contain anything.")
    CheckResult isEmptyGraph() {
        assertThat(count(ComponentType.ANY))
                .isEqualTo(0);

        return correct();
    }

    @DynamicTest(order = 2, feedback = "Initial Mode must be \"Add a Vertex\"")
    CheckResult isCorrectCurrentMode() {
        modeText.requireText(Pattern.compile(".*add a vertex.*", Pattern.CASE_INSENSITIVE));
        return correct();
    }

    void changeModeToAddAVertex() {
        addAVertexMode.click();
    }

    void insertVertex(Vertex vertex) {

        getWindow().robot().click(graph.target(), vertex.point());

        JOptionPaneFixture jOptionPaneFixture;
        try {

            jOptionPaneFixture = JOptionPaneFinder
                    .findOptionPane()
                    .withTimeout(200)
                    .using(getWindow().robot());

        } catch (WaitTimedOutError e) {
            throw new WrongAnswer("Timeout waiting for a dialog box to open");
        }

        try {

            jOptionPaneFixture.textBox();

        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A text box was not found inside the dialog box");
        }

        try {

            jOptionPaneFixture.okButton();

        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A ok button was not found inside the dialog box");
        }

        try {

            jOptionPaneFixture.cancelButton();

        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A cancel button was not found inside the dialog box");
        }

        try {

            jOptionPaneFixture.requireTitle(Pattern.compile("vertex", Pattern.CASE_INSENSITIVE));

        } catch (Exception ignore) {
            throw new WrongAnswer("Dialog box title should contain \"Vertex\"");
        }


        try {

            jOptionPaneFixture
                    .textBox()
                    .setText(vertex.id());

            jOptionPaneFixture
                    .textBox()
                    .requireText(vertex.id());
        } catch (Exception e) {
            throw new WrongAnswer("Cannot write text '" + vertex.id() + "' in dialog box / Incorrect text after writing. Expected: '" + vertex.id() + "'");
        }

        JOptionPaneFinder
                .findOptionPane()
                .using(getWindow().robot())
                .okButton()
                .click();

        try {

            JOptionPaneFinder
                    .findOptionPane()
                    .withTimeout(200)
                    .using(getWindow().robot());

            throw new WrongAnswer("Dialog box did not disappear after clicking ok");
        } catch (WaitTimedOutError ignored) {
        }

        Optional<Component> vOp = getVertex(vertex.id());

        if (vOp.isEmpty()) {
            throw new WrongAnswer(vertex.name() + " was not created. It is not present in graph panel");
        }

        if (!(vOp.get() instanceof JPanel)) {
            throw new WrongAnswer("Each Vertex should be represented by a JPanel.");
        }

        JPanel v = (JPanel) vOp.get();

        if (v.getSize().getHeight() != 50 || v.getSize().getWidth() != 50) {
            Dimension expected = new Dimension(50, 50);
            Dimension got = v.getSize();
            throw new WrongAnswer("Incorrect Vertex Size. Expected: " + expected + ", Got: " + got);
        }

        Optional<Component> vl = getVertexLabel(vertex.id());

        if (vl.isEmpty()) {
            throw new WrongAnswer("Label of " + vertex.name() + " is not Present.");
        }

        if (!(vl.get() instanceof JLabel)) {
            throw new WrongAnswer("Each Vertex label must be represented by JLabel.");
        }

        JLabel vLabel = (JLabel) vl.get();

        if (!Objects.equals(vLabel.getParent().getName(), vertex.name())) {
            throw new WrongAnswer("Each Label of each vertex must be present inside it.");
        }

        if (!Objects.equals(vLabel.getText(), vertex.id())) {
            throw new WrongAnswer("Vertex Label Should Contain ID of Vertex");
        }

        Point got = v.getLocation();
        Point expected = new Point((int) (vertex.point().getX() - 25), (int) (vertex.point().getY() - 25));
        if (!expected.equals(got)) {
            throw new WrongAnswer("Incorrect Position. Expected: " + expected + ", Got: " + got);
        }

    }

    void changeModeToAddAnEdge() {
        addAnEdgeMode.click();
    }

    void insertEdge(Edge edge) {


        getWindow().robot().click(getVertex(edge.from()).orElseThrow());
        getWindow().robot().click(getVertex(edge.to()).orElseThrow());

        JOptionPaneFixture jOptionPaneFixture;

        try {
            jOptionPaneFixture = JOptionPaneFinder
                    .findOptionPane()
                    .withTimeout(1000)
                    .using(getWindow().robot());
        } catch (WaitTimedOutError e) {
            throw new WrongAnswer("Clicking on two vertices in the \"Add an Edge\" mode; a new window must pop up prompting for edge weight");
        }

        try {
            jOptionPaneFixture.okButton();
        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A ok button was not found in edge weight prompting input box.");
        }

        try {
            jOptionPaneFixture.cancelButton();
        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A cancel button was not found in edge weight prompting input box.");
        }

        try {
            jOptionPaneFixture.textBox();
        } catch (ComponentLookupException e) {
            throw new WrongAnswer("A text box was not found in edge weight prompting input box.");
        }

        try {
            jOptionPaneFixture
                    .textBox()
                    .setText(edge.weight() + "");

            jOptionPaneFixture
                    .textBox()
                    .requireText(edge.weight() + "");
        } catch (Exception e) {
            throw new WrongAnswer("Could not enter text in the edge weight prompting dialog box.");
        }

        jOptionPaneFixture
                .okButton()
                .click();

        try {
            JOptionPaneFinder
                    .findOptionPane()
                    .withTimeout(1000)
                    .using(getWindow().robot());

            throw new WrongAnswer("Dialog box must close after clicking on ok button.");
        } catch (WaitTimedOutError ignored) {
        }

        Component aTob = getEdge(edge.from(), edge.to()).orElseThrow(() -> new WrongAnswer("\"" + edge.labelName() + "\" was not found!"));

        Component bToa = getEdge(edge.to(), edge.from()).orElseThrow(() -> new WrongAnswer("\"" + edge.opposite().labelName() + "\" was not found!"));

        Component label = getEdgeLabel(edge.from(), edge.to()).orElseThrow(() -> new WrongAnswer("Edge Label which should contain the edge weight was not found in graph."));

        if (!(aTob instanceof JComponent) || !(bToa instanceof JComponent))
            throw new WrongAnswer("Edge must be represented by JComponent");

        if (!(label instanceof JLabel))
            throw new WrongAnswer("EdgeLabel must be represented by JLabel");

        if (!Objects.equals(graph.target(), label.getParent()))
            throw new WrongAnswer("Parent of each edge label is the graph itself not the edge.");


        try {
            assertThat(((JLabel) label).getText())
                    .containsIgnoringCase(edge.weight() + "");
        } catch (Exception e) {
            throw new WrongAnswer("Edge Label should hold the weight of the respective edge");
        }


    }

    void checkVertexCount(Vertex[] vertices) {
        int got = count(ComponentType.VERTEX);
        int expected = vertices.length;
        if (expected != got)
            throw new WrongAnswer("Incorrect Vertex Count. Expected = " + expected + ", Got = " + got);

    }

    void checkEdgeCount(Edge[] edges) {
        int got = count(ComponentType.EDGE);
        int expected = edges.length * 2;
        if (expected != got)
            throw new WrongAnswer("Incorrect Edge Count. Expected = " + expected + ", Got = " + got);

    }

    void checkVertexLabelCount(Vertex[] vertices) {
        int got = count(ComponentType.VERTEX_LABEL);
        int expected = vertices.length;
        if (expected != got)
            throw new WrongAnswer("Incorrect Vertex Label (id) Count. Expected = " + expected + ", Got = " + got);
    }

    void checkEdgeLabelCount(Edge[] edges) {
        int got = count(ComponentType.EDGE_LABEL);
        int expected = edges.length;
        if (expected != got)
            throw new WrongAnswer("Incorrect Edge Label Count. Expected = " + expected + ", Got = " + got);
    }

    GraphData[] graphs = new GraphData[]{new GraphDataOne(), new GraphDataTwo()};

    @DynamicTest(order = 3, data = "graphs", feedback = "Error while running / trying to run graph traversal algorithms")
    CheckResult checkGraphTraversal(GraphData data) {

        Vertex[] vertices = data.getVertices().toArray(new Vertex[0]);
        Edge[] edges = data.getEdges().toArray(new Edge[0]);

        this.changeModeToAddAVertex();

        for (Vertex v : vertices) {
            insertVertex(v);
        }

        this.changeModeToAddAnEdge();

        for (Edge e : edges) {
            insertEdge(e);
        }

        checkVertexCount(vertices);
        checkEdgeCount(edges);
        checkVertexLabelCount(vertices);
        checkEdgeLabelCount(edges);

        runAlgorithm(data, dijkstraAlgorithm, data.getDijkstraText());

        newMenuItem.click();

        return correct();
    }

    private void runAlgorithm(GraphData data, JMenuItemFixture algo, String answer) {
        algo.click();

        if (!display.text().contains("Please choose a starting vertex"))
            throw new WrongAnswer("Display label should show \"Please choose a starting vertex\" if an algorithm is selected for execution.");

        getWindow().robot().click(getVertex(data.getSource()).orElseThrow(() -> new WrongAnswer("Vertex disappeared after creation.")));

        if (!modeText.text().matches(".*None.*"))
            throw new WrongAnswer("Mode after selecting an algorithm should be \"None\".");

        if (!display.text().contains("Please wait..."))
            throw new WrongAnswer("Display label should show \"Please wait...\" after selecting source / while the algorithm is executing.");

        int totalTime = 0;
        while (display.text().contains("Please wait...")) {
            if (totalTime > 60000) {
                throw new WrongAnswer("Algorithm Running for more than 1 min");
            }
            Utils.sleep(100);
            totalTime += 100;
        }

        if (!display.text().contains(answer))
            throw new WrongAnswer("Wrong Answer in " + algo.target().getName() + "." + " Expected: " + answer + ", Got: " + display.text());
    }

    private int count(ComponentType type) {
        return (int) getAllComponents(graph.target())
                .stream()
                .filter(it -> it.getName() != null && it.getName().startsWith(type.prefix()))
                .count();
    }

    private Optional<Component> getVertexLabel(String id) {
        String name = Vertex.of(id).labelName();

        return getAllComponents(graph.target())
                .stream()
                .filter(it -> Objects.equals(it.getName(), name))
                .findFirst();

    }

    private Optional<Component> getEdgeLabel(String from, String to) {
        String name = Edge.of(from, to).labelName();

        return getAllComponents(graph.target())
                .stream()
                .filter(it -> Objects.equals(it.getName(), name))
                .findFirst();

    }

    private Optional<Component> getEdge(String from, String to) {
        String name = Edge.of(from, to).name();

        return getAllComponents(graph.target())
                .stream()
                .filter(it -> Objects.equals(it.getName(), name))
                .findFirst();
    }

    private Optional<Component> getVertex(String id) {
        String name = Vertex.of(id).name();

        return getAllComponents(graph.target())
                .stream()
                .filter(it -> Objects.equals(name, it.getName()))
                .findFirst();
    }
}
