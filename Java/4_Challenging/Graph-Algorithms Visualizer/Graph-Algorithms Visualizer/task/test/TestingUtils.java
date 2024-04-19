import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

public class TestingUtils {
  public static CheckResult assertComponentCount(JPanelFixture graph, int count) {
    var length = graph.target().getComponents().length;
    if (length != count)
      throw new WrongAnswer(String.format(
          "The graph panel should contain %d items, but %d items were found", count, length));
    return correct();
  }


  public static JOptionPaneFixture getOptionPaneFixture(Robot robot) {
    return JOptionPaneFinder
        .findOptionPane()
        .withTimeout(1000)
        .using(robot);
  }

  public static CheckResult addEdge(List<Edge> edges, JPanelFixture graph, Robot robot) {
    for (var e : edges) {
      new JPanelFixture(robot, (JPanel) getVertex(e.getFrom(), graph)).click();
      new JPanelFixture(robot, (JPanel) getVertex(e.getTo(), graph)).click();

      var dialog = getOptionPaneFixture(robot);
      dialog.textBox().setText(e.getWeight() + "");
      dialog.okButton().click();
    }

    var components = Arrays.stream(graph.target().getComponents())
        .filter((i) -> i.getName()
            .startsWith("Edge <"))
        .count();
    var noOfEdges = 2 * edges.size();
    if (components != noOfEdges)
      return wrong(String.format("%d edges was expected to be present, but found %d", noOfEdges, components));

    return correct();
  }

  public static Component getVertex(String id, JPanelFixture graph) {
    for (var v : SwingTest.getAllComponents(graph.target())) {
      if (v.getName() == null) {
        throw new WrongAnswer("Every component must have a name. Unnamed Component Found : " + v);
      }
      if (v.getName().equals("Vertex " + id))
        return v;
    }
    throw new NoSuchElementException();
  }
}
