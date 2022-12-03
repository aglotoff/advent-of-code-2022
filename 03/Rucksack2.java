import java.util.Scanner;
import java.util.HashSet;

public class Rucksack2 {
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
      String firstRucksack = scanner.nextLine();
      String secondRucksack = scanner.nextLine();
      String thirdRucksack = scanner.nextLine();

      HashSet<Character> firstSet = new HashSet<>();
      for (int i = 0; i < firstRucksack.length(); i++) {
        firstSet.add(firstRucksack.charAt(i));
      }

      HashSet<Character> secondSet = new HashSet<>();
      for (int i = 0; i < secondRucksack.length(); i++) {
        secondSet.add(secondRucksack.charAt(i));
      }

      for (int i = 0; i < thirdRucksack.length(); i++) {
        char item = thirdRucksack.charAt(i);
        if (firstSet.contains(item) && secondSet.contains(item)) {
          prioritySum += getItemPriority(item);
          break;
        }
      }
    }

    scanner.close();

    System.out.println(prioritySum);
  }
}
