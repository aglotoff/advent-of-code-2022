import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class MonkeyInTheMiddle {
  private static class Monkey implements Comparable<Monkey> {
    private LinkedList<Long> items;
    private String operator;
    private String term;
    private int divisor;
    private int monkeyIfTrue;
    private int monkeyIfFalse;
    private long count;

    @Override
    public int compareTo(Monkey o) {
      return Long.compare(count, o.count);
    }
  }
  
  public static void main(String[] args) {  
    ArrayList<Monkey> monkeys = new ArrayList<>();

    Scanner scanner = new Scanner(System.in)
      .useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));

    while (scanner.hasNext()) {
      String line = scanner.next();

      Matcher matcher = Pattern.compile("Monkey \\d+:\\n" +
        "  Starting items: ([^\\n]+)\\n" +
        "  Operation: new = old ([+*]) ([^\\n]+)\\n" +
        "  Test: divisible by (\\d+)\\n" +
        "    If true: throw to monkey (\\d+)\\n" +
        "    If false: throw to monkey (\\d+)",
        Pattern.MULTILINE).matcher(line);

      if (matcher.find()) {
        Monkey monkey = new Monkey();

        monkey.items = new LinkedList<>();
        for (String s: matcher.group(1).split(", ")) {
          monkey.items.addLast(Long.parseLong(s));
        }

        monkey.operator = matcher.group(2);
        monkey.term = matcher.group(3);
        monkey.divisor = Integer.parseInt(matcher.group(4));
        monkey.monkeyIfTrue = Integer.parseInt(matcher.group(5));
        monkey.monkeyIfFalse = Integer.parseInt(matcher.group(6));

        monkeys.add(monkey);
      }
    }

    scanner.close();

    for (int r = 0; r < 20; r++) {
      for (Monkey monkey: monkeys) {
        while (monkey.items.size() > 0) {
          monkey.count++;

          long worryLevel = monkey.items.removeFirst();

          long termValue = monkey.term.equals("old")
            ? worryLevel
            : Integer.parseInt(monkey.term);

          if (monkey.operator.equals("+")) {
            worryLevel += termValue;
          } else if (monkey.operator.equals("*")) {
            worryLevel *= termValue;
          }

          worryLevel /= 3;

          if (worryLevel % monkey.divisor == 0) {
            monkeys.get(monkey.monkeyIfTrue).items.addLast(worryLevel);
          } else {
            monkeys.get(monkey.monkeyIfFalse).items.addLast(worryLevel);
          }
        }
      }
    }

    PriorityQueue<Monkey> pq = new PriorityQueue<>(Collections.reverseOrder());
    pq.addAll(monkeys);

    System.out.println(pq.poll().count * pq.poll().count);
  }
}
