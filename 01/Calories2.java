import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Calories2 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    ArrayList<Integer> list = new ArrayList<>();
    int currentElfCalories = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.length() == 0) {
        list.add(currentElfCalories);
        currentElfCalories = 0;
      } else {
        int itemCalories = Integer.parseInt(line);
        currentElfCalories += itemCalories;
      }
    }

    list.add(currentElfCalories);

    scanner.close();

    list.sort(Collections.reverseOrder());
   
    int total = 0;
    for (int i = 0; i < 3; i++) {
      total += list.get(i);
    }

    System.out.println(total);
  }
}
