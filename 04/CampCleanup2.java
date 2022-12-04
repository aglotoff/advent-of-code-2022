import java.util.Scanner;

public class CampCleanup2 {
  private static class Range {
    private int start;
    private int end;

    private Range(int from, int to) {
      start = from;
      end = to;
    }

    private boolean overlaps(Range other) {
      return (start <= other.end) && (end >= other.start);
    }

    private static Range parseRange(String s) {
      String[] stringRange = s.split("-");
      return new Range(Integer.parseInt(stringRange[0]), Integer.parseInt(stringRange[1]));
    }
  }

  public static void main(String[] args) {
    int count = 0;

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] pair = scanner.nextLine().split(",");

      Range firstRange = Range.parseRange(pair[0]);
      Range secondRange = Range.parseRange(pair[1]);

      if (firstRange.overlaps(secondRange)) {
        count++;
      }
    }

    scanner.close();

    System.out.println(count);
  }
}
