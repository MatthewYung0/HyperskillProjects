import org.assertj.swing.exception.WaitTimedOutError;
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

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

@SuppressWarnings("unused")
@DynamicTest()
public class _5_GraphAlgorithmTests extends SwingTest {

  @SwingComponent
  private JPanelFixture graph;

  @SwingComponent(name = "Mode")
  private JLabelFixture mode;

  @SwingComponent
  private JLabelFixture display;

  @SwingComponent(name = "New")
  private JMenuItemFixture newMenuItem;

  @SwingComponent(name = "Exit")
  private JMenuItemFixture exitMenuItem;

  @SwingComponent(name = "Add a Vertex")
  private JMenuItemFixture addVertexMenuItem;

  @SwingComponent(name = "Add an Edge")
  private JMenuItemFixture addEdgeMenuItem;

  @SwingComponent(name = "Remove a Vertex")
  private JMenuItemFixture removeVertexMenuItem;

  @SwingComponent(name = "Remove an Edge")
  private JMenuItemFixture removeEdgeMenuItem;

  @SwingComponent(name = "None")
  private JMenuItemFixture noneMenuItem;

  @SwingComponent(name = "Depth-First Search")
  private JMenuItemFixture dfs;

  @SwingComponent(name = "Breadth-First Search")
  private JMenuItemFixture bfs;

  @SwingComponent(name = "Dijkstra's Algorithm")
  private JMenuItemFixture dijkstra;

  @SwingComponent(name = "Prim's Algorithm")
  private JMenuItemFixture prim;

  public _5_GraphAlgorithmTests() {
    super(new MainFrame());
  }

  private CheckResult getAddVertexCheckResult(java.util.List<Vertex> vertices) {
    var robot = getWindow().robot();
    for (var v : vertices) {
      robot.click(graph.target(), v.asPoint());

      JOptionPaneFixture dialog;
      try {
        dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());
      } catch (WaitTimedOutError e) {
        throw new WrongAnswer(e.getMessage());
      }
      dialog.textBox().setText(v.getId());
      dialog.okButton().click();
    }

    var components = graph.target().getComponents().length;
    var noOfVertices = vertices.size();
    if (components != noOfVertices)
      return wrong(String.format("%d vertices was expected to be present, but found %d", noOfVertices, components));
    return correct();
  }


  private CheckResult getAddEdgesCheckResult(java.util.List<Edge> edges) {
    var robot = getWindow().robot();
    return TestingUtils.addEdge(edges, graph, robot);
  }

  private void runAlgorithm(String source) {
    if (!Objects.equals("Please choose a starting vertex", display.text()))
      throw new WrongAnswer("After clicking on any algorithm, display JLabel must show - " +
          "\"Please choose a starting vertex\"");
    new JPanelFixture(getWindow().robot(), (JPanel) TestingUtils.getVertex(source, graph)).click();
    display.requireText("Please wait...");
  }

  private CheckResult getAlgorithmCheckResult(String feedback, String expected) {
    if (!Objects.equals(display.text(), expected))
      throw new WrongAnswer(feedback +
          " Expected : \"" + expected + "\", but got : \"" + display.text() + "\"");

    return correct();
  }

  private void waitForCompletion() {
    int totalTime = 0;
    while (Objects.equals(display.text(), "Please wait...")) {
      if (totalTime > 60000) {
        throw new WrongAnswer("Algorithm Running for more than 1 min");
      }
      Utils.sleep(100);
      totalTime += 100;
    }
  }

  @DynamicTest(order = 27, feedback = "Initial Mode must be \"Add a Vertex\"")
  CheckResult checkDefaultMode() {
    newMenuItem.click();
    if (mode.text()== null || !mode.text().contains("Add a Vertex"))
      return wrong("Expected Mode \"Add a Vertex\", Got : " + mode.text());
    return correct();
  }

  @DynamicTest(order = 28, feedback = "Vertices could not be inserted")
  CheckResult insertVertices() {
    var vertices = GraphDataOne.getVertices();
    return getAddVertexCheckResult(vertices);
  }

  @DynamicTest(order = 29, feedback = "Edges could not be inserted")
  CheckResult insertEdges() {
    addEdgeMenuItem.click();
    var edges = GraphDataOne.getEdges();
    return getAddEdgesCheckResult(edges);
  }

  @DynamicTest(order = 30, feedback = "Failed to run DFS Algorithm")
  CheckResult depthFirstSearchTraversal() {
    dfs.click();
    runAlgorithm(GraphDataOne.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in DFS Traversal.",
        GraphDataOne.getDFSText());
  }

  @DynamicTest(order = 31, feedback = "Failed to run BFS Algorithm")
  CheckResult breadthFirstSearchTraversal() {
    bfs.click();
    runAlgorithm(GraphDataOne.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in BFS Traversal.",
        GraphDataOne.getBFSText());
  }

  @DynamicTest(order = 32, feedback = "Failed to run Dijkstra's Algorithm")
  CheckResult dijkstraShortestPathAlgorithm() {
    dijkstra.click();
    runAlgorithm(GraphDataOne.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in Dijkstra's Algorithm.",
        GraphDataOne.getDijkstraText());
  }

  @DynamicTest(order = 33, feedback = "Failed to run Prim's Algorithm")
  CheckResult primsMinimumSpanningTree() {
    prim.click();
    runAlgorithm(GraphDataOne.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in Prim's Algorithm.",
        GraphDataOne.getPrimText());
  }

  @DynamicTest(order = 34, feedback = "Vertices could not be inserted")
  CheckResult insertVertices2() {
    try {
      newMenuItem.click();
      var vertices = GraphDataTwo.getVertices();
      return getAddVertexCheckResult(vertices);
    }catch(IllegalComponentStateException ex){
      return wrong("Vertices could not be inserted");
    }
  }

  @DynamicTest(order = 35, feedback = "Edges could not be inserted")
  CheckResult insertEdges2() {
    addEdgeMenuItem.click();
    var edges = GraphDataTwo.getEdges();
    return getAddEdgesCheckResult(edges);
  }

  @DynamicTest(order = 36, feedback = "Failed to run DFS Algorithm")
  CheckResult depthFirstSearchTraversal2() {
    dfs.click();
    runAlgorithm(GraphDataTwo.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in DFS Traversal.",
        GraphDataTwo.getDFSText());
  }

  @DynamicTest(order = 37, feedback = "Failed to run BFS Algorithm")
  CheckResult breadthFirstSearchTraversal2() {
    bfs.click();
    runAlgorithm(GraphDataTwo.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in BFS Traversal.",
        GraphDataTwo.getBFSText());
  }

  @DynamicTest(order = 38, feedback = "Failed to run Dijkstra's Algorithm")
  CheckResult dijkstraShortestPathAlgorithm2() {
    dijkstra.click();
    runAlgorithm(GraphDataTwo.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in Dijkstra's Algorithm.",
        GraphDataTwo.getDijkstraText());
  }

  @DynamicTest(order = 39, feedback = "Failed to run Prim's Algorithm")
  CheckResult primsMinimumSpanningTree2() {
    prim.click();
    runAlgorithm(GraphDataTwo.getSource());

    waitForCompletion();

    return getAlgorithmCheckResult("Wrong Answer in Prim's Algorithm.",
        GraphDataTwo.getPrimText());
  }
}
