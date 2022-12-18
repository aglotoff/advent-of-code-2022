import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class BoilingBoulders {
  public static class Side {
    private int x;
    private int y;
    private int z;
    private int lx;
    private int ly;
    private int lz;
    private int face;

    public Side(int x, int y, int z, int lx, int ly, int lz, int face) {
      this.x = Math.min(x, x + lx);
      this.y = Math.min(y, y + ly);
      this.z = Math.min(z, z + lz);
      this.lx = Math.abs(lx);
      this.ly = Math.abs(ly);
      this.lz = Math.abs(lz);
      this.face = face;
    }

    @Override
    public int hashCode() {
      int hash = x;
      hash = 31 * hash + y;
      hash = 31 * hash + z;
      hash = 31 * hash + lx;
      hash = 31 * hash + ly;
      hash = 31 * hash + lz;
      hash = 31 * hash + face;
      return hash;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (o == null || !(o instanceof Side)) {
        return false;
      }

      Side c = (Side) o;
      
      return c.x == x && c.y == y && c.z == z && c.lx == lx && c.ly == ly &&
          c.lz == lz && c.face == face;
    }

    public Side flip() {
      return new Side(x, y, z, lx, ly, lz, -face);
    }
  }

  public static void main(String[] args) {
    HashSet<Side> sides = new HashSet<>();

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] coords = scanner.nextLine().split(",");
      int x = Integer.parseInt(coords[0]);
      int y = Integer.parseInt(coords[1]);
      int z = Integer.parseInt(coords[2]);
      
      LinkedList<Side> cubeSides = new LinkedList<>();
      cubeSides.add(new Side(x, y, z, 1, 1, 0, -1));
      cubeSides.add(new Side(x, y, z + 1, 1, 1, 0, 1));
      cubeSides.add(new Side(x, y, z, 1, 0, 1, -1));
      cubeSides.add(new Side(x, y + 1, z, 1, 0, 1, 1));
      cubeSides.add(new Side(x, y, z, 0, 1, 1, -1));
      cubeSides.add(new Side(x + 1, y, z, 0, 1, 1, 1));

      for (Side side: cubeSides) {
        if (sides.contains(side.flip())) {
          sides.remove(side.flip());
        } else {
          sides.add(side);
        }
      }
    }

    scanner.close();

    System.out.println(sides.size());

    Side leftmostSide = null;
    for (Side side: sides) {
      if (leftmostSide == null || leftmostSide.x > side.x) {
        leftmostSide = side;
      }
    }

    LinkedList<Side> queue = new LinkedList<>();
    HashSet<Side> visited = new HashSet<>();

    queue.add(leftmostSide);
    visited.add(leftmostSide);

    while (!queue.isEmpty()) {
      Side s = queue.removeFirst();

      ArrayList<LinkedList<Side>> moves = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
        moves.add(new LinkedList<>());
      }

      if (s.lx == 0) {
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, 1, 0, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 0, 1, -1, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, -1, 1, 0, -s.face));

        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, 1, 1, 0, -s.face));
        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, 0, 1, 1, s.face));
        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, -1, 1, 0, s.face));

        moves.get(2).addLast(new Side(s.x, s.y, s.z, 1, 0, 1, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, 0, -1, 1, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, -1, 0, 1, -s.face));

        moves.get(3).addLast(new Side(s.x, s.y + 1, s.z, 1, 0, 1, -s.face));
        moves.get(3).addLast(new Side(s.x, s.y + 1, s.z, 0, 1, 1, s.face));
        moves.get(3).addLast(new Side(s.x, s.y + 1, s.z, -1, 0, 1, s.face));
      } else if (s.ly == 0) {
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, 1, 0, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, 0, -1, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, -1, 0, -s.face));

        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, 1, 1, 0, -s.face));
        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, 1, 0, 1, s.face));
        moves.get(1).addLast(new Side(s.x, s.y, s.z + 1, 1, -1, 0, s.face));

        moves.get(2).addLast(new Side(s.x, s.y, s.z, 0, 1, 1, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, -1, 0, 1, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, 0, -1, 1, -s.face));

        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 0, 1, 1, -s.face));
        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 1, 0, 1, s.face));
        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 0, -1, 1, s.face));
      } else {
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, 0, 1, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, -1, 0, s.face));
        moves.get(0).addLast(new Side(s.x, s.y, s.z, 1, 0, -1, -s.face));

        moves.get(1).addLast(new Side(s.x, s.y + 1, s.z, 1, 0, 1, -s.face));
        moves.get(1).addLast(new Side(s.x, s.y + 1, s.z, 1, 1, 0, s.face));
        moves.get(1).addLast(new Side(s.x, s.y + 1, s.z, 1, 0, -1, s.face));

        moves.get(2).addLast(new Side(s.x, s.y, s.z, 0, 1, 1, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, -1, 1, 0, s.face));
        moves.get(2).addLast(new Side(s.x, s.y, s.z, 0, 1, -1, -s.face));

        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 0, 1, 1, -s.face));
        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 1, 1, 0, s.face));
        moves.get(3).addLast(new Side(s.x + 1, s.y, s.z, 0, 1, -1, s.face));
      }

      for (LinkedList<Side> m: moves) {
        while (!m.isEmpty()) {
          Side nextMove = s.face == 1 ? m.removeFirst() : m.removeLast();
          if (sides.contains(nextMove)) {
            if (!visited.contains(nextMove)) {
              queue.addLast(nextMove);
              visited.add(nextMove);
            }
            break;
          }
        }
      }
    }

    System.out.println(visited.size());
  }
}
