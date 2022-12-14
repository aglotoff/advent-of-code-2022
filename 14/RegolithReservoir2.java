import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class RegolithReservoir2 {
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
    int maxY = 0;

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      ArrayList<Point> path = new ArrayList<>();

      for (String point: scanner.nextLine().split(" -> ")) {
        String[] coords = point.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        maxY = Math.max(y, maxY);

        path.add(new Point(x, y));
      }
      
      paths.add(path);
    }

    scanner.close();

    maxY++;

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
    while (!map.containsKey(new Point(500, 0))) {
      int x = 500;
      int y = 0;

      while (y < maxY) {
        if (!map.containsKey(new Point(x, y + 1))) {
          y++;
        } else if (!map.containsKey(new Point(x - 1, y + 1))) {
          y++;
          x--;
        } else if (!map.containsKey(new Point(x + 1, y + 1))) {
          y++;
          x++;
        } else {
          break;
        }
      }

      map.put(new Point(x, y), 'o');
      count++;
    }

    System.out.println(count);
  }
}
