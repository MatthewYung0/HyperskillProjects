import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperskill.hstest.testcase.CheckResult.correct;

@SuppressWarnings("unused")
public class DrawingCirclesTests extends SwingTest {

    @SwingComponent(name = "Graph")
    private JPanelFixture graph;

    @SwingComponent(name = "Vertex 0")
    private JPanelFixture vertex0;

    @SwingComponent(name = "VertexLabel 0")
    private JLabelFixture vertexLabel0;

    @SwingComponent(name = "Vertex 1")
    private JPanelFixture vertex1;

    @SwingComponent(name = "VertexLabel 1")
    private JLabelFixture vertexLabel1;

    @SwingComponent(name = "Vertex 2")
    private JPanelFixture vertex2;

    @SwingComponent(name = "VertexLabel 2")
    private JLabelFixture vertexLabel2;

    @SwingComponent(name = "Vertex 3")
    private JPanelFixture vertex3;

    @SwingComponent(name = "VertexLabel 3")
    private JLabelFixture vertexLabel3;

    private final Map<JPanelFixture, Point> positionMap = new HashMap<>();
    private final Map<String, JPanelFixture> vertexNameMap = new HashMap<>();
    private final Map<String, JLabelFixture> labelNameMap = new HashMap<>();

    private final String[] vertexFixtures = new String[]{"Vertex 0", "Vertex 1", "Vertex 2", "Vertex 3"};
    private final String[] labelFixtures = new String[]{"VertexLabel 0", "VertexLabel 1", "VertexLabel 2", "VertexLabel 3"};

    public DrawingCirclesTests() {
        super(new MainFrame());
    }

    @DynamicTest(feedback = "Cannot initialize test")
    CheckResult itShouldInitializeSwingComponents() {
        for (JPanelFixture f : new JPanelFixture[]{vertex0, vertex1, vertex2, vertex3}) {
            vertexNameMap.put(f.target().getName(), f);
        }

        for (JLabelFixture f : new JLabelFixture[]{vertexLabel0, vertexLabel1, vertexLabel2, vertexLabel3}) {
            labelNameMap.put(f.target().getName(), f);
        }

        Dimension size = graph.target().getSize();

        int expectedX = (int) size.getWidth() - 50;
        int expectedY = (int) size.getHeight() - 50;

        positionMap.put(vertex0, new Point(0, 0));
        positionMap.put(vertex1, new Point(expectedX, 0));
        positionMap.put(vertex2, new Point(0, expectedY));
        positionMap.put(vertex3, new Point(expectedX, expectedY));

        return correct();
    }

    @DynamicTest(order = 1, feedback = "Title should be \"Graph-Algorithms Visualizer\"")
    CheckResult itShouldTestForCorrectFrameTitle() {

        assertThat(frame.getTitle())
                .containsIgnoringCase("Graph-Algorithms Visualizer");

        return correct();
    }

    @DynamicTest(order = 1, feedback = "Default Close Operation should be Exit on Close")
    CheckResult itShouldTestForDefaultCloseOperation() {
        assertThat(frame.getDefaultCloseOperation())
            .isEqualTo(JFrame.EXIT_ON_CLOSE);
        return correct();
    }

    @DynamicTest(order = 2, feedback = "Size of Frame Should be - (800 x 600)")
    CheckResult itShouldTestForCorrectFrameDimension() {

        Dimension size = frame.getSize();

        assertThat(size.getWidth())
                .isEqualTo(800);
        assertThat(size.getHeight())
                .isEqualTo(600);

        return correct();
    }

    @DynamicTest(order = 2, feedback = "Invalid Size of Graph")
    CheckResult itShouldTestForCorrectGraphSize() {
        assertThat(graph.target().getHeight())
            .isNotEqualTo(0);
        assertThat(graph.target().getWidth())
            .isNotEqualTo(0);
        return correct();
    }

    @DynamicTest(order = 2, feedback = "Graph is not visible")
    CheckResult itShouldTestForVisibleGraph() {
        graph.requireVisible();
        return correct();
    }

    @DynamicTest(order = 3,
            feedback = "Size of Every Vertex should be 50",
            data = "vertexFixtures")
    CheckResult itShouldCheckForCorrectDimensionOfVertex(String name) {
        JPanelFixture fixture = vertexNameMap.get(name);

        Dimension got = fixture.target().getSize();
        Dimension expected = new Dimension(50, 50);

        if (got.getHeight() != expected.getHeight() || got.getWidth() != expected.getWidth())
            throw new WrongAnswer("Incorrect Dimension of " + name + ". Expected : " + expected + ", Got : " + got);

        return correct();
    }

    @DynamicTest(order = 4,
            feedback = "Incorrect Position of Vertex",
            data = "vertexFixtures")
    CheckResult itShouldTestForCorrectPositionOfVertex(String name) {

        JPanelFixture fixture = vertexNameMap.get(name);

        Point expected = positionMap.get(fixture);
        Point got = fixture.target().getLocation();

        if (expected.getX() != got.getX() || expected.getY() != got.getY())
            throw new WrongAnswer("Incorrect Position of " + name + ". Expected : " + expected + ", Got : " + got);

        return correct();
    }

    @DynamicTest(order = 5,
            feedback = "Incorrect Parent of VertexLabel",
            data = "labelFixtures")
    CheckResult itShouldTestForCorrectParentOfVertexLabel(String name) {

        Container gotParent = labelNameMap.get(name).target().getParent();
        JPanel expectedParent = vertexNameMap.get(name.replace("Label", "")).target();

        if (gotParent != expectedParent) {
            throw new WrongAnswer("Incorrect Parent of " + name + ". Expected : " + expectedParent + ", Got : " + gotParent);
        }

        return correct();
    }

    @DynamicTest(order = 6,
            feedback = "Incorrect Text in VertexLabel",
            data = "labelFixtures")
    CheckResult itShouldTestForCorrectTextInVertexLabel(String name) {

        labelNameMap.get(name).requireText(name.replace("VertexLabel ", ""));

        return correct();
    }

    @DynamicTest(order = 7,
            feedback = "Total Number of Components in the Graph JPanel Should be 8")
    CheckResult itShouldTestForCorrectNumberOfComponents() {
        int got = getAllComponents(graph.target()).size();
        int expected = 8;

        if (got != expected) {
            throw new WrongAnswer("Incorrect Number of Components in the Graph JPanel. Expected = " + expected + ", Got = " + got);
        }

        return correct();
    }
}
