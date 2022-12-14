import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class RegolithReservoir {
  private static class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }

      if (other == null || !(other instanceof Point)) {
        return false;
      }

      Point p = (Point) other;
      return p.x == x && p.y == y;
    }

    public int hashCode() {
      return 31 * x + y;
    }
  }

  public static void main(String[] args) {
    LinkedList<ArrayList<Point>> paths = new LinkedList<>();
    int minX = Integer.MAX_VALUE;
    int maxX = 0;
    int maxY = 0;

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      ArrayList<Point> path = new ArrayList<>();

      for (String point: scanner.nextLine().split(" -> ")) {
        String[] coords = point.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        minX = Math.min(x, minX);
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);

        path.add(new Point(x, y));
      }
      
      paths.add(path);
    }

    scanner.close();

    HashMap<Point, Character> map = new HashMap<>();

    for (ArrayList<Point> path: paths) {
      int x = path.get(0).x;
      int y = path.get(0).y;

      map.put(new Point(x, y), '#');

      for (int i = 1; i < path.size(); i++) {
        int nextX = path.get(i).x;
        int nextY = path.get(i).y;

        if (nextX > x) {
          while (x < nextX) {
            map.put(new Point(++x, y), '#');
          }
        } else if (nextX < x) {
          while (x > nextX) {
            map.put(new Point(--x, y), '#');
          }
        } else if (nextY > y) {
          while (y < nextY) {
            map.put(new Point(x, ++y), '#');
          }
        } else if (nextY < y) {
          while (y > nextY) {
            map.put(new Point(x, --y), '#');
          }
        }
      }
    }

    int count = 0;
    boolean flowsOut = false;

    for (count = 0; !flowsOut; count++) {
      int x = 500;
      int y = 0;

      for (;;) {
        if (y >= maxY) {
          flowsOut = true;
          break;
        }

        if (!map.containsKey(new Point(x, y + 1))) {
          // Fall down
          y++;
        } else if ((x == 0) || (x == maxX)) {
          flowsOut = true;
          break;
        } else if (!map.containsKey(new Point(x - 1, y + 1))) {
          y++;
          x--;
        } else if (!map.containsKey(new Point(x + 1, y + 1))) {
          y++;
          x++;
        } else {
          // Stop
          map.put(new Point(x, y), 'o');
          break;
        }
      }
    }

    System.out.println(count - 1);
  }
}
