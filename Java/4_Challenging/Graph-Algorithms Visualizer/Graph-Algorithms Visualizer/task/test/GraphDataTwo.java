import java.util.List;

public class GraphDataTwo {

  public static List<Vertex> getVertices() {
    return List.of(
        new Vertex(361, 54, "1"),
        new Vertex(152, 164, "2"),
        new Vertex(599, 155, "3"),
        new Vertex(47, 394, "4"),
        new Vertex(291, 392, "5"),
        new Vertex(419, 390, "6"),
        new Vertex(744, 392, "7")
    );
  }

  public static List<Edge> getEdges() {
    return List.of(
        new Edge("1", "2", 1),
        new Edge("1", "3", 3),
        new Edge("2", "4", 5),
        new Edge("5", "2", 7),
        new Edge("6", "3", 9),
        new Edge("7", "3", 11)
    );
  }

  public static String getDFSText() {
    return "DFS : 1 -> 2 -> 4 -> 5 -> 3 -> 6 -> 7";
  }

  public static String getBFSText() {
    return "BFS : 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7";
  }

  public static String getDijkstraText() {
    return "2=1, 3=3, 4=6, 5=8, 6=12, 7=14";
  }

  public static String getPrimText() {
    return "2=1, 3=1, 4=2, 5=2, 6=3, 7=3";
  }

  public static String getSource() {
    return "1";
  }
}
