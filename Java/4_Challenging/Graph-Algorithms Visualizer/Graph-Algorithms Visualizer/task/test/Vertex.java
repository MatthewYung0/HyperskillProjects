import java.awt.*;

public final class Vertex {
  private final int x;
  private final int y;
  private final String id;

  public Vertex(int x, int y, String id) {
    this.x = x;
    this.y = y;
    this.id = id;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }


  public String getId() {
    return id;
  }

  public Point asPoint() {
    return new Point(getX(), getY());
  }
}
