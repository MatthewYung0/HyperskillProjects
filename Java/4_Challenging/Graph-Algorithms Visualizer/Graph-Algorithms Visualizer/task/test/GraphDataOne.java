import java.awt.*;
import java.util.List;

public class GraphDataOne implements GraphData {

    @Override
    public List<Vertex> getVertices() {
        return List.of(
                new Vertex("A", new Point(175, 359)),
                new Vertex("B", new Point(224, 176)),
                new Vertex("C", new Point(365, 363)),
                new Vertex("D", new Point(408, 67)),
                new Vertex("E", new Point(531, 235)),
                new Vertex("F", new Point(590, 64))
        );
    }

    @Override
    public List<Edge> getEdges() {
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

    @Override
    public String getDFSText() {
        return "DFS : A -> B -> D -> F -> E -> C";
    }

    @Override
    public String getBFSText() {
        return "BFS : A -> B -> C -> D -> E -> F";
    }

    @Override
    public String getDijkstraText() {
        return "B=1, C=6, D=3, E=8, F=6";
    }

    @Override
    public String getSource() {
        return "A";
    }
}
