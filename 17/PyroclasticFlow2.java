import java.util.HashMap;
import java.util.Scanner;

public class PyroclasticFlow2 {
  private static final int CHAMBER_WIDTH = 7;

  private static final int INITIAL_POS = 7647;
  private static final int INITIAL_TYPE = 4;
  private static final long INITIAL_ROCKS = 1304;
  private static final long INITIAL_HEIGHT = 2003L;
  private static final long ROCKS_PER_CYCLE = 1740;
  private static final long HEIGHT_PER_CYCLE = 2681L;
  private static final long REQUIRED_ROCKS = 1000000000000L;
  private static final long CYCLES = ((REQUIRED_ROCKS - INITIAL_ROCKS) / ROCKS_PER_CYCLE);
  private static final long MAX_ITERATIONS = REQUIRED_ROCKS - INITIAL_ROCKS - (CYCLES * ROCKS_PER_CYCLE);

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
    Scanner scanner = new Scanner(System.in);
    String pattern = scanner.nextLine();
    scanner.close();

    HashMap<Position, Character> map = new HashMap<>();
    map.put(new Position(0, 2), '#');
    map.put(new Position(0, 3), '#');
    map.put(new Position(0, 5), '#');
    map.put(new Position(1, 2), '#');
    map.put(new Position(1, 3), '#');
    map.put(new Position(2, 3), '#');

    long x = 2;
    long currentRocks = 0;

    Rock rock = new Rock(INITIAL_TYPE);
    long y = 6;
    int p = INITIAL_POS;
    long currentHeight = 3;
    
    while (currentRocks < MAX_ITERATIONS) {
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

      currentRocks++;
      currentHeight = Math.max(currentHeight, y + rockHeight);

      rock = new Rock((rock.type + 1) % Rock.shapes.length);
      x = 2;
      y = currentHeight + 3;
    }

    System.out.println(INITIAL_HEIGHT + (CYCLES * HEIGHT_PER_CYCLE) + currentHeight - 3);
  }
}
