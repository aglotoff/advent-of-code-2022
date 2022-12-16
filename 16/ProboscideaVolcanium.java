import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ProboscideaVolcanium {
  private static class SearchNode implements Comparable<SearchNode> {
    private String name;
    private int dist;

    public SearchNode(String name, int dist) {
      this.name = name;
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
      return name.equals(s.name);
    }

    @Override
    public int compareTo(SearchNode o) {
      return Long.compare(dist, o.dist);
    }
  }

  private static class State {
    int currentValve;
    long openValves;
    int currentTime;

    public State(int c, long o, int m) {
      currentValve = c;
      openValves = o;
      currentTime = m;
    }

    public int hashCode() {
      return 31 * ((31 * Long.hashCode(openValves)) + currentTime) + currentValve;
    }

    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o == null || !(o instanceof State)) {
        return false;
      }
      State s = (State) o;
      return currentValve == s.currentValve && openValves == s.openValves && currentTime == s.currentTime;
    }
  }

  private static long search(State state, HashMap<State, Long> flow,
      int n, int[][] dist,
      HashMap<Integer, Long> rateByValve) {
    if (flow.containsKey(state)) {
      return flow.get(state);
    }

    long maxFlow = 0;

    for (int v = 0; v < n; v++) {
      long rate = rateByValve.get(v);

      if (((state.openValves & (1L << v)) != 0) || (rate == 0)) {
        continue;
      }

      int steps = dist[state.currentValve][v] + 1;
      if (steps >= (30 - state.currentTime)) {
        continue;
      }

      long additionalFlow = rate * (30 - state.currentTime - steps);

      additionalFlow += search(
        new State(v, state.openValves | (1L << v), state.currentTime + steps),
        flow,
        n,
        dist,
        rateByValve);

      if (additionalFlow > maxFlow) {
        maxFlow = additionalFlow;
      }
    }

    flow.put(state, maxFlow);

    return maxFlow;
  }

  private static long search(int start, int n, int[][] dist,
      HashMap<Integer, Long> rateByValve) {
    return search(new State(start, 0, 0), new HashMap<>(), n,
        dist, rateByValve);
  }

  public static void main(String[] args) {
    HashMap<String, Integer> valves = new HashMap<>();
    HashMap<String, LinkedList<String>> adjacentValves = new HashMap<>();
    HashMap<Integer, Long> rateByValve = new HashMap<>();

    Pattern pattern = Pattern.compile("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");
    Scanner scanner = new Scanner(System.in);

    int n;
    for (n = 0; scanner.hasNextLine(); n++) {
      Matcher matcher = pattern.matcher(scanner.nextLine());
      if (matcher.find()) {
        String name = matcher.group(1);
        long flowRate = Long.parseLong(matcher.group(2));

        LinkedList<String> adjacent = new LinkedList<>();
        for (String s: matcher.group(3).split(", ")) {
          adjacent.add(s);
        }

        valves.put(name, n);
        rateByValve.put(n, flowRate);
        adjacentValves.put(name, adjacent);
      }
    }

    scanner.close();

    int[][] dist = new int[valves.size()][valves.size()];
    
    for (String v: valves.keySet()) {
      int i = valves.get(v);

      for (int j = 0; j < dist[i].length; j++) {
        dist[i][j] = i == j ? 0 : Integer.MAX_VALUE;
      }

      PriorityQueue<SearchNode> pq = new PriorityQueue<>();
      pq.add(new SearchNode(v, 0));

      HashSet<String> visited = new HashSet<>();

      while (!pq.isEmpty()) {
        SearchNode currentValve = pq.poll();
  
        for (String name: adjacentValves.get(currentValve.name)) {
          if (visited.contains(name)) {
            continue;
          }
  
          int newDist = currentValve.dist + 1;
  
          if (newDist < dist[i][valves.get(name)]) {
            pq.add(new SearchNode(name, newDist));
            dist[i][valves.get(name)] = newDist;
          }
        }
  
        visited.add(currentValve.name);
      }
    }

    int start = valves.get("AA");
    System.out.println(search(start, n, dist, rateByValve));
  }
}
