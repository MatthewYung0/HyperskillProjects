import java.util.List;

public interface GraphData {
    List<Vertex> getVertices();

    List<Edge> getEdges();

    String getDFSText();

    String getBFSText();

    String getSource();
}
