import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.PriorityQueue;

public class HillClimbingAlgorithm {
  private static class SearchNode implements Comparable<SearchNode> {
    private int row;
    private int column;
    private int dist;

    public SearchNode(int row, int column, int dist) {
      this.row = row;
      this.column = column;
      this.dist = dist;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o == null || !(o instanceof SearchNode)) {
        return false;
      }
      SearchNode s = (SearchNode) o;
      return s.column == column && s.row == row;
    }

    @Override
    public int compareTo(SearchNode o) {
      return Integer.compare(dist, o.dist);
    }
  }

  public static void main(String[] args) {
    ArrayList<ArrayList<Integer>> map = new ArrayList<>();

    int initialRow = 0;
    int initialColumn = 0;

    int targetRow = 0;
    int targetColumn = 0;

    Scanner scanner = new Scanner(System.in);

    for (int i = 0; scanner.hasNextLine(); i++) {
      String line = scanner.nextLine();

      ArrayList<Integer> row = new ArrayList<>();
      for (int j = 0; j < line.length(); j++) {
        char c = line.charAt(j);

        if (c == 'S') {
          initialRow = i;
          initialColumn = j;
          row.add(0);
        } else if (c == 'E') {
          targetRow = i;
          targetColumn = j;
          row.add(25);
        } else {
          row.add(c - 'a');
        }
      }

      map.add(row);
    }

    scanner.close();

    PriorityQueue<SearchNode> pq = new PriorityQueue<>();
    pq.add(new SearchNode(targetRow, targetColumn, 0));

    boolean[][] visited = new boolean[map.size()][map.get(0).size()];

    int[][] distTo = new int[map.size()][map.get(0).size()];
    for (int i = 0; i < distTo.length; i++) {
      for (int j = 0; j < distTo[i].length; j++) {
        distTo[i][j] = Integer.MAX_VALUE;
      }
    }
    distTo[targetRow][targetColumn] = 0;

    while (!pq.isEmpty()) {
      SearchNode current = pq.poll();

      int row = current.row;
      int column = current.column;
      int currentElevation = map.get(row).get(column);
      int currentDist = distTo[row][column];

      LinkedList<SearchNode> moves = new LinkedList<>();

      if (row > 0) {
        moves.add(new SearchNode(row - 1, column, currentDist + 1));
      }
      if (row < map.size() - 1) {
        moves.add(new SearchNode(row + 1, column, currentDist + 1));
      }
      if (column > 0) {
        moves.add(new SearchNode(row, column - 1, currentDist + 1));
      }
      if (column < map.get(row).size() - 1) {
        moves.add(new SearchNode(row, column + 1, currentDist + 1));
      }

      for (SearchNode move: moves) {
        if (visited[move.row][move.column]) {
          continue;
        }

        int neighborElevation = map.get(move.row).get(move.column);
        if (currentElevation - neighborElevation > 1) {
          continue;
        }

        int neighborDist = distTo[move.row][move.column];
        if (neighborDist > move.dist) {
          pq.add(move);
        }

        distTo[move.row][move.column] = Math.min(neighborDist, currentDist + 1);
      }

      visited[row][column] = true;
    }

    System.out.println(distTo[initialRow][initialColumn]);

    int shortestTrail = Integer.MAX_VALUE;
    for (int i = 0; i < map.size(); i++) {
      for (int j = 0; j < map.get(i).size(); j++) {
        if ((map.get(i).get(j) == 0) && (distTo[i][j] < shortestTrail)) {
          shortestTrail = distTo[i][j];
        }
      }
    }

    System.out.println(shortestTrail);
  }
}
