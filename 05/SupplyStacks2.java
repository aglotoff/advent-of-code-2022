import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.LinkedList;

public class SupplyStacks2 {
  public static void main(String[] args) {
    TreeMap<Integer, LinkedList<Character>> stacks = new TreeMap<>();

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (!line.contains("[")) {
        break;
      }

      for (int linePos = 1, stackNo = 1; linePos < line.length(); linePos += 4, stackNo++) {
        char crate = line.charAt(linePos);
        if (crate != ' ') {
          if (!stacks.containsKey(stackNo)) {
            stacks.put(stackNo, new LinkedList<>());
          }
          stacks.get(stackNo).addFirst(crate);
        }
      }
    }

    if (scanner.hasNextLine()) {
      scanner.nextLine();
    }

    Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    while (scanner.hasNextLine()) {
      Matcher matcher = pattern.matcher(scanner.nextLine());
      if (matcher.find()) {
        int amount = Integer.parseInt(matcher.group(1));
        int fromNo = Integer.parseInt(matcher.group(2));
        int toNo = Integer.parseInt(matcher.group(3));

        LinkedList<Character> fromStack = stacks.get(fromNo);
        LinkedList<Character> toStack = stacks.get(toNo);
        LinkedList<Character> removedCrates = new LinkedList<>();

        for (int i = 0; i < amount; i++) {
          removedCrates.addFirst(fromStack.removeLast());
        }

        for (char crate: removedCrates) {
          toStack.addLast(crate);
        }
      }
    }

    scanner.close();

    for (LinkedList<Character> stack: stacks.values()) {
      System.out.print(stack.getLast());
    }
    System.out.println();
  }
}
