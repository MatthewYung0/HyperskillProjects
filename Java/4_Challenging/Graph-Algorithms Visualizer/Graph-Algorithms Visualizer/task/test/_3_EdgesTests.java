import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.TestPassed;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

@SuppressWarnings("unused")
public class _3_EdgesTests extends SwingTest {

  @SwingComponent
  JPanelFixture graph;

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

  @SwingComponent(name = "None")
  JMenuItemFixture noneMenuItem;

  public _3_EdgesTests() {
    super(new MainFrame());
  }

  public static List<Vertex> getEdgeTestingVertexPositions() {
    return List.of(
        new Vertex(175, 359, "A"),
        new Vertex(224, 176, "B"),
        new Vertex(365, 363, "C"),
        new Vertex(408, 67, "D"),
        new Vertex(531, 235, "E"),
        new Vertex(590, 64, "F")
    );
  }

  @DynamicTest(feedback = "Could not insert vertices")
  CheckResult addVerticesForTestingEdges() {
    addVertexMenuItem.click();

    for (var v : getEdgeTestingVertexPositions()) {
      _2_VertexTests.assertSuccessfulVertexInsertion(graph, v.getX(), v.getY(), v.getId(), getWindow().robot());
    }
    return correct();
  }

  public static Map<String, Vertex> getVertexEdgeTestingPositionsAsMap() {
    Map<String, Vertex> map = new HashMap<>();
    for (var item : getEdgeTestingVertexPositions()) {
      map.put(item.getId(), item);
    }
    return map;
  }

  public static List<Edge> getEdgeTestingPositions() {
    return List.of(
        new Edge("A", "B", 1),
        new Edge("B", "D", 2),
        new Edge("D", "F", 3),
        new Edge("F", "E", 4),
        new Edge("E", "C", 5),
        new Edge("C", "A", 6),
        new Edge("B", "E", 7),
        new Edge("D", "C", 8),
        new Edge("B", "C", 9),
        new Edge("D", "E", 10)
    );
  }

  @DynamicTest(order = 14, feedback = "Error while adding edges")
  CheckResult test15() {
    var map = getVertexEdgeTestingPositionsAsMap();
    var target = graph.target();

    addEdgeMenuItem.click();

    for (var e : getEdgeTestingPositions()) {

      var v = e.getFrom();
      var u = e.getTo();

      var fixtureV = getVertexPanelFixture(map, target, v);
      var fixtureU = getVertexPanelFixture(map, target, u);
      fixtureV.click();
      fixtureU.click();

      var dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());
      dialog.textBox().setText(e.getWeight() + "");
      dialog.okButton().click();

      checkEdge(v, u, true);
      checkEdge(u, v, true);
      checkEdgeLabel(v, u, e.getWeight(), true);
    }

    return correct();
  }

  @DynamicTest(order = 15)
  CheckResult componentCountCheckupAfterAddingEdges() {
    return TestingUtils.assertComponentCount(
        graph,
        getEdgeTestingVertexPositions().size() +
            3 * getEdgeTestingPositions().size()
    );
  }

  @DynamicTest(order = 16, feedback = "Inclusion of Edges Error")
  CheckResult checkInclusionOfEdgesInIncorrectModes() {
    Map<String, Vertex> map = getVertexEdgeTestingPositionsAsMap();
    var target = graph.target();

    try {

      removeVertexMenuItem.click();
      tryToIncludeTwoArbitraryEdges(map, target);

      noneMenuItem.click();
      tryToIncludeTwoArbitraryEdges(map, target);

      removeEdgeMenuItem.click();
      tryToIncludeTwoArbitraryEdges(map, target);
    } catch (WaitTimedOutError e) {
      throw new TestPassed();
    }

    return wrong("Edges can only be added in \"Add an Edge\" mode. Found Mode : " + mode.text());
  }

  private JPanelFixture getVertexPanelFixture(Map<String, Vertex> map, JPanel target, String v) {
    return new JPanelFixture(getWindow().robot(), (JPanel) target.getComponentAt(map.get(v).asPoint()));
  }

  private void checkEdgeLabel(String v, String u, int weight, boolean value) {
    boolean foundEdgeLabel = false;
    for (var e : getAllComponents(graph.target())) {
      if (e.getName() == null)
        throw new WrongAnswer("Every component in the application must have a name. Component : " + e);
      if (e.getName().equals(String.format("EdgeLabel <%s -> %s>", v, u))) {
        foundEdgeLabel = true;
        var text = ((JLabel) e).getText();
        if (!text.equals(weight + ""))
          throw new WrongAnswer(String.format("EdgeLabel <%s -> %s> was expected " +
              "to have weight %s but found %s", v, u, weight, text));
      }
    }

    String edgeMessage;

    if (value) {
      edgeMessage = "EdgeLabel (JLabel) with name \"EdgeLabel <%s -> %s>\" " +
          "must be placed in the graph panel after successful edge insertion";
    } else {
      edgeMessage = "EdgeLabel can be inserted in \"Add an Edge\" mode only.";
    }

    if (foundEdgeLabel != value)
      throw new WrongAnswer(String.format(edgeMessage, v, u));
  }

  private void checkEdge(String v, String u, boolean value) {
    boolean foundEdge = false;
    for (var e : getAllComponents(graph.target())) {
      if (e.getName() == null)
        throw new WrongAnswer("Every component in the application must have a name. Component : " + e);
      if (e.getName().equals(String.format("Edge <%s -> %s>", v, u))) {
        foundEdge = true;
      }
    }

    String edgeMessage;

    if (value) {
      edgeMessage = "Edge (JPanel) with name \"Edge <%s -> %s>\" " +
          "must be placed in the graph panel after successful insertion.";
    } else {
      edgeMessage = "Edges can be inserted in \"Add an Edge\" mode only.";
    }

    if (foundEdge != value)
      throw new WrongAnswer(String.format(edgeMessage, v, u));
  }

  private void tryToIncludeTwoArbitraryEdges(Map<String, Vertex> map, JPanel target) {
    var v = "A";
    var u = "D";
    var a = "C";
    var b = "F";

    addEdges(map, target, v, u, a, b);
    checkEdgeLabel(v, u, 1, false);
    checkEdgeLabel(a, b, 2, false);
    checkEdge(u, v, false);
    checkEdge(v, u, false);
    checkEdge(a, b, false);
    checkEdge(b, a, false);
  }

  private void addEdges(Map<String, Vertex> map, JPanel target, String v, String u, String a, String b) {
    JOptionPaneFixture dialog;
    dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());

    dialog.textBox().setText(1 + "");
    dialog.okButton().click();

    var fixtureV = getVertexPanelFixture(map, target, v);
    var fixtureU = getVertexPanelFixture(map, target, u);
    fixtureV.click();
    fixtureU.click();

    dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());
    dialog.textBox().setText(2 + "");
    dialog.okButton().click();

    var fixtureA = getVertexPanelFixture(map, target, a);
    var fixtureB = getVertexPanelFixture(map, target, b);
    fixtureA.click();
    fixtureB.click();
  }
}
