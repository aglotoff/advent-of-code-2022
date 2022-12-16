import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ProboscideaVolcanium2 {
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

  private static class State2 {
    int myValve;
    int elephantValve;
    long openValves;
    int myTime;
    int elephantTime;

    public State2(int c1, int c2, long o, int m1, int m2) {
      myValve = c1;
      elephantValve = c2;
      openValves = o;
      myTime = m1;
      elephantTime = m2;
    }

    public int hashCode() {
      int h = myValve;
      h = 31 * h + elephantValve;
      h = 31 * h + Long.hashCode(openValves);
      h = 31 * h + myTime;
      h = 31 * h + elephantTime;
      return h;
    }

    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o == null || !(o instanceof State2)) {
        return false;
      }
      State2 s = (State2) o;


      return openValves == s.openValves &&
        (((myValve == s.myValve) && (elephantValve == s.elephantValve) && (myTime == s.myTime) && (elephantTime == s.elephantTime)) ||
        ((myValve == s.elephantValve) && (elephantValve == s.myValve) && (myTime == s.elephantTime) && (elephantTime == s.myTime)));
    }
  }

  private static long search2(State2 state, HashMap<State2, Long> cache,
      int n, int[][] dist,
      HashMap<Integer, Long> rateByValve, int turn) {
    if (cache.containsKey(state)) {
      return cache.get(state);
    }

    long maxFlow = 0;

    if (turn == 0) {
      for (int v = 0; v < n; v++) {
        long rate = rateByValve.get(v);

        if (((state.openValves & (1L << v)) != 0) || (rate == 0)) {
          continue;
        }
  
        int steps = dist[state.myValve][v] + 1;
        if (steps >= (26 - state.myTime)) {
          continue;
        }
  
        long additionalFlow = rate * (26 - state.myTime - steps);
        additionalFlow += search2(
          new State2(v, state.elephantValve, state.openValves | (1L << v), state.myTime + steps, state.elephantTime),
          cache,
          n,
          dist,
          rateByValve,
          (turn + 1) % 2
          );
  
        if (additionalFlow > maxFlow) {
          maxFlow = additionalFlow;
        }
      }
    } else {
      for (int v = 0; v < n; v++) {
        long rate = rateByValve.get(v);

        if (((state.openValves & (1L << v)) != 0) || (rate == 0)) {
          continue;
        }
  
        int steps = dist[state.elephantValve][v] + 1;
        if (steps >= (26 - state.elephantTime)) {
          continue;
        }
  
        long additionalFlow = rate * (26 - state.elephantTime - steps);
        additionalFlow += search2(
          new State2(state.myValve, v, state.openValves | (1L << v), state.myTime, state.elephantTime + steps),
          cache,
          n,
          dist,
          rateByValve,
          (turn + 1) % 2
          );
  
        if (additionalFlow > maxFlow) {
          maxFlow = additionalFlow;
        }
      }
    }
 
    cache.put(state, maxFlow);

    return maxFlow;
  }

  private static long search2(int start, int n,
      int[][] dist, HashMap<Integer, Long> rateByValve) {
    return search2(new State2(start, start, 0, 0, 0), new HashMap<>(), n,
        dist, rateByValve, 0);
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
    System.out.println(search2(start, n, dist, rateByValve));
  }
}
