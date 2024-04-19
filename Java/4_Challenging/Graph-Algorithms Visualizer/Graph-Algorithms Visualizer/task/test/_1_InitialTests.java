import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import visualizer.MainFrame;

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

@SuppressWarnings("unused")
public class _1_InitialTests extends SwingTest {

  @SwingComponent
  JPanelFixture graph;

  @SwingComponent(name = "Exit")
  JMenuItemFixture exitMenuItem;

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

  @SwingComponent(name = "Mode")
  JLabelFixture mode;

  public _1_InitialTests() {
    super(new MainFrame());
  }

  @DynamicTest(order = 1, feedback = "Either title (Expected: \"Graph-Algorithms Visualizer\") or " +
      "the dimension of the frame (Expected: 800 x 600) is incorrect")
  CheckResult checkTitleAndDimension() {
    if (!frame.getTitle().equals("Graph-Algorithms Visualizer"))
      throw new WrongAnswer("Title of the frame must be \"Graph-Algorithms Visualizer\" " +
          "but instead got - \"" + frame.getTitle() + "\"");
    if (!(frame.getHeight() == 600 && frame.getWidth() == 800)) {
      throw new WrongAnswer("Frame Dimension should be (w=800, h=600)");
    }
    return correct();
  }

  @DynamicTest(order = 2, feedback = "Could not check components. " +
      "Make sure there's no components in the graph initially")
  CheckResult checkForEmptyGraphPanel() {
    return TestingUtils.assertComponentCount(graph, 0);
  }

  @DynamicTest(order = 3, feedback = "Could not change mode.")
  CheckResult checkForModeChanges() {
    addVertexMenuItem.click();
    modeCheck("Add a Vertex");

    removeVertexMenuItem.click();
    modeCheck("Remove a Vertex");

    addEdgeMenuItem.click();
    modeCheck("Add an Edge");

    removeEdgeMenuItem.click();
    modeCheck("Remove an Edge");

    noneMenuItem.click();
    modeCheck("None");

    return correct();
  }

  @DynamicTest(order = 4)
  CheckResult exitCheck() {
    exitMenuItem.click();
    try {
      var frameFixture = WindowFinder
          .findFrame("Graph-Algorithms Visualizer")
          .withTimeout(200)
          .using(getWindow().robot());
    } catch (WaitTimedOutError e) {
      return correct();
    }
    return wrong("Window is not closed after clicking on the exit menu item.");
  }

  private void modeCheck(String text) {
    var msg = "After clicking on \"%s\" Menu Item, the mode (JLabel) should should be \"%s\". Got : \"%s\"";

    if (mode.text() == null || !mode.text().contains(text))
      throw new WrongAnswer(String.format(msg, text, text, mode.text()));
  }
}
