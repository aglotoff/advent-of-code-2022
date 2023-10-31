import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class BlizzardBasin {
  private static int gcd(int p, int q) {
    if (q == 0) return p;
    return gcd(q, p % q);
  }

  private static int lcm(int p, int q) {
    return p * (q / gcd(p, q));
  }

  private static class Node {
    int row;
    int col;
    int dist;

    public Node(int row, int col, int dist) {
      this.row = row;
      this.col = col;
      this.dist = dist;
    }
  }

  public static int find(boolean[][][] danger, int row, int col, int dist, int targetRow, int targetCol) {
    int height = danger.length;
    int width = danger[0].length;
    int maxStates = danger[0][0].length;

    boolean[][][] visited = new boolean[height][width][maxStates];
    LinkedList<Node> queue = new LinkedList<>();

    queue.addLast(new Node(row, col, dist));
    visited[row][col][dist % maxStates] = true;

    while (!queue.isEmpty()) {
      Node current = queue.removeFirst();
    
      if ((current.row == targetRow) && (current.col == targetCol)) {
        return current.dist;
      }

      int nextRow = current.row;
      int nextCol = current.col;
      int nextState = (current.dist + 1) % maxStates;
      int nextDist = current.dist + 1;

      if (!visited[nextRow][nextCol][nextState] && !danger[nextRow][nextCol][nextState]) {
        visited[nextRow][nextCol][nextState] = true;
        queue.addLast(new Node(nextRow, nextCol, nextDist));
      }

      if ((current.row > 1) || (current.row == 1 && current.col == 1)) {
        nextRow = current.row - 1;
        if (!visited[nextRow][nextCol][nextState] && !danger[nextRow][nextCol][nextState]) {
          visited[nextRow][nextCol][nextState] = true;
          queue.addLast(new Node(nextRow, nextCol, nextDist));
        }
      }

      if ((current.row < height - 2) || (current.row == height - 2 && current.col == width - 2)) {
        nextRow = current.row + 1;
        if (!visited[nextRow][nextCol][nextState] && !danger[nextRow][nextCol][nextState]) {
          visited[nextRow][nextCol][nextState] = true;
          queue.addLast(new Node(nextRow, nextCol, nextDist));
        }
      }

      if ((current.col > 1) && (current.row > 0) && (current.row < height - 1)) {
        nextRow = current.row;
        nextCol = current.col - 1;
        if (!visited[nextRow][nextCol][nextState] && !danger[nextRow][nextCol][nextState]) {
          visited[nextRow][nextCol][nextState] = true;
          queue.addLast(new Node(nextRow, nextCol, nextDist));
        }
      }
      if ((current.col < width - 2) && (current.row > 0) && (current.row < height - 1)) {
        nextRow = current.row;
        nextCol = current.col + 1;
        if (!visited[nextRow][nextCol][nextState] && !danger[nextRow][nextCol][nextState]) {
          visited[nextRow][nextCol][nextState] = true;
          queue.addLast(new Node(nextRow, nextCol, nextDist));
        }
      }
    }

    return -1;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    HashMap<Integer, HashMap<Integer, Character>> blizzards = new HashMap<>();

    int width = 0;
    int height = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (width == 0) {
        width = line.length();
      }

      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        if (c != '#' && c != '.') {
          if (!blizzards.containsKey(height)) {
            HashMap<Integer, Character> b = new HashMap<>();
            b.put(i, c);
            blizzards.put(height, b);
          } else {
            blizzards.get(height).put(i, c);
          }
        }
      }

      height++;
    }

    scanner.close();

    int maxStates = lcm(width - 2, height - 2);

    boolean[][][] danger = new boolean[height][width][maxStates];

    for (int row: blizzards.keySet()) {
      for (int col: blizzards.get(row).keySet()) {
        char c = blizzards.get(row).get(col);

        int i = row;
        int j = col;

        for (int k = 0; k < maxStates; k++) {
          danger[i][j][k] = true;

          switch (c) {
            case '>':
              j++;
              if (j == width - 1) j = 1;
              break;
            case '<':
              j--;
              if (j == 0) j = width - 2;
              break;
            case 'v':
              i++;
              if (i == height - 1) i = 1;
              break;
            case '^':
              i--;
              if (i == 0) i = height - 2;
              break;
          }
        }
      }
    }

    int time = find(danger, 0, 1, 0, height - 1, width - 2);
    System.out.println(time);

    time = find(danger, height - 1, width - 2, time, 0, 1);
    time = find(danger, 0, 1, time, height - 1, width - 2);
    System.out.println(time);
  }
}
