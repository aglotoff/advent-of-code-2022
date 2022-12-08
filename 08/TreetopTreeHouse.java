import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreetopTreeHouse {
  private static boolean isVisibleFromLeft(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = j - 1; k >= 0; k--) {
      int otherHeight = map.get(i).get(k);
      if (otherHeight >= height)
        return false;
    }

    return true;
  }

  private static boolean isVisibleFromRight(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = j + 1; k < map.get(0).size(); k++) {
      int otherHeight = map.get(i).get(k);
      if (otherHeight >= height)
        return false;
    }

    return true;
  }

  private static boolean isVisibleFromTop(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = i - 1; k >= 0; k--) {
      int otherHeight = map.get(k).get(j);
      if (otherHeight >= height)
        return false;
    }

    return true;
  }

  private static boolean isVisibleFromBottom(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = i + 1; k < map.size(); k++) {
      int otherHeight = map.get(k).get(j);
      if (otherHeight >= height)
        return false;
    }

    return true;
  }

  private static boolean isVisible(List<List<Integer>> map, int i, int j) {
    return isVisibleFromLeft(map, i, j) || isVisibleFromRight(map, i, j) ||
      isVisibleFromTop(map, i, j) || isVisibleFromBottom(map, i, j);
  }

  private static int getLeftViewingDistance(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = j - 1; k >= 0; k--) {
      int otherHeight = map.get(i).get(k);
      if (otherHeight >= height)
        return j - k;
    }

    return j;
  }

  private static int getRightViewingDistance(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = j + 1; k < map.get(0).size(); k++) {
      int otherHeight = map.get(i).get(k);
      if (otherHeight >= height)
        return k - j;
    }

    return map.get(0).size() - 1 - j;
  }

  private static int getTopViewingDistance(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = i - 1; k >= 0; k--) {
      int otherHeight = map.get(k).get(j);
      if (otherHeight >= height)
        return i - k;
    }

    return i;
  }

  private static int getBottomViewingDistance(List<List<Integer>> map, int i, int j) {
    int height = map.get(i).get(j);

    for (int k = i + 1; k < map.size(); k++) {
      int otherHeight = map.get(k).get(j);
      if (otherHeight >= height)
        return k - i;
    }

    return map.size() - 1 - i;
  }

  private static int getScenicScore(List<List<Integer>> map, int i, int j) {
    return getLeftViewingDistance(map, i, j) * 
      getRightViewingDistance(map, i, j) *
      getTopViewingDistance(map, i, j) *
      getBottomViewingDistance(map, i, j);
  }

  public static void main(String[] args) {
    List<List<Integer>> map = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      ArrayList<Integer> row = new ArrayList<>();
      for (int i = 0; i < line.length(); i++) {
        row.add(line.charAt(i) - '0');
      }

      map.add(row);
    }

    scanner.close();

    int visibleTreeCount = 0;

    for (int i = 0; i < map.size(); i++) {
      for (int j = 0; j < map.get(0).size(); j++) {
        if (isVisible(map, i, j)) {
          visibleTreeCount++;
        }
      }
    }

    System.out.println(visibleTreeCount);

    int maxScenicScore = 0;

    for (int i = 0; i < map.size(); i++) {
      for (int j = 0; j < map.get(0).size(); j++) {
        int scenicScore = getScenicScore(map, i, j);
        if (scenicScore > maxScenicScore) {
          maxScenicScore = scenicScore;
        }
      }
    }

    System.out.println(maxScenicScore);
  }
}
