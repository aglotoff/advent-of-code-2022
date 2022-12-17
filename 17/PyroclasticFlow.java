import java.util.HashMap;
import java.util.Scanner;

public class PyroclasticFlow {
  private static final int CHAMBER_WIDTH = 7;

  private static class Position {
    long x;
    long y;

    public Position(long y, long x) {
      this.y = y;
      this.x = x;
    }

    public int hashCode() {
      return 31 * Long.hashCode(y) + Long.hashCode(x);
    }

    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o == null || !(o instanceof Position)) {
        return false;
      }
      Position p = (Position) o;
      return p.x == x && p.y == y;
    }
  }

  private static class Rock {
    private static char[][][] shapes = {
      {
        { '#', '#', '#', '#' },
      },
      {
        { '.', '#', '.' },
        { '#', '#', '#' },
        { '.', '#', '.' },
      },
      {
        { '#', '#', '#' },
        { '.', '.', '#' },
        { '.', '.', '#' },
      },
      {
        { '#' },
        { '#' },
        { '#' },
        { '#' },
      },
      {
        { '#', '#' },
        { '#', '#' }
      },
    };

    private int type;

    public Rock(int type) {
      this.type = type;
    }

    public int getHeight() {
      return shapes[type].length;
    }

    public int getWidth() {
      return shapes[type][0].length;
    }

    public char[][] getShape() {
      return shapes[type];
    }
  }

  public static void main(String[] args) {
    long maxRocks = Long.parseLong(args[0]);

    Scanner scanner = new Scanner(System.in);
    String pattern = scanner.nextLine();
    scanner.close();

    HashMap<Position, Character> map = new HashMap<>();


    long x = 2;
    long currentRocks = 0;

    Rock rock = new Rock(0);
    long y = 3;
    int p = 0;
    long currentHeight = 0;

    // Uncomment for cycle detection:
    // long prevRocks = 0;
    // long prevHeight = 0;
    
    while (currentRocks < maxRocks) {
      char[][] shape = rock.getShape();
      int rockWidth = rock.getWidth();
      int rockHeight = rock.getHeight();

      boolean canMoveDown = true;

      while (canMoveDown) {
        char direction = pattern.charAt(p);

        if ((direction == '<') && (x > 0)) {
          boolean canMoveLeft = true;

          checkSides: for (int i = 0; i < rockHeight; i++) {
            for (int j = 0; j < rockWidth; j++) {
              if ((shape[i][j] == '#') &&
                  map.containsKey(new Position(y + i, x + j - 1))) {
                canMoveLeft = false;
                break checkSides;
              }
            }
          }

          if (canMoveLeft) {
            x--;
          }
        } else if ((direction == '>') && x + rock.getWidth() < CHAMBER_WIDTH) {
          boolean canMoveRight = true;

          checkSides: for (int i = 0; i < rockHeight; i++) {
            for (int j = 0; j < rockWidth; j++) {
              if ((shape[i][j] == '#') &&
                  map.containsKey(new Position(y + i, x + j + 1))) {
                canMoveRight = false;
                break checkSides;
              }
            }
          }

          if (canMoveRight) {
            x++;
          }
        }

        if (y == 0) {
          canMoveDown = false;
        } else {
          checkSides: for (int i = 0; i < rockHeight; i++) {
            for (int j = 0; j < rockWidth; j++) {
              if ((shape[i][j] == '#') &&
                  map.containsKey(new Position(y + i - 1, x + j))) {
                canMoveDown = false;
                break checkSides;
              }
            }
          }
        }

        if (canMoveDown) {
          y--;
        }

        p = (p + 1) % pattern.length();
      }
        
      for (int i = 0; i < rockHeight; i++) {
        for (int j = 0; j < rockWidth; j++) {
          if (shape[i][j] == '#') {
            map.put(new Position(y + i, x + j), '#');
          }
        }
      }

      // Uncomment for cycle detection:
      // boolean completeRow = false;
      // if (y >= currentHeight - rockHeight) {
      //   completeRow = true;
      //   for (int j = 0; j < CHAMBER_WIDTH; j++) {
      //     if (!map.containsKey(new Position(y, j))) {
      //       completeRow = false;
      //     }
      //   }
      // }

      currentRocks++;
      currentHeight = Math.max(currentHeight, y + rockHeight);

      // Uncomment for cycle detection:
      // if (completeRow) {
      //   System.out.println("Complete row #" + y);
      //   System.out.println("  height = " + currentHeight + " (+" + (currentHeight - prevHeight) + ")");
      //   System.out.println("  rocks = " + currentRocks + " (+" + (currentRocks - prevRocks) + ")");
      //   System.out.println("  next pattern idx = " + p);
      //   System.out.println();
 
      //   prevRocks = currentRocks;
      //   prevHeight = currentHeight;

      //   for (long i = y + rockHeight - 1; i >= y; i--) {
      //     System.out.print("  ");
      //     for (int j = 0; j < CHAMBER_WIDTH; j++) {
      //       if (map.containsKey(new Position(i, j))) {
      //         System.out.print('#');
      //       } else {
      //         System.out.print('.');
      //       }
      //     }
      //     System.out.println();
      //   }
      //   System.out.println();
      // }

      rock = new Rock((rock.type + 1) % Rock.shapes.length);
      x = 2;
      y = currentHeight + 3;
    }

    System.out.println(currentHeight);
  }
}
