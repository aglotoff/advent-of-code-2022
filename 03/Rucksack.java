import java.util.Scanner;
import java.util.HashSet;

public class Rucksack {
  private static int getItemPriority(char item) {
    if (item >= 'a' && item <= 'z') {
      return item - 'a' + 1;
    }
    if (item >= 'A' && item <= 'Z') {
      return item - 'A' + 27;
    }
    throw new IllegalArgumentException("Invalid item type: " + item);
  }

  public static void main(String[] args) {
    int prioritySum = 0;

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String items = scanner.nextLine();

      String firstCompartment = items.substring(0, items.length() / 2);
      String secondCompartment = items.substring(items.length() / 2);

      HashSet<Character> set = new HashSet<>();
      for (int i = 0; i < firstCompartment.length(); i++) {
        set.add(firstCompartment.charAt(i));
      }

      for (int i = 0; i < secondCompartment.length(); i++) {
        char item = secondCompartment.charAt(i);
        if (set.contains(item)) {
          prioritySum += getItemPriority(item);
          break;
        }
      }
    }

    scanner.close();

    System.out.println(prioritySum);
  }
}
