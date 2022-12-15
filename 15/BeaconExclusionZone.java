import java.text.ParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.HashSet;

public class BeaconExclusionZone {
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
    long row = Long.parseLong(args[0]);

    HashSet<Position> sensors = new HashSet<>();
    HashSet<Position> beacons = new HashSet<>();
    HashMap<Position, Long> beaconDistanceBySensor = new HashMap<>();

    long minX = 0;
    long maxX = 0;

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
      sensors.add(sensor);

      if (!matcher.find()) {
        scanner.close();
        throw new ParseException(line, 0);
      }

      long beaconX = Long.parseLong(matcher.group(1));
      long beaconY = Long.parseLong(matcher.group(2));
      Position beacon = new Position(beaconX, beaconY);
      beacons.add(beacon);

      long distance = sensor.distanceTo(beacon);
      beaconDistanceBySensor.put(sensor, distance);

      minX = Math.min(minX, sensor.x - distance);
      maxX = Math.max(maxX, sensor.x + distance);
    }

    scanner.close();

    long count = 0;

    for (long x = minX; x <= maxX; x++) {
      Position p = new Position(x, row);

      if (beacons.contains(p)) {
        continue;
      }

      for (Position sensor: sensors) {
        if (sensor.distanceTo(p) <= beaconDistanceBySensor.get(sensor)) {
          count++;
          break;
        }
      }
    }

    System.out.println(count);
  }
}
