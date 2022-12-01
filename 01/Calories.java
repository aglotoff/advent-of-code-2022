import java.util.Scanner;

public class Calories {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int maxCalories = 0;
    int currentElfCalories = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.length() == 0) {
        if (currentElfCalories > maxCalories) {
          maxCalories = currentElfCalories;
        }
        currentElfCalories = 0;
      } else {
        int itemCalories = Integer.parseInt(line);
        currentElfCalories += itemCalories;
      }
    }

    scanner.close();

    if (currentElfCalories > maxCalories) {
      maxCalories = currentElfCalories;
    }

    System.out.println(maxCalories);
  }
}
