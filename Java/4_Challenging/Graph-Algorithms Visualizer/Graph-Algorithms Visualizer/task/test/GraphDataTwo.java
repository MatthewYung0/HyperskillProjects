import java.awt.*;
import java.util.List;

public class GraphDataTwo implements GraphData {

    @Override
    public List<Vertex> getVertices() {
        return List.of(
                new Vertex("1", new Point(361, 54)),
                new Vertex("2", new Point(152, 164)),
                new Vertex("3", new Point(599, 155)),
                new Vertex("4", new Point(47, 394)),
                new Vertex("5", new Point(291, 392)),
                new Vertex("6", new Point(419, 390)),
                new Vertex("7", new Point(744, 392))
        );
    }

    @Override
    public List<Edge> getEdges() {
        return List.of(
                new Edge("1", "2", 1),
                new Edge("1", "3", 3),
                new Edge("2", "4", 5),
                new Edge("5", "2", 7),
                new Edge("6", "3", 9),
                new Edge("7", "3", 11)
        );
    }

    @Override
    public String getDFSText() {
        return "DFS : 1 -> 2 -> 4 -> 5 -> 3 -> 6 -> 7";
    }

    @Override
    public String getBFSText() {
        return "BFS : 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7";
    }

    @Override
    public String getSource() {
        return "1";
    }
}
