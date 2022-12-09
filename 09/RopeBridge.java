import java.util.HashSet;
import java.util.Scanner;

public class RopeBridge {
  private static class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }

      if (other == null || !(other instanceof Position)) {
        return false;
      }

      Position p = (Position) other;
      return p.x == x && p.y == y;
    }

    public int hashCode() {
      return 31 * x + y;
    }

    public boolean isTouching(Position p) {
      return (Math.abs(x - p.x) <= 1) && (Math.abs(y - p.y) <= 1);
    }

    public Position moveLeft() {
      return new Position(x - 1, y);
    }

    public Position moveRight() {
      return new Position(x + 1, y);
    }

    public Position moveUp() {
      return new Position(x, y + 1);
    }

    public Position moveDown() {
      return new Position(x, y - 1);
    }
  }
  
  public static void main(String[] args) {
    int ropeLength = Integer.parseInt(args[0]);

    Position[] rope = new Position[ropeLength];
    for (int i = 0; i < ropeLength; i++) {
      rope[i] = new Position(0, 0);
    }

    HashSet<Position> visited = new HashSet<>();
    visited.add(new Position(0, 0));
    
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");

      char direction = line[0].charAt(0);
      int stepCount = Integer.parseInt(line[1]);

      for (int step = 0; step < stepCount; step++) { 
        switch (direction) {
          case 'U':
            rope[0] = rope[0].moveUp();
            break;
          case 'R':
            rope[0] = rope[0].moveRight();
            break;
          case 'D':
            rope[0] = rope[0].moveDown();
            break;
          case 'L':
            rope[0] = rope[0].moveLeft();
            break;
        }

        for (int i = 1; i < ropeLength; i++) {
          Position head = rope[i - 1];
          Position tail = rope[i];

          if (tail.isTouching(head)) {
            break;
          }

          if (tail.x < head.x) {
            tail = tail.moveRight();
          } else if (tail.x > head.x) {
            tail = tail.moveLeft();
          }

          if (tail.y < head.y) {
            tail = tail.moveUp();
          } else if (tail.y > head.y) {
            tail = tail.moveDown();
          }

          rope[i] = tail;
        }

        visited.add(rope[ropeLength - 1]);
      }
    }

    scanner.close();

    System.out.println(visited.size());
  }
}
