import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotEnoughMinerals {
  private static class Blueprint {
    private class State {
      private long ore;
      private long clay;
      private long obsidian;
      private long geode;
      private long oreRobots;
      private long clayRobots;
      private long obsidianRobots;
      private long geodeRobots;
      private int minutesLeft;

      @Override
      public int hashCode() {
        int h = Long.hashCode(ore);
        h = 31 * h + Long.hashCode(clay);
        h = 31 * h + Long.hashCode(obsidian);
        h = 31 * h + Long.hashCode(geode);
        h = 31 * h + Long.hashCode(oreRobots);
        h = 31 * h + Long.hashCode(clayRobots);
        h = 31 * h + Long.hashCode(obsidianRobots);
        h = 31 * h + Long.hashCode(geodeRobots);
        h = 31 * h + minutesLeft;
        return h;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }

        if (o == null || !(o instanceof State)) {
          return false;
        }

        State s = (State) o;
        return ore == s.ore && clay == s.clay && obsidian == s.obsidian &&
          geode == s.geode && oreRobots == s.oreRobots &&
          clayRobots == s.clayRobots && obsidianRobots == s.obsidianRobots &&
          geodeRobots == s.geodeRobots && minutesLeft == s.minutesLeft;
      }

      public State next() {
        State s = new State();

        s.ore = ore + oreRobots;
        s.clay = clay + clayRobots;
        s.obsidian = obsidian + obsidianRobots;
        s.geode = geode + geodeRobots;
        s.oreRobots = oreRobots;
        s.clayRobots = clayRobots;
        s.obsidianRobots = obsidianRobots;
        s.geodeRobots = geodeRobots;
        s.minutesLeft = minutesLeft - 1;

        return s;
      }

      public boolean canBuildOreRobot() {
        return ore >= orePerOreRobot;
      }

      public boolean canBuildClayRobot() {
        return ore >= orePerClayRobot;
      }

      public boolean canBuildObsidianRobot() {
        return ore >= orePerObsidianRobot && clay >= clayPerObsidianRobot;
      }

      public boolean canBuildGeodeRobot() {
        return ore >= orePerGeodeRobot && obsidian >= obsidianPerGeodeRobot;
      }

      public State buildOreRobot() {
        State s = next();
        s.ore -= orePerOreRobot;
        s.oreRobots++;
        return s;
      }

      public State buildClayRobot() {
        State s = next();
        s.ore -= orePerClayRobot;
        s.clayRobots++;
        return s;
      }

      public State buildObsidianRobot() {
        State s = next();
        s.ore -= orePerObsidianRobot;
        s.clay -= clayPerObsidianRobot;
        s.obsidianRobots++;
        return s;
      }

      public State buildGeodeRobot() {
        State s = next();
        s.ore -= orePerGeodeRobot;
        s.obsidian -= obsidianPerGeodeRobot;
        s.geodeRobots++;
        return s;
      }
    }

    private int orePerOreRobot;
    private int orePerClayRobot;
    private int orePerObsidianRobot;
    private int clayPerObsidianRobot;
    private int orePerGeodeRobot;
    private int obsidianPerGeodeRobot;

    public Blueprint(int orePerOre, int orePerClay, int orePerObsidian,
        int clayPerObsidian, int orePerGeode, int obsidianPerGeode) {
      orePerOreRobot = orePerOre;
      orePerClayRobot = orePerClay;
      orePerObsidianRobot = orePerObsidian;
      clayPerObsidianRobot = clayPerObsidian;
      orePerGeodeRobot = orePerGeode;
      obsidianPerGeodeRobot = obsidianPerGeode;
    }

    private long search(State s, HashMap<State, Long> cache, HashMap<Integer, Long> best) {
      if (cache.containsKey(s)) {
        return cache.get(s);
      }

      long max = s.geode;

      if (s.minutesLeft > 0) {
        long bestPossible = s.geode + s.minutesLeft * s.geodeRobots + s.minutesLeft * (s.minutesLeft - 1) / 2;
        if (best.containsKey(s.minutesLeft) && best.get(s.minutesLeft) >= bestPossible) {
          cache.put(s, max);
          return max;
        }

        if (s.canBuildGeodeRobot() && s.minutesLeft > 1) {
          max = Math.max(max, search(s.buildGeodeRobot(), cache, best));
        } else {
          if (s.canBuildObsidianRobot() &&
              s.obsidianRobots < obsidianPerGeodeRobot &&
              s.minutesLeft > 2) {
            max = Math.max(max, search(s.buildObsidianRobot(), cache, best));
          }

          if (s.canBuildClayRobot() &&
              s.clayRobots < clayPerObsidianRobot &&
              s.minutesLeft > 3) {
            max = Math.max(max, search(s.buildClayRobot(), cache, best));
          }

          if (s.canBuildOreRobot() &&
              (s.oreRobots < orePerObsidianRobot || 
              s.oreRobots < orePerClayRobot ||
              s.oreRobots < orePerOreRobot) &&
              s.minutesLeft > 2) {
            max = Math.max(max, search(s.buildOreRobot(), cache, best));
          }

          max = Math.max(max, search(s.next(), cache, best));
        }
      }

      if (!best.containsKey(s.minutesLeft) || best.get(s.minutesLeft) < max) {
        best.put(s.minutesLeft, max);
      }

      cache.put(s, max);
      return max;
    }

    private long search(int minutesLeft) {
      State initial = new State();
      initial.oreRobots = 1;
      initial.minutesLeft = minutesLeft;

      return search(initial, new HashMap<>(), new HashMap<>());
    }
  }

  public static void main(String[] args) {
    ArrayList<Blueprint> blueprints = new ArrayList<>();

    Pattern pattern = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      Matcher matcher = pattern.matcher(scanner.nextLine());
      if (matcher.find()) {
        int orePerOre = Integer.parseInt(matcher.group(2));
        int orePerClay = Integer.parseInt(matcher.group(3));
        int orePerObsidian = Integer.parseInt(matcher.group(4));
        int clayPerObsidian = Integer.parseInt(matcher.group(5));
        int orePerGeode = Integer.parseInt(matcher.group(6));
        int obsidianPerGeode = Integer.parseInt(matcher.group(7));

        blueprints.add(new Blueprint(orePerOre, orePerClay, orePerObsidian,
            clayPerObsidian, orePerGeode, obsidianPerGeode));
      }
    }

    scanner.close();

    long qualityLevelSum = 0;

    for (int i = 0; i < blueprints.size(); i++) {
      qualityLevelSum += blueprints.get(i).search(24) * (i + 1);
    }

    System.out.println(qualityLevelSum);

    long maxGeodesProduct = 1;

    for (int i = 0; i < 3; i++) {
      maxGeodesProduct *= blueprints.get(i).search(32);
    }

    System.out.println(maxGeodesProduct);
  }
}
