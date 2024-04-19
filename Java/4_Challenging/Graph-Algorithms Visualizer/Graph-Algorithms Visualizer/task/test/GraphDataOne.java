import java.util.List;

public class GraphDataOne {

  public static List<Vertex> getVertices() {
    return List.of(
        new Vertex(175, 359, "A"),
        new Vertex(224, 176, "B"),
        new Vertex(365, 363, "C"),
        new Vertex(408, 67, "D"),
        new Vertex(531, 235, "E"),
        new Vertex(590, 64, "F")
    );
  }

  public static List<Edge> getEdges() {
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

  public static String getDFSText() {
    return "DFS : A -> B -> D -> F -> E -> C";
  }

  public static String getBFSText() {
    return "BFS : A -> B -> C -> D -> E -> F";
  }

  public static String getDijkstraText() {
    return "B=1, C=6, D=3, E=8, F=6";
  }

  public static String getPrimText() {
    return "B=A, C=E, D=B, E=F, F=D";
  }

  public static String getSource() {
    return "A";
  }
}
