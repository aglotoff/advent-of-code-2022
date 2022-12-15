import java.text.ParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public class BeaconExclusionZone2 {
  private static class Position {
    private long x;
    private long y;

    public Position(long x, long y) {
      this.x = x;
      this.y = y;
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || !(o instanceof Position)) {
        return false;
      }
      Position p = (Position) o;
      return p.x == x && p.y == y;
    }

    public int hashCode() {
      return 31 * Long.hashCode(x) + Long.hashCode(y);
    }

    public long distanceTo(Position p) {
      return Math.abs(x - p.x) + Math.abs(y - p.y);
    }
  }

  public static void main(String[] args) throws ParseException {
    long maxCoordinate = Long.parseLong(args[0]);

    HashMap<Position, Long> beaconDistanceBySensor = new HashMap<>();

    Pattern pattern = Pattern.compile("x=(-?\\d+), y=(-?\\d+)");
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Matcher matcher = pattern.matcher(line);

      if (!matcher.find()) {
        scanner.close();
        throw new ParseException(line, 0);
      }

      long sensorX = Long.parseLong(matcher.group(1));
      long sensorY = Long.parseLong(matcher.group(2));
      Position sensor = new Position(sensorX, sensorY);

      if (!matcher.find()) {
        scanner.close();
        throw new ParseException(line, 0);
      }

      long beaconX = Long.parseLong(matcher.group(1));
      long beaconY = Long.parseLong(matcher.group(2));
      Position beacon = new Position(beaconX, beaconY);

      long distance = sensor.distanceTo(beacon);
      beaconDistanceBySensor.put(sensor, distance);
    }

    scanner.close();

    for (long y = 0; y <= maxCoordinate; y++) {
      Position p = new Position(0, y);

      while (p.x <= maxCoordinate) {
        Position sensor = null;

        for (Position s: beaconDistanceBySensor.keySet()) {
          if (s.distanceTo(p) <= beaconDistanceBySensor.get(s)) {
            sensor = s;
            break;
          }
        }

        if (sensor == null) {
          System.out.println(p.x * 4000000L + p.y);
          return;
        }

        long distance = beaconDistanceBySensor.get(sensor);
        p.x = sensor.x + distance - Math.abs(p.y - sensor.y) + 1;
      }
    }
  }
}
