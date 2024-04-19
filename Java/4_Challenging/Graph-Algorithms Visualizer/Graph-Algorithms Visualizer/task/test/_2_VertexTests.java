import org.assertj.swing.core.Robot;
import org.assertj.swing.exception.ComponentLookupException;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

@SuppressWarnings("unused")
public class _2_VertexTests extends SwingTest {

  @SwingComponent
  JPanelFixture graph;

  @SwingComponent(name = "New")
  JMenuItemFixture newMenuItem;

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

  public _2_VertexTests() {
    super(new MainFrame());
  }

  @DynamicTest(order = 5)
  CheckResult checkDialogAppearing() {
    addVertexMenuItem.click();
    graph.click();

    try {

      testDialog("");
      testDialog("123");
      testDialog("ABC");
      cancelDialog();
      graph.click();
      testDialog("0");

    } catch (WaitTimedOutError wt) {
      throw new WrongAnswer("Could not find a dialog box after clicking on the graph panel.");
    } catch (ComponentLookupException cl) {
      throw new WrongAnswer("A ok button, cancel button and a text field must be present inside the dialog");
    } catch (IllegalStateException e) {
      throw new WrongAnswer("Either the text field is disabled or it is not showing on the screen.");
    } catch (Exception e) {
      throw new WrongAnswer("Something went wrong. Please read the instructions carefully and try again.");
    }

    return TestingUtils.assertComponentCount(graph, 1);
  }

  @DynamicTest(order = 6)
  CheckResult addVertices() {
    for (var pos : getVertexTestingPositions()) {
      assertSuccessfulVertexInsertion(graph,
          pos.getX(),
          pos.getY(),
          pos.getId(),
          getWindow().robot());
    }
    return correct();
  }

  @DynamicTest(order = 7)
  CheckResult checkComponentsAfterVertexInsertions() {
    return TestingUtils.assertComponentCount(graph, 8);
  }

  @DynamicTest(order = 8, feedback = "There's a problem with VertexLabel (JLabel)")
  CheckResult checkVertexLabels() {
    int flag = 0;
    for (var component : getAllComponents(graph.target())) {
      if (component instanceof JLabel && component.getName().startsWith("VertexLabel ")) {
        var labelName = component.getName().replace("VertexLabel ", "");
        var componentName = component.getParent().getName().replace("Vertex ", "");
        if (!(labelName.equals(componentName))) {
          throw new WrongAnswer("All of the vertices must contain " +
              "a JLabel with name as \"VertexLabel <id>\" and text as \"<id>\" in it.");
        }
        if (!component.getParent().getName().startsWith("Vertex "))
          throw new WrongAnswer("All VertexLabel must be inside of Vertex (JPanel)");
        flag++;
      }
    }
    if (flag != getVertexTestingPositions().size() + 1) {
      throw new WrongAnswer("All of the vertices must contain " +
          "a JLabel with name as \"VertexLabel <id>\" and text as \"<id>\" in it.");
    }
    return correct();
  }

  @DynamicTest(order = 9, feedback = "Graph panel should be empty after clicking on new menu item (JMenuItem)")
  CheckResult checkForEmptyGraphAfterClickingNewMenuItem() {
    newMenuItem.click();

    if (mode.text() == null && !mode.text().contains("Add a Vertex"))
      throw new WrongAnswer("Mode should be \"Add a Vertex\" after " +
          "clicking on new menu item. Found : " + mode.text());

    if (graph.target().getComponents().length != 0)
      throw new WrongAnswer("Graph panel should not contain anything " +
          "after clicking on new menu item. Found : " + graph.target().getComponents().length);

    return correct();
  }

  @DynamicTest(order = 10)
  CheckResult checkVertexInsertionInNoneMode() {
    noneMenuItem.click();
    return checkVertexInsertionInOtherModes();
  }

  @DynamicTest(order = 11)
  CheckResult checkVertexInsertionInAddEdgeMode() {
    addEdgeMenuItem.click();
    return checkVertexInsertionInOtherModes();
  }

  @DynamicTest(order = 12)
  CheckResult checkVertexInsertionInRemoveEdgeMode() {
    removeEdgeMenuItem.click();
    return checkVertexInsertionInOtherModes();
  }

  @DynamicTest(order = 13)
  CheckResult checkVertexInsertionInRemoveVertexMode() {
    removeVertexMenuItem.click();
    return checkVertexInsertionInOtherModes();
  }

  private CheckResult checkVertexInsertionInOtherModes() {
    getWindow().robot().click(graph.target(), new Point(100, 200));
    try {
      JOptionPaneFinder
          .findOptionPane()
          .withTimeout(200)
          .using(getWindow().robot());
      throw new WrongAnswer("Vertex adding process should only " +
          "be initiated in \"Add a Vertex\" mode. Current Mode: " + mode.text());
    } catch (WaitTimedOutError e) {
      return correct();
    }
  }

  public static List<Vertex> getVertexTestingPositions() {
    return List.of(
        new Vertex(116, 108, "1"),
        new Vertex(579, 238, "9"),
        new Vertex(671, 91, "A"),
        new Vertex(290, 309, "H"),
        new Vertex(581, 360, "N"),
        new Vertex(551, 72, "Y"),
        new Vertex(698, 249, "Z")
    );
  }

  public static void assertSuccessfulVertexInsertion(JPanelFixture graph, int x, int y, String id, Robot robot) {
    robot.click(graph.target(), new Point(x, y));
    JOptionPaneFixture dialog;
    try {
      dialog = TestingUtils.getOptionPaneFixture(robot);
    } catch (WaitTimedOutError e) {
      throw new WrongAnswer(e.getMessage());
    }

    dialog
        .textBox()
        .setText(id);

    if (!dialog.textBox().text().equals(id))
      throw new WrongAnswer(
          String.format("Content of the text field inside the dialog box must be " +
              "filled with the content after filling. Expected: \"%s\", Got: \"%s\"", id, dialog.textBox().text()));

    dialog
        .okButton()
        .click();


    boolean flag = false;
    var feedbackText = String.format("Graph Panel should contain a vertex with name \"Vertex %s\" " +
        "after a successful insertion.", id);

    for (var vertex : getAllComponents(graph.target())) {
      if (vertex.getName().equals("Vertex " + id) && vertex instanceof JComponent) {
        if (!(vertex.getX() == (x - 25) && vertex.getY() == (y - 25)))
          throw new WrongAnswer(
              String.format("Incorrect position of Vertex %s -> (%d, %d), Expected position -> (%s, %s).",
                  id, vertex.getX(), vertex.getY(), x, y));
        flag = true;
        break;
      }
    }
    if (!flag)
      throw new WrongAnswer(feedbackText);
  }

  private void cancelDialog() {
    JOptionPaneFixture dialog;
    try {
      dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());
    } catch (WaitTimedOutError e) {
      throw new WrongAnswer(e.getMessage());
    }

    dialog.cancelButton().click();
  }

  private void testDialog(String s) {
    JOptionPaneFixture dialog;
    try {
      dialog = TestingUtils.getOptionPaneFixture(getWindow().robot());
    } catch (WaitTimedOutError e) {
      throw new WrongAnswer(e.getMessage());
    }

    dialog.textBox()
        .setText(s);

    dialog.okButton().click();
  }
}
