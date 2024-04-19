import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class _4_RemoveVertexEdgeTests extends SwingTest {

  @SwingComponent
  private JPanelFixture graph;

  @SwingComponent(name = "Mode")
  JLabelFixture mode;

  @SwingComponent(name = "Add a Vertex")
  JMenuItemFixture addVertexMenuItem;

  @SwingComponent(name = "Add an Edge")
  JMenuItemFixture addEdgeMenuItem;

  @SwingComponent(name = "Remove a Vertex")
  JMenuItemFixture removeVertexMenuItem;

  @SwingComponent(name = "Remove an Edge")
  JMenuItemFixture removeEdgeMenuItem;

  private final List<Vertex> vertices = new ArrayList<>(List.of(
      new Vertex(386, 221, "0"),
      new Vertex(211, 216, "1"),
      new Vertex(245, 92, "2"),
      new Vertex(372, 28, "3"),
      new Vertex(512, 81, "4"),
      new Vertex(543, 215, "5"),
      new Vertex(499, 345, "6"),
      new Vertex(372, 407, "7"),
      new Vertex(233, 349, "8")
  ));

  private final List<Edge> edges = new ArrayList<>(List.of(
      new Edge("0", "1", 1),
      new Edge("0", "2", 1),
      new Edge("0", "3", 2),
      new Edge("0", "4", 3),
      new Edge("0", "5", 4),
      new Edge("0", "6", 5),
      new Edge("0", "7", 6),
      new Edge("0", "8", 7),
      new Edge("1", "2", 3),
      new Edge("2", "3", 4),
      new Edge("3", "4", 5),
      new Edge("4", "5", 6),
      new Edge("5", "6", 7),
      new Edge("6", "7", 3),
      new Edge("7", "8", 4),
      new Edge("8", "1", 5)
  ));

  public _4_RemoveVertexEdgeTests() {
    super(new MainFrame());
  }

  @DynamicTest(order = 17, feedback = "Could not insert vertices")
  CheckResult insertVertices() {
    addVertexMenuItem.click();
    for (var v : vertices) {
      _2_VertexTests.assertSuccessfulVertexInsertion(graph, v.getX(), v.getY(), v.getId(), getWindow().robot());
    }
    return CheckResult.correct();
  }

  @DynamicTest(order = 18, feedback = "Could not insert edges")
  CheckResult insertEdges() {
    addEdgeMenuItem.click();
    TestingUtils.addEdge(edges, graph, getWindow().robot());
    return CheckResult.correct();
  }

  @DynamicTest(order = 19, feedback = "Could not Remove an Edge <8 -> 1> or Edge <4 -> 5>")
  CheckResult removeTwoEdges() {
    removeEdgeMenuItem.click();
    var edge_8_1 = getEdge("8", "1").getMidPoint(getVertex("8").asPoint(), getVertex("1").asPoint());
    var edge_5_6 = getEdge("4", "5").getMidPoint(getVertex("4").asPoint(), getVertex("5").asPoint());
    getWindow().robot().click(graph.target().getComponentAt(edge_8_1));
    getWindow().robot().click(graph.target().getComponentAt(edge_5_6));
    return CheckResult.correct();
  }

  @DynamicTest(order = 20, feedback = "Edge still exists in the graph (JPanel) after removing them.")
  CheckResult checkRemoveTwoEdges() {
    var msg = " still exists in the graph after removing.";
    var edgeNameFormat = "Edge <%d -> %d>";
    var edge_8_1 = String.format(edgeNameFormat, 8, 1);
    var edge_1_8 = String.format(edgeNameFormat, 1, 8);
    var edge_4_5 = String.format(edgeNameFormat, 4, 5);
    var edge_5_4 = String.format(edgeNameFormat, 5, 4);

    for (var c : getAllComponents(graph.target())) {
      if (Objects.equals(edge_8_1, c.getName()) || Objects.equals(edge_1_8, c.getName()))
        throw new WrongAnswer(edge_8_1 + " or " + edge_1_8 + msg);

      if (Objects.equals(edge_4_5, c.getName()) || Objects.equals(edge_5_4, c.getName()))
        throw new WrongAnswer(edge_4_5 + " or " + edge_5_4 + msg);
    }

    edges.removeIf(e -> (e.getFrom().equals("8") && e.getTo().equals("1")) ||
        (e.getFrom().equals("4") && e.getTo().equals("5")));

    return CheckResult.correct();
  }

  @DynamicTest(order = 21, feedback = "Wrong number of components in Graph")
  CheckResult validateNumberOfComponents() {
    return TestingUtils.assertComponentCount(graph,
        vertices.size() + 3 * edges.size());
  }

  @DynamicTest(order = 22, feedback = "Could not delete vertices")
  CheckResult deleteTwoVertices() {
    removeVertexMenuItem.click();
    var vertex_0 = getVertex("0").asPoint();
    var vertex_7 = getVertex("7").asPoint();
    getWindow().robot().click(graph.target().getComponentAt(vertex_0));
    getWindow().robot().click(graph.target().getComponentAt(vertex_7));
    return CheckResult.correct();
  }

  @DynamicTest(order = 23, feedback = "All of the in and out edges of the vertex must be deleted along with the vertices")
  CheckResult checkDeleteResult() {
    var feedbackText = "Already deleted Vertex %s is present in the graph.";
    for (var c : getAllComponents(graph.target())) {

      if (Objects.equals("Vertex " + 0, c.getName())) {
        throw new WrongAnswer(String.format(feedbackText, 0));
      } else if (Objects.equals("Vertex " + 7, c.getName())) {
        throw new WrongAnswer(String.format(feedbackText, 7));
      } else if (c.getName().matches("Edge <0 -> .>") ||
          c.getName().matches("Edge <. -> 0>") ||
          c.getName().matches("Edge <7 -> .>") ||
          c.getName().matches("Edge <. -> 7>")) {
        throw new WrongAnswer("In or Out edges of the deleted vertex shall be removed too.");
      }
    }

    vertices.removeIf(v -> v.getId().equals("7") || v.getId().equals("0"));

    edges.removeIf(e -> e.getFrom().equals("0") ||
        e.getTo().equals("0") ||
        e.getFrom().equals("7") ||
        e.getTo().equals("7"));

    return CheckResult.correct();
  }

  @DynamicTest(order = 24, feedback = "Incorrect number of components in graph after deleting edges and vertices")
  CheckResult componentCheckAfterRemove() {
    return TestingUtils.assertComponentCount(graph, vertices.size() + 3 * edges.size());
  }

  @DynamicTest(order = 25, feedback = "Error removing vertex and edges")
  CheckResult deleteAllComponents() {
    var robot = getWindow().robot();
    for (var v : vertices) {
      robot.click(graph.target().getComponentAt(v.asPoint()));
    }

    for (var e : edges) {
      robot.click(graph.target()
          .getComponentAt(
              e.getMidPoint(
                  getVertex(e.getFrom())
                      .asPoint(),
                  getVertex(e.getTo()).asPoint())
          )
      );
    }

    return CheckResult.correct();
  }

  @DynamicTest(order = 26, feedback = "After deleting all component, JPanel Graph is not empty")
  CheckResult checkEmpty() {
    return TestingUtils.assertComponentCount(graph, 0);
  }

  private Vertex getVertex(String id) {
    return vertices
        .stream()
        .filter(v -> v.getId().equals(id))
        .findFirst()
        .orElseThrow();
  }

  private Edge getEdge(String from, String to) {
    return edges
        .stream()
        .filter(e -> e.getFrom().equals(from) && e.getTo().equals(to))
        .findFirst()
        .orElseThrow();
  }
}
